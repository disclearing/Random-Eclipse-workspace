package me.prestige.bases.faction.argument.staff;

import com.customhcf.util.command.CommandArgument;
import me.prestige.bases.Bases;
import me.prestige.bases.faction.type.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactionRemoveArgument extends CommandArgument {
    private final ConversationFactory factory;
    private final Bases plugin;

    public FactionRemoveArgument(final Bases plugin) {
        super("remove", "Remove a faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"delete", "forcedisband", "forceremove"};
        this.permission = "command.faction." + this.getName();
        this.factory = new ConversationFactory((Plugin) plugin).withFirstPrompt((Prompt) new RemoveAllPrompt(plugin)).withEscapeSequence("/no").withTimeout(10).withModality(false).withLocalEcho(true);
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <all|factionName>";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if(args[1].equalsIgnoreCase("all")) {
            if(!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "This command can be only executed from console.");
                return true;
            }
            final Conversable conversable = (Conversable) sender;
            conversable.beginConversation(this.factory.buildConversation(conversable));
            return true;
        } else {
            final Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
            if(faction == null) {
                sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
                return true;
            }
            if(this.plugin.getFactionManager().removeFaction(faction, sender)) {
                Command.broadcastCommandMessage(sender, ChatColor.WHITE + "Disbanded faction " + ChatColor.GOLD + faction.getName() + ChatColor.WHITE + '.');
            }
            return true;
        }
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

    private static class RemoveAllPrompt extends StringPrompt {
        private final Bases plugin;

        public RemoveAllPrompt(final Bases plugin) {
            this.plugin = plugin;
        }

        public String getPromptText(final ConversationContext context) {
            return ChatColor.WHITE + "Are you sure you want to do this? " + ChatColor.RED + ChatColor.BOLD + "All factions" + ChatColor.WHITE + " will be cleared. " + "Type " + ChatColor.GREEN + "yes" + ChatColor.WHITE + " to confirm or " + ChatColor.RED + "no" + ChatColor.WHITE + " to deny.";
        }

        public Prompt acceptInput(final ConversationContext context, final String string) {
            final String lowerCase = string.toLowerCase();
            switch(lowerCase) {
                case "yes": {
                    for(final Faction faction : this.plugin.getFactionManager().getPlayerFactions()) {
                        this.plugin.getFactionManager().removeFaction(faction, Bukkit.getConsoleSender());
                    }
                    final Conversable conversable = context.getForWhom();
                    Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "All factions have been disbanded" + ((conversable instanceof CommandSender) ? (" by " + ((CommandSender) conversable).getName()) : "") + '.');
                    return Prompt.END_OF_CONVERSATION;
                }
                case "no": {
                    context.getForWhom().sendRawMessage(ChatColor.BLUE + "Cancelled the process of disbanding all factions.");
                    return Prompt.END_OF_CONVERSATION;
                }
                default: {
                    context.getForWhom().sendRawMessage(ChatColor.RED + "Unrecognized response. Process of disbanding all factions cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }
            }
        }
    }
}
