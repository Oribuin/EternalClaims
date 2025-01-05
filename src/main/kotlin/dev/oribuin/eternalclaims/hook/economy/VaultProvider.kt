package dev.oribuin.eternalclaims.hook.economy

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class VaultProvider : EconomyPlugin {

    private var api: Economy = Bukkit.getServicesManager().getRegistration(Economy::class.java)!!.provider

    /**
     * Check if the economy plugin is enabled
     *
     * @return If the economy plugin is enabled
     */
    override fun enabled(): Boolean {
        return Bukkit.getPluginManager().isPluginEnabled("Vault")
    }

    /**
     * Take money from a player
     *
     * @param player The player
     * @param amount The amount
     * @return If the transaction was successful
     */
    override fun take(player: OfflinePlayer, amount: Double): Boolean {
        if (!this.enabled()) return false

        return api.withdrawPlayer(player, amount).transactionSuccess()
    }

    /**
     * Give a player money
     *
     * @param player The player
     * @param amount The amount
     * @return If the transaction was successful
     */
    override fun give(player: OfflinePlayer, amount: Double): Boolean {
        if (!this.enabled()) return false

        return api.depositPlayer(player, amount).transactionSuccess()
    }

    /**
     * Get the balance of a player
     *
     * @param player The player
     * @return The balance
     */
    override fun balance(player: OfflinePlayer): Double {
        if (!this.enabled()) return 0.0

        return api.getBalance(player)
    }

    /**
     * Check if a player has a certain amount of money
     *
     * @param player The player
     * @param amount The amount
     * @return If the player has the amount
     */
    override fun has(player: OfflinePlayer, amount: Double): Boolean {
        if (!this.enabled()) return false

        return api.has(player, amount)
    }

    companion object {
        private var instance: VaultProvider = VaultProvider()
    }

}