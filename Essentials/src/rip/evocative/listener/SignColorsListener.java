package rip.evocative.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import rip.evocative.Essentials;

public class SignColorsListener implements Listener
{
	@EventHandler
	public void onSign(SignChangeEvent e)
	{
		if (Essentials.config.getBoolean("SIGNCOLOR.MODULE"))
		{
			if (e.getPlayer().hasPermission("essentials.signcolors.admin"))
			{
				e.setLine(0, ChatColor.translateAlternateColorCodes('&', e.getLine(0)));
				e.setLine(1, ChatColor.translateAlternateColorCodes('&', e.getLine(1)));
				e.setLine(2, ChatColor.translateAlternateColorCodes('&', e.getLine(2)));
				e.setLine(3, ChatColor.translateAlternateColorCodes('&', e.getLine(3)));
			}
		}
	}
}
