/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.bizarrealex.aether.scoreboard;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;

public interface BoardAdapter {
    public String getTitle(Player var1);

    public List<String> getScoreboard(Player var1, Board var2, Set<BoardCooldown> var3);
}

