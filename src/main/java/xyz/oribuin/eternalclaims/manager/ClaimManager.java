package xyz.oribuin.eternalclaims.manager;

import com.google.gson.Gson;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.claim.Member;
import xyz.oribuin.eternalclaims.storage.ClaimNamespace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ClaimManager extends Manager {

    private final Gson gson = new Gson();

    public ClaimManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        // Nothing to reload
    }

    @Override
    public void disable() {
        // Nothing to disable
    }

    /**
     * Create a new claim and save it to the database
     *
     * @param owner    The owner of the claim
     * @param chunk    The chunk to claim
     * @param callback The callback to run after the claim is created
     */
    public void createClaims(@NotNull UUID owner, @NotNull List<Chunk> chunks, @NotNull Consumer<List<Claim>> callback) {
        final DataManager dataManager = this.rosePlugin.getManager(DataManager.class);

        dataManager.createClaims(owner, chunks, claims -> {

            // Modify the chunk to be claimed
            for (Claim claim : claims) {
                Chunk chunk = claim.getChunk(false); // We shouldn't need to load the chunk as it should already be loaded
                if (chunk == null)
                    continue;

                chunk.getPersistentDataContainer().set(ClaimNamespace.CLAIM_ID, PersistentDataType.INTEGER, claim.getId());
            }
            callback.accept(claims);

        });
    }

    /**
     * Delete a claim and remove it from the database
     *
     * @param claim The claim to delete
     */
    public void deleteClaim(Claim claim) {
        final DataManager dataManager = this.rosePlugin.getManager(DataManager.class);
        final World world = claim.getWorld();
        final Chunk chunk = world.getChunkAt(claim.getChunkX(), claim.getChunkZ());

        dataManager.deleteClaim(claim.getOwner(), claim.getId());

        // Modify the chunk to be unclaimed
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        container.remove(ClaimNamespace.CLAIM_ID);
    }

    /**
     * Get the claim from a chunk
     *
     * @param chunk The chunk to get the claim from
     * @return The claim
     */
    @Nullable
    public Claim getClaim(@NotNull Chunk chunk) {
        final PersistentDataContainer container = chunk.getPersistentDataContainer();
        final int id = container.getOrDefault(ClaimNamespace.CLAIM_ID, PersistentDataType.INTEGER, -1);
        if (id == -1)
            return null;

        return this.rosePlugin.getManager(DataManager.class).getClaims().get(id);
    }

    /**
     * Check if a chunk is claimed
     *
     * @param chunk The chunk to check
     * @return If the chunk is claimed
     */
    public boolean isClaimed(@NotNull Chunk chunk) {
        return chunk.getPersistentDataContainer().has(ClaimNamespace.CLAIM_ID, PersistentDataType.INTEGER);
    }

    /**
     * Get all claims from a world
     *
     * @param world The world to get the claims from
     * @return A list of claims
     */
    @NotNull
    public List<Claim> getClaims(@NotNull World world) {
        List<Claim> result = new ArrayList<>();

        for (Claim claim : this.rosePlugin.getManager(DataManager.class).getClaims().values()) {
            if (claim.getWorld().equals(world))
                result.add(claim);
        }

        return result;
    }

    /**
     * Get a claim by its id
     *
     * @param id The id of the claim
     * @return The claim
     */
    @Nullable
    public Claim getClaimById(@NotNull Integer id) {
        return this.rosePlugin.getManager(DataManager.class).getClaims().get(id);
    }

    /**
     * Get a claim by the chunkX, ChunkZ and world of the claim
     *
     * @param chunkX The chunkX of the claim
     * @param chunkZ The chunkZ of the claim
     * @param world  The world of the claim
     * @return The claim
     */
    @Nullable
    public Claim getClaimByChunk(int chunkX, int chunkZ, @NotNull World world) {
        List<Claim> claims = this.getClaims(world);

        for (Claim claim : claims) {
            if (claim.getChunkX() == chunkX && claim.getChunkZ() == chunkZ)
                return claim;
        }

        return null;
    }

    /**
     * Check whether a player has a claim in a world
     *
     * @param owner The owner
     * @param world The world
     * @return Whether the player has a claim in the world
     */
    public boolean hasClaim(@NotNull UUID owner, @Nullable World world) {
        return this.getClaims(owner, world).size() > 0;
    }

    /**
     * Get all claims from a player in a world
     *
     * @param owner The owner of the claims
     * @param world The world to get the claims from
     * @return A list of claims
     */
    @NotNull
    public List<Claim> getClaims(@NotNull UUID owner, @Nullable World world) {
        List<Claim> results = new ArrayList<>(
                this.rosePlugin.getManager(DataManager.class).getClaims().values()
                        .stream()
                        .filter(claim -> claim.getOwner().equals(owner))
                        .toList()
        );

        if (world != null)
            results.removeIf(claim -> claim.getWorld() != world);

        return results;
    }

    /**
     * Get a member by their uuid
     *
     * @param uuid The uuid of the member
     * @return The member
     */
    public Member getMember(@NotNull UUID uuid) {
        return this.rosePlugin.getManager(DataManager.class).getMembers().getOrDefault(uuid, new Member(uuid));
    }


}
