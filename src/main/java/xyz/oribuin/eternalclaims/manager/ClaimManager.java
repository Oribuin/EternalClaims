package xyz.oribuin.eternalclaims.manager;

import com.google.gson.Gson;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.claim.permission.ClaimPermission;
import xyz.oribuin.eternalclaims.claim.permission.PermissionHolder;
import xyz.oribuin.eternalclaims.claim.permission.PermissionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ClaimManager extends Manager {

    private final Gson gson = new Gson();

    public ClaimManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.rosePlugin.getManager(DataManager.class).loadDatabaseValues();

    }

    @Override
    public void disable() {

    }

    /**
     * Create a new claim and save it to the database
     *
     * @param owner    The owner of the claim
     * @param chunk    The chunk to claim
     * @param callback The callback to run after the claim is created
     */
    public void createClaim(UUID owner, Chunk chunk, Consumer<Claim> callback) {
        final DataManager dataManager = this.rosePlugin.getManager(DataManager.class);

        dataManager.createClaim(owner, chunk, claim -> {

            // Modify the chunk to be claimed
            PersistentDataContainer container = chunk.getPersistentDataContainer();
            container.set(new NamespacedKey(this.rosePlugin, "id"), PersistentDataType.INTEGER, claim.getId());
            container.set(new NamespacedKey(this.rosePlugin, "owner"), PersistentDataType.STRING, claim.getOwner().toString());
            container.set(new NamespacedKey(this.rosePlugin, "permissions"), PersistentDataType.STRING, this.fromPermissionMap(claim.getPermissions()));
            callback.accept(claim);
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
        container.remove(new NamespacedKey(this.rosePlugin, "id"));
        container.remove(new NamespacedKey(this.rosePlugin, "owner"));
        container.remove(new NamespacedKey(this.rosePlugin, "permissions"));
    }

    /**
     * Get the claim from a chunk
     *
     * @param chunk The chunk to get the claim from
     * @return The claim
     */
    public Claim getClaim(Chunk chunk) {
        final PersistentDataContainer container = chunk.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(this.rosePlugin, "id"), PersistentDataType.INTEGER))
            return null;

        final Integer id = container.get(new NamespacedKey(this.rosePlugin, "id"), PersistentDataType.INTEGER);
        final String owner = container.get(new NamespacedKey(this.rosePlugin, "owner"), PersistentDataType.STRING);
        final Map<PermissionType, PermissionHolder> permissions = this.toPermissionMap(container.get(new NamespacedKey(this.rosePlugin, "permissions"), PersistentDataType.STRING));

        if (id == null || owner == null || permissions == null)
            return null;

        Claim claim = new Claim(id, UUID.fromString(owner));
        claim.setChunkX(chunk.getX());
        claim.setChunkZ(chunk.getZ());
        claim.setPermissions(permissions);
        return claim;
    }

    /**
     * Get all claims from a player
     *
     * @param owner The owner of the claims
     * @return A list of claims
     */
    public List<Claim> getClaims(UUID owner) {
        return this.rosePlugin.getManager(DataManager.class).getClaims()
                .row(owner)
                .values()
                .stream()
                .toList();
    }

    /**
     * Get all claims
     *
     * @return A list of claims
     */
    public List<Claim> getClaims() {
        return this.rosePlugin.getManager(DataManager.class).getClaims()
                .values()
                .stream()
                .toList();
    }

    /**
     * Convert a string to a map of permissions
     *
     * @param data The string to convert
     * @return A map of permissions
     */
    public Map<PermissionType, PermissionHolder> toPermissionMap(String data) {
        if (data == null)
            return new HashMap<>();

        ClaimPermission claimPermission = this.gson.fromJson(data, ClaimPermission.class);
        return new HashMap<>(claimPermission.permissions());
    }

    /**
     * Convert a map of permissions to a string
     *
     * @param permissions The map of permissions to convert
     * @return A string of permissions
     */
    public String fromPermissionMap(Map<PermissionType, PermissionHolder> permissions) {
        return this.gson.toJson(new ClaimPermission(permissions));
    }

}
