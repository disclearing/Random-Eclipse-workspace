package server.wenjapvp.hcf.faction.event;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import server.wenjapvp.hcf.faction.FactionMember;
import server.wenjapvp.hcf.faction.struct.ChatChannel;
import server.wenjapvp.hcf.faction.type.PlayerFaction;

import java.util.Collection;

/**
 * Faction event called when a {@link Player} in a faction talks to their relations.
 */
public class FactionChatEvent extends FactionEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final Player player;
    private final FactionMember factionMember;
    private final ChatChannel chatChannel;
    private final String message;
    private final Collection<? extends CommandSender> recipients;
    private final String fallbackFormat;

    public FactionChatEvent(boolean async, PlayerFaction faction, Player player, ChatChannel chatChannel, Collection<? extends CommandSender> recipients, String message) {
        super(faction, async);
        this.player = player;
        this.factionMember = faction.getMember(player.getUniqueId());
        this.chatChannel = chatChannel;
        this.recipients = recipients;
        this.message = message;
        this.fallbackFormat = chatChannel.getRawFormat(player);
    }

    public Player getPlayer() {
        return player;
    }

    public FactionMember getFactionMember() {
        return factionMember;
    }

    public ChatChannel getChatChannel() {
        return chatChannel;
    }

    public Collection<? extends CommandSender> getRecipients() {
        return recipients;
    }

    public String getMessage() {
        return message;
    }

    public String getFallbackFormat() {
        return fallbackFormat;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}