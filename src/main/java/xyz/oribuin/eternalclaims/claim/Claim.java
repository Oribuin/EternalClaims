package xyz.oribuin.eternalclaims.claim;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.manager.ClaimManager;
import xyz.oribuin.eternalclaims.storage.ClaimDataKeys;
import xyz.oribuin.eternalclaims.storage.ClaimSettingHolder;
import xyz.oribuin.eternalclaims.storage.ClaimTrustedHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class Claim {

    private UUID id; // The ID of the claim
    private UUID owner; // The UUID of the owner
    private String worldName; // The name of the world
    private int chunkX; // The X coordinate of the chunk
    private int chunkZ; // The Z coordinate of the chunk
    private Map<ClaimSetting, Boolean> settings; // The settings of the claim
    private List<UUID> trusted; // The trusted players of the claim

    public Claim(UUID owner, int chunkX, int chunkZ, @NotNull World world) {
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.worldName = world.getName();
        this.settings = new HashMap<>();
        this.trusted = new ArrayList<>();
    }

    public Claim(@NotNull UUID owner, @NotNull Chunk chunk) {
        this(owner, chunk.getX(), chunk.getZ(), chunk.getWorld());

        this.load(); // Load the claim from the chunk
    }

    /**
     * Create the claim and save it to the chunk
     *
     * @return Whether the claim was created
     */
    public boolean create() {
        Chunk chunk = this.getChunk();
        if (chunk == null) return false; // Could not create the chunk.

        PersistentDataContainer container = chunk.getPersistentDataContainer();

        // The chunk already has a claim.
        if (container.has(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING)) {
            return false;
        }

        // Set the claim id when the chunk is created.
        container.set(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING, this.id.toString());

        return this.save(chunk);
    }

    public void load() {
        Chunk chunk = this.getChunk();
        if (chunk == null) return; // Could not create the chunk.

        PersistentDataContainer container = chunk.getPersistentDataContainer();

        // The chunk already has a claim.
        if (!container.has(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING)) return;

        String claimId = container.get(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING);
        String claimOwner = container.get(ClaimDataKeys.CLAIM_OWNER, PersistentDataType.STRING);
        String claimSettings = container.get(ClaimDataKeys.SETTINGS, PersistentDataType.STRING);
        String claimTrusted = container.get(ClaimDataKeys.TRUSTED, PersistentDataType.STRING);

        if (claimId == null || claimOwner == null)
            return;

        this.id = UUID.fromString(claimId);
        this.owner = UUID.fromString(claimOwner);
        this.worldName = chunk.getWorld().getName();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();

        // Load the settings and trusted players
        if (claimSettings != null) {
            this.settings = ClaimManager.GSON.fromJson(claimSettings, ClaimSettingHolder.class).settings();
        }

        // Load the trusted players
        if (claimTrusted != null) {
            this.trusted = ClaimManager.GSON.fromJson(claimTrusted, ClaimTrustedHolder.class).trusted();
        }

        this.save(chunk);

    }


    /**
     * Save the claim to the chunk
     *
     * @return Whether the claim was saved
     */
    public boolean save() {
        return this.save(this.getChunk());
    }

    /**
     * Save the claim to the chunk
     *
     * @param chunk The chunk to save the claim to
     * @return Whether the claim was saved
     */
    public boolean save(Chunk chunk) {

        // Don't save the claim if the chunk is not provided
        if (chunk == null) return false;

        PersistentDataContainer container = chunk.getPersistentDataContainer();
        container.set(ClaimDataKeys.CLAIM_OWNER, PersistentDataType.STRING, this.owner.toString());
        container.set(ClaimDataKeys.SETTINGS, PersistentDataType.STRING, ClaimManager.GSON.toJson(new ClaimSettingHolder(this.settings)));
        container.set(ClaimDataKeys.TRUSTED, PersistentDataType.STRING, ClaimManager.GSON.toJson(new ClaimTrustedHolder(this.trusted)));

        // Save the chunk in the cache
        EternalClaims.getInstance()
                .getManager(ClaimManager.class)
                .getCachedClaims()
                .put(this.id, this);

        return true;
    }

    /**
     * Delete the claim from the chunk
     */
    public void delete() {
        Chunk chunk = this.getChunk();
        if (chunk == null) return; // Could not create the chunk.

        PersistentDataContainer container = chunk.getPersistentDataContainer();

        // The chunk already has a claim.
        if (!container.has(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING)) {
            return;
        }

        container.remove(ClaimDataKeys.CLAIM_ID);
        container.remove(ClaimDataKeys.CLAIM_OWNER);
        container.remove(ClaimDataKeys.SETTINGS);
        container.remove(ClaimDataKeys.TRUSTED);

        // Remove the claim from the cache
        EternalClaims.getInstance()
                .getManager(ClaimManager.class)
                .getCachedClaims()
                .remove(this.id);
    }

    /**
     * Check if a player is trusted in the claim
     *
     * @param player The player to check
     * @return Whether the player is trusted
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Shut up intellij
    public boolean isTrusted(@NotNull Player player) {
        if (this.owner.equals(player.getUniqueId()))
            return true;

        return this.trusted.contains(player.getUniqueId());
    }

    /**
     * Trust a player in the claim
     *
     * @param player The player to trust
     * @return Whether the player was trusted
     */
    public boolean trust(@NotNull Player player) {
        return this.trusted.add(player.getUniqueId());
    }

    /**
     * Untrust a player in the claim
     *
     * @param player The player to untrust
     * @return Whether the player was untrusted
     */
    public boolean untrust(@NotNull Player player) {
        return this.trusted.remove(player.getUniqueId());
    }

    /**
     * Check if a setting is enabled for the claim
     *
     * @param type The setting type to check
     * @return Whether the setting is enabled
     */
    public boolean checkSetting(@NotNull ClaimSetting type) {
        return this.settings.getOrDefault(type, type.isDefaultValue());
    }

    /**
     * Get the chunk the claim is in (This will return null if the chunk is not loaded)
     *
     * @return The chunk the claim is in
     */
    @Nullable
    public Chunk getChunk() {
        World world = Bukkit.getWorld(this.worldName);
        if (world == null)
            return null;

        return world.getChunkAt(this.chunkX, this.chunkZ);
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
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

    public Map<ClaimSetting, Boolean> getSettings() {
        return settings;
    }

    public void setSettings(Map<ClaimSetting, Boolean> settings) {
        this.settings = settings;
    }

    public List<UUID> getTrusted() {
        return trusted;
    }

    public void setTrusted(List<UUID> trusted) {
        this.trusted = trusted;
    }

}


