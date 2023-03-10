package xyz.oribuin.eternalclaims.hook.protection;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldGuardHook implements ProtectionHook {

    private final WorldGuard worldGuard = WorldGuard.getInstance();

    @Override
    public boolean canPlace(@NotNull Player player, @NotNull Location location) {
        return this.checkFlag(player, location, Flags.BLOCK_PLACE);
    }

    @Override
    public boolean canBreak(@NotNull Player player, @NotNull Location location) {
        return this.checkFlag(player, location, Flags.BLOCK_BREAK);
    }

    @Override
    public boolean canInteract(@NotNull Player player, @NotNull Location location) {
        return this.checkFlag(player, location, Flags.INTERACT);
    }

    @Override
    public boolean canAccess(@NotNull Player player, @NotNull Location location) {
        return this.checkFlag(player, location, Flags.CHEST_ACCESS);
    }

    /**
     * A method to check if a player can perform an action at a location, without having to copy and paste the same code.
     *
     * @param player   The player
     * @param location The location
     * @param flags    The flags to check
     * @return true if the player can perform the action
     */
    private boolean checkFlag(@NotNull Player player, @NotNull Location location, StateFlag... flags) {
        if (location.getWorld() == null)
            return false;

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        World world = BukkitAdapter.adapt(location.getWorld());

        if (worldGuard.getPlatform().getSessionManager().hasBypass(localPlayer, world))
            return true;

        RegionQuery query = worldGuard.getPlatform().getRegionContainer().createQuery();
        return query.testState(BukkitAdapter.adapt(location), localPlayer, flags);
    }

}
