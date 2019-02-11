package me.prestige.bases.timer;

import me.prestige.bases.timer.event.TimerExpireEvent;
import me.prestige.bases.timer.event.TimerExtendEvent;
import me.prestige.bases.timer.event.TimerPauseEvent;
import me.prestige.bases.timer.event.TimerStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public abstract class GlobalTimer extends Timer {
    private TimerRunnable runnable;
    private Long expectedExpire;

    public GlobalTimer(final String name, final long defaultCooldown) {
        super(name, defaultCooldown);
    }

    public boolean clearCooldown() {
        if(this.runnable != null) {
            this.runnable.cancel();
            this.runnable = null;
            return true;
        }
        return false;
    }
    public void onExpire() {
        this.expectedExpire = (long) 0;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerExpireLoadReduce(final TimerExpireEvent event) {
        if(event.getTimer().equals(this)) {
                this.onExpire();
        }
    }
    public boolean isPaused() {
        return this.runnable != null && this.runnable.isPaused();
    }

    public void setPaused(final boolean paused) {
        if(this.runnable != null && this.runnable.isPaused() != paused) {
            final TimerPauseEvent event = new TimerPauseEvent(this, paused);
            Bukkit.getPluginManager().callEvent((Event) event);
            if(!event.isCancelled()) {
                this.runnable.setPaused(paused);
            }
        }
    }

    public long getRemaining() {
        return (this.runnable == null) ? 0L : this.runnable.getRemaining();
    }

    public boolean setRemaining() {
        return this.setRemaining(this.defaultCooldown, true);
    }

    public boolean setRemaining(final long duration, final boolean overwrite) {
        boolean hadCooldown = false;
        if(this.runnable != null) {
            if(!overwrite) {
                return false;
            }
            final TimerExtendEvent event = new TimerExtendEvent(this, this.runnable.getRemaining(), duration);
            Bukkit.getPluginManager().callEvent((Event) event);
            if(event.isCancelled()) {
                return false;
            }
            hadCooldown = (this.runnable.getRemaining() > 0L);
            this.runnable.setRemaining(duration);
        } else {
            Bukkit.getPluginManager().callEvent((Event) new TimerStartEvent(this, duration));
            this.runnable = new TimerRunnable(this, duration);
        }
        this.expectedExpire = System.currentTimeMillis() + duration;
        return !hadCooldown;
    }

    public Long getExpectedExpire() {
        return expectedExpire;
    }

    public void setExpectedExpire(Long expectedExpire) {
    this.expectedExpire = expectedExpire;
        setRemaining(this.expectedExpire - System.currentTimeMillis(), true);
    }
}
