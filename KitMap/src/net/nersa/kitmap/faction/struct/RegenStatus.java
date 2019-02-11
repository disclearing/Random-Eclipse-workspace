package net.nersa.kitmap.faction.struct;

import net.nersa.kitmap.faction.type.Faction;

import org.bukkit.ChatColor;

/**
 * Represents the {@link RegenStatus} of a {@link Faction}.
 */
public enum RegenStatus {

	FULL(ChatColor.GREEN.toString() + '\u25B6'), REGENERATING(ChatColor.GOLD.toString() + '\u21ea'), PAUSED(ChatColor.RED.toString() + '\u25a0');

	private final String symbol;

	RegenStatus(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Gets the symbol of this {@link RegenStatus}.
	 *
	 * @return the regeneration status
	 */
	public String getSymbol() {
		return symbol;
	}
}
