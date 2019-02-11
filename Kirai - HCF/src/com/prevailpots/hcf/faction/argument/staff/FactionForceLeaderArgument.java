package com.prevailpots.hcf.faction.argument.staff;

import com.customhcf.util.command.CommandArgument;
import com.prevailpots.hcf.HCF;
import com.prevailpots.hcf.faction.FactionMember;
import com.prevailpots.hcf.faction.struct.Role;
import com.prevailpots.hcf.faction.type.PlayerFaction;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class FactionForceLeaderArgument extends CommandArgument {
    private final HCF plugin;

    public FactionForceLeaderArgument(final HCF plugin) {
        super("forceleader", "Forces the leader of a faction.");
        this.plugin = plugin;
        this.permission = "command.faction." + this.getName();
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getContainingPlayerFaction(args[1]);
        if(playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        final FactionMember factionMember = playerFaction.getMember(args[1]);
        if(factionMember == null) {
            sender.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if(factionMember.getRole() == Role.LEADER) {
            sender.sendMessage(ChatColor.RED + factionMember.getName() + " is already the leader of " + playerFaction.getDisplayName(sender) + ChatColor.RED + '.');
            return true;
        }
        final FactionMember leader = playerFaction.getLeader();
        final String oldLeaderName = (leader == null) ? "none" : leader.getName();
        final String newLeaderName = factionMember.getName();
        if(leader != null) {
            leader.setRole(Role.CAPTAIN);
        }
        factionMember.setRole(Role.LEADER);
        playerFaction.broadcast(ChatColor.YELLOW + sender.getName() + " has forcefully set the leader to " + newLeaderName + '.');
        sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Leader of " + playerFaction.getName() + "was forcefully set from " + oldLeaderName + " to " + newLeaderName + '.');
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 2) ? null : Collections.emptyList();
    }
}