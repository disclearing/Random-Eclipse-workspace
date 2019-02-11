package me.prestige.bases.faction.argument.staff;

import com.customhcf.util.command.CommandArgument;
import com.google.common.primitives.Doubles;
import me.prestige.bases.Bases;
import me.prestige.bases.faction.type.Faction;
import me.prestige.bases.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactionSetDtrArgument extends CommandArgument {
    private final Bases plugin;

    public FactionSetDtrArgument(final Bases plugin) {
        super("setdtr", "Sets the DTR of a faction.");
        this.plugin = plugin;
        this.permission = "command.faction." + this.getName();
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <playerName|factionName> <newDtr>";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Double newDTR = Doubles.tryParse(args[2]);
        if(newDTR == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
            return true;
        }
        if(args[1].equalsIgnoreCase("all")) {
            for(final Faction faction : this.plugin.getFactionManager().getFactions()) {
                if(faction instanceof PlayerFaction) {
                    ((PlayerFaction) faction).setDeathsUntilRaidable(newDTR);
                }
            }
            Command.broadcastCommandMessage(sender, ChatColor.WHITE + "Set DTR of all factions to " + newDTR + '.');
            return true;
        }
        final Faction faction2 = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if(faction2 == null) {
            sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        if(!(faction2 instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "You can only set DTR of player factions.");
            return true;
        }
        final PlayerFaction playerFaction = (PlayerFaction) faction2;
        final double previousDtr = playerFaction.getDeathsUntilRaidable();
        newDTR = playerFaction.setDeathsUntilRaidable(newDTR);
        Command.broadcastCommandMessage(sender, ChatColor.WHITE + "Set DTR of " + faction2.getName() + " from " + previousDtr + " to " + newDTR + '.');
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        if(args[1].isEmpty()) {
            return null;
        }
        final Player player = (Player) sender;
        final List<String> results = new ArrayList<String>(this.plugin.getFactionManager().getFactionNameMap().keySet());
        for(final Player target : Bukkit.getOnlinePlayers()) {
            if(player.canSee(target) && !results.contains(target.getName())) {
                results.add(target.getName());
            }
        }
        return results;
    }
}
