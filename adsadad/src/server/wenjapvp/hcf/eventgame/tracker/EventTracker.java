package server.wenjapvp.hcf.eventgame.tracker;

import org.bukkit.entity.Player;
import server.wenjapvp.hcf.eventgame.EventTimer;
import server.wenjapvp.hcf.eventgame.EventType;
import server.wenjapvp.hcf.eventgame.CaptureZone;
import server.wenjapvp.hcf.eventgame.faction.EventFaction;

/**
 * Tracker for handling event mini-games. NOTE: The methods here are called before they happen, so the onControlLoss method for example would still have its' {@link CaptureZone} player unchanged.
 */
@Deprecated
public interface EventTracker {

    EventType getEventType();

    /**
     * Handles ticking every 5 seconds
     *
     * @param eventTimer
     *            the timer
     * @param eventFaction
     *            the faction
     */
    void tick(EventTimer eventTimer, EventFaction eventFaction);

    void onContest(EventFaction eventFaction, EventTimer eventTimer);

    boolean onControlTake(Player player, CaptureZone captureZone);

    void onControlLoss(Player player, CaptureZone captureZone, EventFaction eventFaction);

    void stopTiming();
}
