package me.prestige.bases.faction.argument;


import com.customhcf.util.command.CommandArgument;
import me.prestige.bases.Bases;
import me.prestige.bases.faction.struct.ChatChannel;
import me.prestige.bases.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class FactionMessageArgument extends CommandArgument {
    private final Bases plugin;

    public FactionMessageArgument(final Bases plugin) {
        super("message", "Sends a message to your faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"msg"};
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <message>";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use faction chat.");
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
        String format = String.format(ChatChannel.FACTION.getRawFormat(player), "", org.apache.commons.lang.StringUtils.join((Object[]) args, (char) ' ', (int) 1, (int) args.length));
        Iterator<Player> iterator = playerFaction.getOnlinePlayers().iterator();
        while(iterator.hasNext()) {
            Player target = iterator.next();
            target.sendMessage(format);
        }
        return true;
    }
}
