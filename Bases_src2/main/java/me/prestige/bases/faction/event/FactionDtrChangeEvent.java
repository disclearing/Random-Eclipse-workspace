package me.prestige.bases.faction.event;

import me.prestige.bases.faction.struct.Raidable;
import me.prestige.bases.faction.type.ColorFaction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FactionDtrChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final DtrUpdateCause cause;
    private final ColorFaction raidable;
    private final double originalDtr;
    private boolean cancelled;
    private double newDtr;

    public FactionDtrChangeEvent(final DtrUpdateCause cause, final ColorFaction raidable, final double originalDtr, final double newDtr) {
        this.cause = cause;
        this.raidable = raidable;
        this.originalDtr = originalDtr;
        this.newDtr = newDtr;
    }

    public static HandlerList getHandlerList() {
        return FactionDtrChangeEvent.handlers;
    }

    public DtrUpdateCause getCause() {
        return this.cause;
    }

    public ColorFaction getRaidable() {
        return this.raidable;
    }

    public double getOriginalDtr() {
        return this.originalDtr;
    }

    public double getNewDtr() {
        return this.newDtr;
    }

    public void setNewDtr(final double newDtr) {
        this.newDtr = newDtr;
    }

    public boolean isCancelled() {
        return this.cancelled || Math.abs(this.newDtr - this.originalDtr) == 0.0;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return FactionDtrChangeEvent.handlers;
    }

    public enum DtrUpdateCause {
        REGENERATION,
        MEMBER_DEATH;
    }
}