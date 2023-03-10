package xyz.oribuin.eternalclaims.hook.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ProtectionHook {

    /**
     * Check if a player can place a block at a location
     *
     * @param player   The player
     * @param location The location
     * @return true if the player can place a block
     */
    boolean canPlace(@NotNull Player player, @NotNull Location location);

    /**
     * Check if a player can break a block at a location
     *
     * @param player   The player
     * @param location The location
     * @return true if the player can break a block
     */
    boolean canBreak(@NotNull Player player, @NotNull Location location);

    /**
     * Check if a player can interact with a block at a location
     *
     * @param player   The player
     * @param location The location
     * @return true if the player can interact with a block
     */
    boolean canInteract(@NotNull Player player, @NotNull Location location);

    /**
     * Check if a player can access a container at a location
     *
     * @param player   The player
     * @param location The location
     * @return true if the player can access a container
     */
    boolean canAccess(@NotNull Player player, @NotNull Location location);

}
