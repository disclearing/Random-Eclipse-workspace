package us.kirai.bunkers.utils;

import com.sk89q.worldedit.bukkit.selections.*;

import us.kirai.bunkers.config.*;
import us.kirai.bunkers.game.*;

import org.bukkit.*;

public class RegionUtils
{
    public static CuboidSelection getSelectionFromRegion(final World w, final Team team) {
        final Location p1 = new Location(w, ConfigurationService.getRegions(team)[0].getX(), 0.0, ConfigurationService.getRegions(team)[0].getZ());
        final Location p2 = new Location(w, ConfigurationService.getRegions(team)[1].getX(), 255.0, ConfigurationService.getRegions(team)[1].getZ());
        final CuboidSelection sel = new CuboidSelection(w, p1, p2);
        return sel;
    }
}
