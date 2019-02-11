package me.prestige.bases.faction.type;

import com.customhcf.util.BukkitUtils;
import com.customhcf.util.GenericUtils;
import com.customhcf.util.cuboid.Cuboid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.prestige.bases.Bases;
import me.prestige.bases.faction.claim.Claim;
import me.prestige.bases.faction.event.FactionClaimChangeEvent;
import me.prestige.bases.faction.event.FactionClaimChangedEvent;
import me.prestige.bases.faction.event.cause.ClaimChangeCause;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ClaimableFaction extends Faction {
    protected static final ImmutableMap<World.Environment, String>  ENVIRONMENT_MAPPINGS = Maps.immutableEnumMap(ImmutableMap.of(World.Environment.NETHER, "Nether", World.Environment.NORMAL, "Overworld", World.Environment.THE_END, "The End"));


    protected final Set<Claim> claims;

    public ClaimableFaction(final String name) {
        super(name);
        this.claims = new HashSet<>();
    }

    public ClaimableFaction(final Map<String, Object> map) {
        super(map);
        (this.claims = new HashSet<>()).addAll(GenericUtils.createList(map.get("claims"), Claim.class));
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = super.serialize();
        map.put("claims", new ArrayList(claims));
        return map;
    }

    @Override
    public void printDetails(final CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(this.getDisplayName(sender));
        for(final Claim claim : this.claims) {
            final Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.WHITE.toString() +  ClaimableFaction.ENVIRONMENT_MAPPINGS.get( location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ());
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public Set<Claim> getClaims() {
        return  this.claims;
    }

    public boolean addClaim(final Claim claim,  final CommandSender sender) {
        return this.addClaims(Collections.singleton(claim), sender);
    }

    public void setClaim(final Cuboid cuboid, final CommandSender sender) {
        this.removeClaims(this.getClaims(), sender);
        final Location min = cuboid.getMinimumPoint();
        min.setY(0);
        final Location max = cuboid.getMaximumPoint();
        max.setY(256);
        addClaim(new Claim(this, min, max), sender);
    }

    public boolean addClaims(final Collection<Claim> adding,  CommandSender sender) {
        if(sender == null) {
            sender =  Bukkit.getConsoleSender();
        }
        final FactionClaimChangeEvent event = new FactionClaimChangeEvent(sender, ClaimChangeCause.CLAIM, adding, this);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled() || !this.claims.addAll(adding)) {
            return false;
        }
        Bukkit.getPluginManager().callEvent( new FactionClaimChangedEvent(sender, ClaimChangeCause.CLAIM, adding));
        return true;
    }

    public boolean removeClaim(final Claim claim,  final CommandSender sender) {
        return this.removeClaims(Collections.singleton(claim), sender);
    }

    public boolean removeClaims(final Collection<Claim> removing,  CommandSender sender) {
        Bukkit.getPluginManager().callEvent( new FactionClaimChangedEvent(sender, ClaimChangeCause.UNCLAIM, removing));
        return true;
    }
}