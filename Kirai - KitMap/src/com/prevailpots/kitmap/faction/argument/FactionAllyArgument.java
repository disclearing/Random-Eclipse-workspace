package com.prevailpots.kitmap.faction.argument;

import com.customhcf.util.command.CommandArgument;
import com.prevailpots.kitmap.ConfigurationService;
import com.prevailpots.kitmap.HCF;
import com.prevailpots.kitmap.faction.event.FactionRelationCreateEvent;
import com.prevailpots.kitmap.faction.struct.Relation;
import com.prevailpots.kitmap.faction.struct.Role;
import com.prevailpots.kitmap.faction.type.Faction;
import com.prevailpots.kitmap.faction.type.PlayerFaction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FactionAllyArgument extends CommandArgument {
    private static final Relation RELATION;

    static {
        RELATION = Relation.ALLY;
    }

    private final HCF plugin;

    public FactionAllyArgument(final HCF plugin) {
        super("ally", "Send an alliance request to an faction.", new String[]{"alliance"});
        this.plugin = plugin;
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <factionName>";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Player player = (Player) sender;
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
        if(playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }
        if(playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be an officer to make relation wishes.");
            return true;
        }
        final Faction containingFaction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if(!(containingFaction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        final PlayerFaction targetFaction = (PlayerFaction) containingFaction;
        if(playerFaction.equals(targetFaction)) {
            sender.sendMessage(ChatColor.RED + "You cannot send " + FactionAllyArgument.RELATION.getDisplayName() + ChatColor.RED + " requests to your own faction.");
            return true;
        }
        final Collection<UUID> allied = playerFaction.getAllied();
        if(allied.size() >= 1) { // ALLY LIMIT
            sender.sendMessage(ChatColor.RED + "Your faction cant have more allies than " + plugin.getHcfHandler().getAllyLimit() + '.');
            return true;
        }
        if(targetFaction.getAllied().size() >= plugin.getHcfHandler().getAllyLimit()) {
            sender.sendMessage(targetFaction.getDisplayName(sender) + ChatColor.RED + " has reached their maximum alliance limit, which is " + plugin.getHcfHandler().getAllyLimit() + '.');
            return true;
        }
        if(allied.contains(targetFaction.getUniqueID())) {
            sender.sendMessage(ChatColor.RED + "Your faction already is " + FactionAllyArgument.RELATION.getDisplayName() + 'd' + ChatColor.RED + " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.RED + '.');
            return true;
        }
        if(targetFaction.getRequestedRelations().remove(playerFaction.getUniqueID()) != null) {
            final FactionRelationCreateEvent event = new FactionRelationCreateEvent(playerFaction, targetFaction, FactionAllyArgument.RELATION);
            Bukkit.getPluginManager().callEvent((Event) event);
            targetFaction.getRelations().put(playerFaction.getUniqueID(), FactionAllyArgument.RELATION);
            targetFaction.broadcast(ChatColor.YELLOW + "Your faction is now " + FactionAllyArgument.RELATION.getDisplayName() + 'd' + ChatColor.YELLOW + " with " + playerFaction.getDisplayName(targetFaction) + ChatColor.YELLOW + '.');
            playerFaction.getRelations().put(targetFaction.getUniqueID(), FactionAllyArgument.RELATION);
            playerFaction.broadcast(ChatColor.YELLOW + "Your faction is now " + FactionAllyArgument.RELATION.getDisplayName() + 'd' + ChatColor.YELLOW + " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + '.');
            return true;
        }
        if(playerFaction.getRequestedRelations().putIfAbsent(targetFaction.getUniqueID(), FactionAllyArgument.RELATION) != null) {
            sender.sendMessage(ChatColor.YELLOW + "Your faction has already requested to " + FactionAllyArgument.RELATION.getDisplayName() + ChatColor.YELLOW + " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + '.');
            return true;
        }
        playerFaction.broadcast(targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + " were informed that you wish to be " + FactionAllyArgument.RELATION.getDisplayName() + ChatColor.YELLOW + '.');
        targetFaction.broadcast(playerFaction.getDisplayName(targetFaction) + ChatColor.YELLOW + " has sent a request to be " + FactionAllyArgument.RELATION.getDisplayName() + ChatColor.YELLOW + ". Use " + ConfigurationService.ALLY_COLOUR + "/faction " + this.getName() + ' ' + playerFaction.getName() + ChatColor.YELLOW + " to accept.");
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        final Player player = (Player) sender;
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
        if(playerFaction == null) {
            return Collections.emptyList();
        }
        final List<String> results = new ArrayList<String>();
        return results;
    }
}
