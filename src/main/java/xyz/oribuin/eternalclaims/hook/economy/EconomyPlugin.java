package xyz.oribuin.eternalclaims.hook.economy;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface EconomyPlugin {

    /**
     * @param player The player
     * @return The balance of the player
     */
    double getBalance(@NotNull UUID player);

    /**
     * @param player The player
     * @param amount The amount to withdraw
     */
    void withdraw(@NotNull UUID player, double amount);

    /**
     * @param player The player
     * @param amount The amount to deposit
     */
    void deposit(@NotNull UUID player, double amount);

    /**
     * @param player The player
     * @param amount The amount to check
     * @return If the player has the amount
     */
    boolean has(@NotNull UUID player, double amount);

}
