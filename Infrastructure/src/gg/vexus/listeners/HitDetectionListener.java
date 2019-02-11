package gg.vexus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import gg.vexus.Core;

public class HitDetectionListener implements Listener {

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, Core.getCore());
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setMaximumNoDamageTicks(19);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMaximumNoDamageTicks(19);
    }
}