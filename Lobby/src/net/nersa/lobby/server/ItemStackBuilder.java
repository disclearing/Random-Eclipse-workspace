package net.nersa.lobby.server;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackBuilder {
    public static ItemStack get(Material material, int amount, short datavalue, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, datavalue);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        itemmeta.setLore(lore);
        item.setItemMeta(itemmeta);
        return item;
    }
}
