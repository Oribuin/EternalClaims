package xyz.oribuin.eternalclaims.hook.economy;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerPointsHook implements EconomyPlugin {

    private final PlayerPointsAPI api;

    public PlayerPointsHook() {
        this.api = PlayerPoints.getInstance().getAPI();
    }

    @Override
    public double getBalance(@NotNull UUID player) {
        return this.api.look(player);
    }

    @Override
    public void withdraw(@NotNull UUID player, double amount) {
        this.api.take(player, (int) amount);
    }

    @Override
    public void deposit(@NotNull UUID player, double amount) {
        this.api.give(player, (int) amount);
    }

    @Override
    public boolean has(@NotNull UUID player, double amount) {
        return this.api.look(player) >= amount;
    }

}
