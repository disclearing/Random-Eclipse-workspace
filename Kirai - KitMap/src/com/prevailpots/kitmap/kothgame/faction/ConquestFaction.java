package com.prevailpots.kitmap.kothgame.faction;

import com.customhcf.util.BukkitUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.prevailpots.kitmap.faction.claim.Claim;
import com.prevailpots.kitmap.kothgame.CaptureZone;
import com.prevailpots.kitmap.kothgame.EventType;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class ConquestFaction extends CapturableFaction implements ConfigurationSerializable
{
    private final EnumMap<ConquestZone, CaptureZone> captureZones;

    public ConquestFaction(final String name) {
        super(name);
        this.setDeathban(true);
        this.captureZones = new EnumMap<ConquestZone, CaptureZone>(ConquestZone.class);
    }

    public ConquestFaction(final Map<String, Object> map) {
        super(map);
        this.setDeathban(true);
        this.captureZones = new EnumMap<ConquestZone, CaptureZone>(ConquestZone.class);
        Object object;
        if ((object = map.get("red")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.RED, (CaptureZone)object);
        }
        if ((object = map.get("green")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.GREEN, (CaptureZone)object);
        }
        if ((object = map.get("blue")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.BLUE, (CaptureZone)object);
        }
        if ((object = map.get("WHITE")) instanceof CaptureZone) {
            this.captureZones.put(ConquestZone.WHITE, (CaptureZone)object);
        }
    }

    public Map<String, Object> serialize() {
        final Map<String, Object> map = super.serialize();
        for (final Map.Entry<ConquestZone, CaptureZone> entry : this.captureZones.entrySet()) {
            map.put(entry.getKey().name().toLowerCase(), entry.getValue());
        }
        return map;
    }

    public EventType getEventType() {
        return EventType.CONQUEST;
    }

    public void printDetails(final CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(this.getDisplayName(sender));
        for (final Claim claim : this.claims) {
            final Location location = claim.getCenter();
            sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.RED + '(' + (String)ConquestFaction.ENVIRONMENT_MAPPINGS.get((Object)location.getWorld().getEnvironment()) + ", " + location.getBlockX() + " | " + location.getBlockZ() + ')');
        }
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    public void setZone(final ConquestZone conquestZone, final CaptureZone captureZone) {
        switch (conquestZone) {
            case RED: {
                this.captureZones.put(ConquestZone.RED, captureZone);
                break;
            }
            case BLUE: {
                this.captureZones.put(ConquestZone.BLUE, captureZone);
                break;
            }
            case GREEN: {
                this.captureZones.put(ConquestZone.GREEN, captureZone);
                break;
            }
            case WHITE: {
                this.captureZones.put(ConquestZone.WHITE, captureZone);
                break;
            }
            default: {
                throw new AssertionError((Object)"Unsupported operation");
            }
        }
    }

    public CaptureZone getRed() {
        return this.captureZones.get(ConquestZone.RED);
    }

    public CaptureZone getGreen() {
        return this.captureZones.get(ConquestZone.GREEN);
    }

    public CaptureZone getBlue() {
        return this.captureZones.get(ConquestZone.BLUE);
    }

    public CaptureZone getWHITE() {
        return this.captureZones.get(ConquestZone.WHITE);
    }

    public Collection<ConquestZone> getConquestZones() {
        return (Collection<ConquestZone>)ImmutableSet.copyOf((Collection)this.captureZones.keySet());
    }

    public List<CaptureZone> getCaptureZones() {
        return (List<CaptureZone>)ImmutableList.copyOf((Collection)this.captureZones.values());
    }

    public enum ConquestZone
    {
        RED(ChatColor.RED, "Red"),
        BLUE(ChatColor.AQUA, "Blue"),
        WHITE(ChatColor.YELLOW, "WHITE"),
        GREEN(ChatColor.GREEN, "Green"), ;

        private final String name;
        private final ChatColor color;
        private static final Map<String, ConquestZone> BY_NAME;

        private ConquestZone(final ChatColor color, final String name) {
            this.color = color;
            this.name = name;
        }

        public ChatColor getColor() {
            return this.color;
        }

        public String getName() {
            return this.name;
        }

        public String getDisplayName() {
            return this.color.toString() + this.name;
        }

        public static ConquestZone getByName(final String name) {
            return ConquestZone.BY_NAME.get(name.toUpperCase());
        }

        public static Collection<String> getNames() {
            return new ArrayList<String>(ConquestZone.BY_NAME.keySet());
        }

        static {
            final ImmutableMap.Builder<String, ConquestZone> builder = ImmutableMap.builder();
            for (final ConquestZone zone : values()) {
                builder.put(zone.name().toUpperCase(), zone);
            }
            BY_NAME = (Map)builder.build();
        }
    }
}