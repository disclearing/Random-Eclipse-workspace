package org.ipvp.hcf.listener;

import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.ipvp.hcf.DurationFormatter;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.timer.PlayerTimer;

import java.util.UUID;

public class PortalListener implements Listener {

    private static final long PORTAL_MESSAGE_DELAY_THRESHOLD = 2500L;

    private final Location endExit = new Location(Bukkit.getWorld("world"), 0, 70.5, 200); // TODO: configurable

    private final TObjectLongMap<UUID> messageDelays = new TObjectLongHashMap<>();
    private final HCF plugin;

    public PortalListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWaterEnter(PlayerMoveEvent e) {
        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
            if (e.getTo().getBlock().getType() == Material.WATER || e.getTo().getBlock().getType() == Material.STATIONARY_WATER || e.getTo().getBlock().getType() == Material.ENDER_PORTAL) {
                if (e.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
                    e.getPlayer().teleport(endExit);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }

        if (event.getTo() != null) {
            World toWorld = event.getTo().getWorld();
            if (toWorld != null && toWorld.getEnvironment() == World.Environment.THE_END) {
                event.useTravelAgent(false);
                event.setTo(toWorld.getSpawnLocation());
                return;
            }
        }

        World fromWorld = event.getFrom().getWorld();
        if (fromWorld != null && fromWorld.getEnvironment() == World.Environment.THE_END) {
            event.useTravelAgent(false);
            event.setTo(endExit);
        }
    }

    // Prevent players jumping the End with Strength.
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World from = event.getFrom();
        World to = player.getWorld();
        if (from.getEnvironment() != World.Environment.THE_END && to.getEnvironment() == World.Environment.THE_END && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPortalEnter(PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }

        Location to = event.getTo();
        World toWorld = to.getWorld();
        if (toWorld == null)
            return; // safe-guard if the End or Nether is disabled

        if (toWorld.getEnvironment() == World.Environment.THE_END) {
            Player player = event.getPlayer();

            // Prevent entering the end if it's closed.
            if (false /* TODO:plugin.getEndManager().isOpened() */) {
                message(player, ChatColor.RED + "The End is currently closed.");
                event.setCancelled(true);
                return;
            }

            // Prevent entering the end if the player is Spawn Tagged.
            PlayerTimer timer = plugin.getTimerManager().getCombatTimer();
            long remaining;
            if ((remaining = timer.getRemaining(player)) > 0L) {
                message(player, ChatColor.RED + "You cannot enter the End whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active [" + ChatColor.BOLD
                        + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.RED + " remaining]");

                event.setCancelled(true);
                return;
            }

            // Prevent entering the end if the player is PVP Protected.
            if ((remaining = timer.getRemaining(player)) > 0L) {
                message(player, ChatColor.RED + "You cannot enter the End whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active [" + ChatColor.BOLD
                        + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.RED + " remaining]");

                event.setCancelled(true);
                return;
            }

            event.useTravelAgent(false);
            event.setTo(toWorld.getSpawnLocation().add(0.5, 0, 0.5));
        }
    }

    private void message(Player player, String message) {
        long last = messageDelays.get(player.getUniqueId());
        long millis = System.currentTimeMillis();
        if (last != messageDelays.getNoEntryValue() && (last + PORTAL_MESSAGE_DELAY_THRESHOLD) - millis > 0L) {
            return;
        }

        messageDelays.put(player.getUniqueId(), millis);
        player.sendMessage(message);
    }
}