package org.ipvp.hcf.timer.type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.pvpclass.archer.ArcherClass;
import org.ipvp.hcf.timer.PlayerTimer;
import org.ipvp.hcf.timer.event.TimerExpireEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
public class ArcherTimer extends PlayerTimer implements Listener {
    @Override
    public String getScoreboardPrefix() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD;
    }
    private final HCF plugin;

    private final Double ARCHER_DAMAGE;

    public ArcherTimer(final HCF plugin) {
        super("Archer Tag", TimeUnit.SECONDS.toMillis(6L));
        this.plugin = plugin;
        ARCHER_DAMAGE = .15;
    }


    @EventHandler
    public void onExpire(TimerExpireEvent e){
        if(e.getUserUUID().isPresent()){
            if(e.getTimer().equals(this)){
                UUID userUUID = e.getUserUUID().get();
                final Player player = Bukkit.getPlayer(userUUID);
                if (player == null) {
                    return;
                }
                ArcherClass.TAGGED.remove(player.getUniqueId());
                for(Player players : Bukkit.getServer().getOnlinePlayers()){
                    plugin.getScoreboardHandler().getPlayerBoard(players.getUniqueId()).addUpdates(Bukkit.getOnlinePlayers());
                }
            }
        }
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        Player entity;
        Entity damager;
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            entity = (Player) e.getEntity();
            damager = (Player) e.getDamager();
            if(this.getRemaining(entity) > 0){
                Double damage = e.getDamage() * ARCHER_DAMAGE;
                e.setDamage(e.getDamage() + damage);
            }
        }
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow){
            entity = (Player) e.getEntity();
            damager = (Player) ((Arrow) e.getDamager()).getShooter();
            if(damager instanceof Player) {
                if (this.getRemaining(entity) > 0) {
                    if(ArcherClass.TAGGED.get(entity.getUniqueId()).equals(damager.getUniqueId())){
                        this.setCooldown(entity, entity.getUniqueId());
                    }
                    Double damage = e.getDamage() * ARCHER_DAMAGE;
                    e.setDamage(e.getDamage() + damage);
                }
            }
        }
    }


}