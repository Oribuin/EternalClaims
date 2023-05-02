package xyz.oribuin.eternalclaims.claim;

import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.claim.type.PermissionType;
import xyz.oribuin.eternalclaims.claim.type.SettingType;
import xyz.oribuin.eternalclaims.manager.ClaimManager;
import xyz.oribuin.eternalclaims.manager.ConfigurationManager.Setting;
import xyz.oribuin.eternalclaims.util.PluginUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Claim {

    private final int id; // The ID of the claim
    private final UUID owner; // The UUID of the owner
    private @Nullable Member cachedOwner; // The cached owner
    private int chunkX; // The X coordinate of the chunk
    private int chunkZ; // The Z coordinate of the chunk
    private Chunk chunk; // The chunk the claim is in
    private @NotNull World world; // The world the claim is in
    private @NotNull Map<SettingType, Boolean> settings; // The settings of the claim

    public Claim(int id, UUID owner, int chunkX, int chunkZ, @NotNull World world) {
        this.id = id;
        this.owner = owner;
        this.cachedOwner = null;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = world;
        this.settings = new HashMap<>();
    }

    public Claim(@NotNull Integer id, @NotNull UUID owner, @NotNull Chunk chunk) {
        this(id, owner, chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    // TODO: canUse(Player player, PermissionType type)
    // TODO: canEnter(Player uuid)

    /**
     * Check if a setting is enabled for the claim
     *
     * @param type The setting type to check
     * @return Whether the setting is enabled
     */
    public boolean checkSetting(@NotNull SettingType type) {
        return this.settings.get(type) != null && this.settings.get(type);
    }

    /**
     * Check whether a player can use a permission type
     *
     * @param player The player to check
     * @param type   The permission type to check
     * @return Whether the player can use the permission type
     */
    public boolean checkPermission(@NotNull Player player, @NotNull PermissionType type) {

        // Check if the player is the owner
        if (this.getOwner().equals(player.getUniqueId()))
            return true;

        // TODO: Add option to check if the player is bypassing the claim

        // Get the cached member
        Member member = this.cachedOwner != null
                ? this.cachedOwner
                : EternalClaims.getInstance().getManager(ClaimManager.class).getMember(this.getOwner());

        // Check if the member is null or the player is not trusted
        if (member == null || !member.getTrustedUsers().contains(player.getUniqueId()))
            return false;

        // Check if the member has the permission
        return member.getPermissions().get(type) != null && member.getPermissions().get(type);
    }

    /**
     * Get the default settings for a claim protection
     *
     * @return The default settings
     */
    public static Map<SettingType, Boolean> getDefaultSettings() {
        Map<SettingType, Boolean> settings = new HashMap<>();

        // Load the default settings
        CommentedConfigurationSection section = Setting.CLAIMS_SETTINGS_DEFAULT.getSection();
        if (section == null)
            return settings;

        for (String key : section.getKeys(false)) {
            SettingType type = PluginUtils.getEnum(SettingType.class, key);

            if (type != null)
                settings.put(type, section.getBoolean(key));
        }

        return settings;
    }

    /**
     * Get the chunk the claim is in  (This will return null if the chunk is not loaded)
     *
     * @param force Whether to force get the chunk
     * @return The chunk the claim is in
     */
    @Nullable
    public Chunk getChunk(boolean force) {
        if (force || this.chunk == null)
            this.chunk = this.world.getChunkAt(this.chunkX, this.chunkZ);

        return this.chunk;
    }

    public void setChunk(final Chunk chunk) {
        this.chunk = chunk;
    }

    public int getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public @Nullable Member getCachedOwner() {
        return cachedOwner;
    }

    public void setCachedOwner(final @Nullable Member cachedOwner) {
        this.cachedOwner = cachedOwner;
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

    public @NotNull World getWorld() {
        return world;
    }

    public void setWorld(@NotNull World world) {
        this.world = world;
    }

    public @NotNull Map<SettingType, Boolean> getSettings() {
        return settings;
    }

    public void setSettings(@NotNull Map<SettingType, Boolean> settings) {
        this.settings = settings;
    }


}
