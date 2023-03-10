package xyz.oribuin.eternalclaims.claim;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.claim.permission.PermissionHolder;
import xyz.oribuin.eternalclaims.claim.permission.PermissionType;
import xyz.oribuin.eternalclaims.claim.setting.SettingType;
import xyz.oribuin.eternalclaims.manager.ConfigurationManager.Setting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Claim {

    private final int id; // The ID of the claim
    private final UUID owner; // The UUID of the owner
    private @Nullable Member cachedOwner; // The cached owner
    private int chunkX; // The X coordinate of the chunk
    private int chunkZ; // The Z coordinate of the chunk
    private @NotNull World world; // The world the claim is in
    private @NotNull Map<PermissionType, PermissionHolder> permissions; // The permissions the member has
    private @NotNull Map<SettingType, Boolean> settings; // The settings of the claim

    public Claim(int id, UUID owner) {
        this.id = id;
        this.owner = owner;
        this.cachedOwner = null;
        this.chunkX = 0;
        this.chunkZ = 0;
        this.world = null;
        this.permissions = new HashMap<>();
        this.settings = new HashMap<>();
    }

    // TODO: canUse(Player player, PermissionType type)
    // TODO: canEnter(Player uuid)

    /**
     * @return The chunk the claim is in
     */
    @NotNull
    public Chunk getChunk() {
        return this.world.getChunkAt(this.chunkX, this.chunkZ);
    }

    public int getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Map<PermissionType, PermissionHolder> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<PermissionType, PermissionHolder> permissions) {
        this.permissions = permissions;
    }

}
