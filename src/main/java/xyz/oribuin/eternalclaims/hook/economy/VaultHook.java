package xyz.oribuin.eternalclaims.hook.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VaultHook implements EconomyPlugin {

    private final Economy economy;

    public VaultHook() {
        this.economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    }
    @Override
    public double getBalance(@NotNull UUID player) {
        return this.economy.getBalance(Bukkit.getOfflinePlayer(player));
    }

    @Override
    public void withdraw(@NotNull UUID player, double amount) {
        this.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player), amount);
    }

    @Override
    public void deposit(@NotNull UUID player, double amount) {
        this.economy.depositPlayer(Bukkit.getOfflinePlayer(player), amount);
    }

    @Override
    public boolean has(@NotNull UUID player, double amount) {
        return this.economy.has(Bukkit.getOfflinePlayer(player), amount);
    }

}
