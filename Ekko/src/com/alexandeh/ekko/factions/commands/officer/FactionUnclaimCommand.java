package com.alexandeh.ekko.factions.commands.officer;

import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.commands.FactionCommand;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.LocationSerialization;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionUnclaimCommand extends FactionCommand {
    @Command(name = "f.unclaim", aliases = {"faction.unclaim", "factions.unclaim", "factions.unclaimall", "f.unclaimall", "faction.unclaimall"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        String[] args = command.getArgs();
        int argPos = 1;
        boolean skip = false;

        Faction faction;
        PlayerFaction playerFaction = null;
        if (command.getArgs().length >= 2 && player.hasPermission("ekko.admin")) {
            String name = command.getArgs(1);
            Faction faction1 = PlayerFaction.getAnyByString(name);
            if (faction1 != null) {
                if (faction1 instanceof PlayerFaction) {
                    playerFaction = (PlayerFaction) faction1;
                }
                faction = faction1;
                skip = true;
            } else {
                player.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_FOUND").replace("%NAME%", name));
                return;
            }
        } else {
            faction = profile.getFaction();
            playerFaction = profile.getFaction();

            if (faction == null) {
                Claim claim = Claim.getProminentClaimAt(player.getLocation());
                if (claim != null && player.hasPermission("ekko.admin")) {
                    faction = claim.getFaction();
                    playerFaction = null;
                } else {
                    player.sendMessage(langConfig.getString("ERROR.NOT_IN_FACTION"));
                    return;
                }
            }

            if (playerFaction != null && !playerFaction.getLeader().equals(player.getUniqueId()) && !playerFaction.getOfficers().contains(player.getUniqueId()) && !player.hasPermission("ekko.admin")) {
                player.sendMessage(langConfig.getString("ERROR.NOT_OFFICER_OR_LEADER"));
                return;
            }
        }

        if (command.getLabel().equalsIgnoreCase("f.unclaimall") || args.length >= argPos) {

            if (!command.getLabel().equalsIgnoreCase("f.unclaimall") && !skip && (args.length != argPos || !args[argPos - 1].equalsIgnoreCase("all"))) {
                player.sendMessage(langConfig.getString("INCORRECT_USAGE.UNCLAIM"));
                return;
            }

            if ((playerFaction != null && !playerFaction.getLeader().equals(player.getUniqueId())) && !player.hasPermission("ekko.admin")) {
                player.sendMessage(langConfig.getString("ERROR.NOT_LEADER"));
                return;
            }


            Set<Claim> claims = faction.getClaims();
            if (claims.isEmpty()) {
                player.sendMessage(langConfig.getString("ERROR.NO_CLAIMS"));
                return;
            }
            for (Claim claim : new ArrayList<>(claims)) {
                if (faction.getHome() != null && claim.isInside(LocationSerialization.deserializeLocation(faction.getHome()))) {
                    faction.setHome(null);
                }
                claim.remove();
            }

            String message = langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_UNCLAIM_ALL").replace("%PLAYER%", player.getName());

            if (playerFaction != null) {
                playerFaction.sendMessage(message);
                if (!(playerFaction.getOnlinePlayers().contains(player))) {
                    player.sendMessage(message);
                }
            } else {
                player.sendMessage(message);
            }

            return;
        }

        Location location = player.getLocation();
        List<Claim> claims = Claim.getClaimsAt(location);

        if (claims != null) {
            String message = langConfig.getString("ANNOUNCEMENTS.FACTION.PLAYER_UNCLAIM").replace("%PLAYER%", player.getName());
            for (Claim claim : new ArrayList<>(claims)) {
                if (claim.getFaction() == faction && claim.isInside(location)) {

                    if (faction.getHome() != null && claim.isInside(LocationSerialization.deserializeLocation(faction.getHome()))) {
                        faction.setHome(null);
                    }

                    claim.remove();

                    if (playerFaction != null) {
                        playerFaction.sendMessage(message);
                        if (!(playerFaction.getOnlinePlayers().contains(player))) {
                            player.sendMessage(message);
                        }
                    } else {
                        player.sendMessage(message);
                    }

                    return;
                }
            }
        }

        player.sendMessage(langConfig.getString("ERROR.MUST_BE_IN_LAND_TO_UNCLAIM"));
    }
}
