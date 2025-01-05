package dev.oribuin.eternalclaims.hook.economy

import org.bukkit.OfflinePlayer

interface EconomyPlugin {
    /**
     * Check if the economy plugin is enabled
     *
     * @return If the economy plugin is enabled
     */
    fun enabled(): Boolean

    /**
     * Take money from a player
     *
     * @param player The player
     * @param amount The amount
     * @return If the transaction was successful
     */
    fun take(player: OfflinePlayer, amount: Double): Boolean

    /**
     * Give a player money
     *
     * @param player The player
     * @param amount The amount
     * @return If the transaction was successful
     */
    fun give(player: OfflinePlayer, amount: Double): Boolean

    /**
     * Get the balance of a player
     *
     * @param player The player
     * @return The balance
     */
    fun balance(player: OfflinePlayer): Double

    /**
     * Check if a player has a certain amount of money
     *
     * @param player The player
     * @param amount The amount
     * @return If the player has the amount
     */
    fun has(player: OfflinePlayer, amount: Double): Boolean
}