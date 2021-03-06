package com.prevailpots.hcf.visualise;

import com.customhcf.util.cuboid.Cuboid;
import com.google.common.base.Predicate;
import com.prevailpots.hcf.HCF;
import com.prevailpots.hcf.faction.claim.Claim;
import com.prevailpots.hcf.faction.type.ClaimableFaction;
import com.prevailpots.hcf.faction.type.Faction;
import com.prevailpots.hcf.faction.type.RoadFaction;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WallBorderListener implements Listener {
    private static final int WALL_BORDER_HEIGHT_BELOW_DIFF = 3;
    private static final int WALL_BORDER_HEIGHT_ABOVE_DIFF = 4;
    private static final int WALL_BORDER_HORIZONTAL_DISTANCE = 7;
    private final boolean useTaskInstead;
    private final Map<UUID, BukkitTask> wallBorderTask;
    private final HCF plugin;

    public WallBorderListener(final HCF plugin) {
        this.wallBorderTask = new HashMap<UUID, BukkitTask>();
        this.plugin = plugin;
        this.useTaskInstead = (plugin.getRandom().nextBoolean() && false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if(!this.useTaskInstead) {
            return;
        }
        final BukkitTask task = this.wallBorderTask.remove(event.getPlayer().getUniqueId());
        if(task != null) {
            task.cancel();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if(this.useTaskInstead) {
            this.wallBorderTask.put(player.getUniqueId(), new WarpTimerRunnable(this, player).runTaskTimer((Plugin) this.plugin, 3L, 3L));
            return;
        }
        final Location now = player.getLocation();
        new BukkitRunnable() {
            public void run() {
                final Location location = player.getLocation();
                if(now.equals((Object) location)) {
                    WallBorderListener.this.handlePositionChanged(player, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                }
            }
        }.runTaskLater((Plugin) this.plugin, 4L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if(this.useTaskInstead) {
            return;
        }
        final Location to = event.getTo();
        final int toX = to.getBlockX();
        final int toY = to.getBlockY();
        final int toZ = to.getBlockZ();
        final Location from = event.getFrom();
        if(from.getBlockX() != toX || from.getBlockY() != toY || from.getBlockZ() != toZ) {
            this.handlePositionChanged(event.getPlayer(), to.getWorld(), toX, toY, toZ);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        this.onPlayerMove((PlayerMoveEvent) event);
    }

    private void handlePositionChanged(Player player, final World toWorld, final int toX, final int toY, final int toZ) {
        VisualType visualType;
        if(this.plugin.getTimerManager().spawnTagTimer.getRemaining(player) > 0L) {
            visualType = VisualType.SPAWN_BORDER;
        } else {
            if(this.plugin.getTimerManager().pvpProtectionTimer.getRemaining(player) <= 0L) {
                return;
            }
            visualType = VisualType.CLAIM_BORDER;
        }

        this.plugin.getVisualiseHandler().clearVisualBlocks(player, visualType, visualBlock -> {
            Location other = visualBlock.getLocation();
            if (!other.getWorld().equals(toWorld)) return false;
            if (Math.abs(toX - other.getBlockX()) > 7) return true;
            if (Math.abs(toY - other.getBlockY()) > 4) return true;
            if (Math.abs(toZ - other.getBlockZ()) <= 7) return false;
            return true;
        });
        final int minHeight = toY - WALL_BORDER_HEIGHT_BELOW_DIFF;
        final int maxHeight = toY + WALL_BORDER_HEIGHT_ABOVE_DIFF;
        final int minX = toX - WALL_BORDER_HORIZONTAL_DISTANCE;
        final int maxX = toX + WALL_BORDER_HORIZONTAL_DISTANCE;
        final int minZ = toZ - WALL_BORDER_HORIZONTAL_DISTANCE;
        final int maxZ = toZ + WALL_BORDER_HORIZONTAL_DISTANCE;
        final Collection<Claim> added = new HashSet<Claim>();
        for(int x = minX; x < maxX; ++x) {
            for(int z = minZ; z < maxZ; ++z) {
                final Faction faction = this.plugin.getFactionManager().getFactionAt(toWorld, x, z);
                if(faction instanceof ClaimableFaction) {

                    if(visualType == VisualType.SPAWN_BORDER) {
                        if(!faction.isSafezone()) {
                            continue;
                        }
                    } else if(visualType == VisualType.CLAIM_BORDER) {
                        if(faction instanceof RoadFaction) {
                            continue;
                        }
                        if(faction.isSafezone()) {
                            continue;
                        }
                    }
                    final Collection<Claim> claims = ((ClaimableFaction) faction).getClaims();
                    for(final Claim claim : claims) {
                        if(toWorld.equals(claim.getWorld())) {
                            added.add(claim);
                        }
                    }
                }
            }
        }
        if(!added.isEmpty()) {
            int generated = 0;
            final Iterator<Claim> iterator = added.iterator();
            while(iterator.hasNext()) {
                final Claim claim2 = iterator.next();
                final List<org.bukkit.util.Vector> edges = claim2.edges();
                for(final org.bukkit.util.Vector edge : edges) {
                    if(Math.abs(edge.getBlockX() - toX) > 7) {
                        continue;
                    }
                    if(Math.abs(edge.getBlockZ() - toZ) > 7) {
                        continue;
                    }
                    final Location location = edge.toLocation(toWorld);
                    if(location == null) {
                        continue;
                    }
                    final Location first = location.clone();
                    first.setY(minHeight);
                    final Location second = location.clone();
                    second.setY(maxHeight);
                    generated += this.plugin.getVisualiseHandler().generate(player, new Cuboid(first, second), visualType, false).size();
                }

                iterator.remove();
            }
        }
    }

    private static final class WarpTimerRunnable extends BukkitRunnable {
        private WallBorderListener listener;
        private Player player;
        private double lastX;
        private double lastY;
        private double lastZ;

        public WarpTimerRunnable(final WallBorderListener listener, final Player player) {
            this.lastX = Double.MAX_VALUE;
            this.lastY = Double.MAX_VALUE;
            this.lastZ = Double.MAX_VALUE;
            this.listener = listener;
            this.player = player;
        }

        public void run() {
            final Location location = this.player.getLocation();
            final double x = location.getBlockX();
            final double y = location.getBlockY();
            final double z = location.getBlockZ();
            if(this.lastX == x && this.lastY == y && this.lastZ == z) {
                return;
            }
            this.lastX = x;
            this.lastY = y;
            this.lastZ = z;
            this.listener.handlePositionChanged(this.player, this.player.getWorld(), (int) x, (int) y, (int) z);
        }

        public synchronized void cancel() throws IllegalStateException {
            super.cancel();
            this.listener = null;
            this.player = null;
        }
    }
}
