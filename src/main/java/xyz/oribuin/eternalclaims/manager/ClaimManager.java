package xyz.oribuin.eternalclaims.manager;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.storage.ClaimDataKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ClaimManager extends Manager {

    public static final Gson GSON = new Gson();

    private final Map<UUID, Claim> cachedClaims = new HashMap<>();
    private final Set<UUID> bypassing = Sets.newConcurrentHashSet();

    public ClaimManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.cachedClaims.values().forEach(Claim::save);
        this.cachedClaims.clear();
    }

    @Override
    public void disable() {
        this.cachedClaims.values().forEach(Claim::save);
    }

    /**
     * Get a claim from a chunk
     *
     * @param chunk The chunk to get the claim from
     * @return The claim
     */
    @Nullable
    public Claim getClaim(@NotNull Chunk chunk) {

        // Check if the chunk is claimed
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (!container.has(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING))
            return null;

        String claimId = container.get(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING);

        // Get the claim from the cache
        if (claimId != null && this.cachedClaims.containsKey(UUID.fromString(claimId))) {
            return this.cachedClaims.get(UUID.fromString(claimId));
        }

        return null;
    }

    /**
     * Get a claim from an ID
     *
     * @param id The ID of the claim
     * @return The claim
     */
    @Nullable
    public Claim getClaim(@NotNull UUID id) {
        return this.cachedClaims.get(id);
    }

    /**
     * Get a claim from a location
     *
     * @param location The location to get the claim from
     * @return The claim
     */
    @Nullable
    public Claim getClaim(@NotNull Location location) {
        return this.getClaim(location.getChunk());
    }

    /**
     * Save a claim to a chunk
     *
     * @param claim The chunk to save the claim to
     * @return If the claim was saved
     */
    public boolean save(@NotNull Claim claim) {
        return claim.save() && this.cachedClaims.put(claim.getId(), claim) != null;
    }

    /**
     * Delete a claim
     *
     * @param claim The claim to delete
     */
    public void delete(@NotNull Claim claim) {
        this.cachedClaims.remove(claim.getId());

        claim.delete();
    }

    /**
     * Check if a chunk is claimed
     *
     * @param chunk The chunk to check
     * @return If the chunk is claimed
     */
    public boolean isClaimed(@NotNull Chunk chunk) {
        return chunk.getPersistentDataContainer().has(ClaimDataKeys.CLAIM_ID, PersistentDataType.STRING);
    }

    /**
     * Check if a player is bypassing
     *
     * @param uuid The UUID of the player
     * @return If the player is bypassing
     */
    public boolean isBypassing(@NotNull UUID uuid) {
        return this.bypassing.contains(uuid);
    }

    /**
     * Toggle bypassing for a player
     *
     * @param uuid The UUID of the player
     * @return If the player is bypassing
     */
    public boolean toggleBypassing(@NotNull UUID uuid) {
        if (!this.bypassing.remove(uuid)) {
            return this.bypassing.add(uuid);
        }

        return false;
    }

    public Map<UUID, Claim> getCachedClaims() {
        return cachedClaims;
    }

}
