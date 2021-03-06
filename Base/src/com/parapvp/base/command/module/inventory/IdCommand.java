/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.parapvp.base.command.module.inventory;

import com.parapvp.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class IdCommand
extends BaseCommand {
    public IdCommand() {
        super("id", "Checks the ID/name of an item.");
        this.setUsage("/(command) [itemName]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (p.getInventory().getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
                p.sendMessage((Object)ChatColor.YELLOW + "The ID of: " + p.getItemInHand().getType().toString().replace("_", "").toLowerCase() + " is " + p.getItemInHand().getTypeId());
                return true;
            }
            p.sendMessage((Object)ChatColor.RED + "ERROR: The item is not found or is air. Try putting something in your hand.");
            return true;
        }
        sender.sendMessage((Object)ChatColor.RED + "You must be a player to execute this.");
        return true;
    }
}

