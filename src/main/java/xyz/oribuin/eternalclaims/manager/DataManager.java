package xyz.oribuin.eternalclaims.manager;

import com.google.gson.Gson;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.claim.Member;
import xyz.oribuin.eternalclaims.claim.type.PermissionType;
import xyz.oribuin.eternalclaims.claim.type.SettingType;
import xyz.oribuin.eternalclaims.database.migrations._1_CreateTables;
import xyz.oribuin.eternalclaims.storage.serializer.EnumMapSerializer;
import xyz.oribuin.eternalclaims.storage.serializer.HashSetSerializer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class DataManager extends AbstractDataManager {

    private static final Gson GSON = new Gson();

    private final Map<Integer, Claim> claims = new HashMap<>();
    private final Map<UUID, Member> members = new HashMap<>();

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        super.reload();

        this.loadDatabaseValues(); // Load all the database values
    }

    /**
     * Load all the database values
     */
    public void loadDatabaseValues() {
        this.claims.clear();
        this.members.clear();

        this.async(task -> this.databaseConnector.connect(connection -> {

            // Load all the members and claims from the database
            final String selectMembers = "SELECT * FROM " + this.getTablePrefix() + "members;";
            try (PreparedStatement statement = connection.prepareStatement(selectMembers)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));

                    // Load the permissions and settings
                    Map<PermissionType, Boolean> permissions = new EnumMapSerializer<>(new HashMap<PermissionType, Boolean>())
                            .deserialize(resultSet.getString("permissions"))
                            .orElse(new HashMap<>()); // If the permissions are null, set it to an empty map

                    Set<UUID> trustedUsers = new HashSetSerializer(new HashSet<>())
                            .deserialize(resultSet.getString("trusted_users"))
                            .orElse(new HashSet<>()); // If the trusted users are null, set it to an empty set

                    Member member = new Member(uuid);
                    member.setTrustedUsers(trustedUsers);
                    member.setPermissions(permissions);
                    member.setUsername(resultSet.getString("name"));
                }
            }

            final String selectClaims = "SELECT * FROM " + this.getTablePrefix() + "claims;";
            try (PreparedStatement statement = connection.prepareStatement(selectClaims)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    UUID owner = UUID.fromString(resultSet.getString("owner"));
                    int chunkX = resultSet.getInt("chunk_x");
                    int chunkZ = resultSet.getInt("chunk_z");
                    World world = this.rosePlugin.getServer().getWorld(resultSet.getString("world"));

                    if (world == null) {
                        this.rosePlugin.getLogger().warning("World " + resultSet.getString("world") + " does not exist. Skipping claim " + id);
                        continue;
                    }

                    // Load the settings for the claim
                    Claim claim = new Claim(id, owner, chunkX, chunkZ, world);
                    claim.setChunkX(chunkX);
                    claim.setChunkZ(chunkZ);
                    claim.setWorld(world);
                    claim.setSettings(new EnumMapSerializer<>(new HashMap<SettingType, Boolean>())
                            .deserialize(resultSet.getString("settings"))
                            .orElse(new HashMap<>()));
                    claim.setCachedOwner(this.members.get(owner)); // Set the cached owner (if it exists)
                    this.claims.put(id, claim);
                }
            }
        }));
    }

    /**
     * Create a player claim at a chunk location
     *
     * @param owner    The owner of the claim
     * @param chunks   The chunks to create the claim at
     * @param callback The callback to run after the claim is created
     */
    public void createClaims(@NotNull UUID owner, @NotNull List<Chunk> chunks, Consumer<List<Claim>> callback) {
        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "INSERT INTO " + this.getTablePrefix() + "claims (owner, chunk_x, chunk_z, world) VALUES (?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (Chunk chunk : chunks) {
                    statement.setString(1, owner.toString());
                    statement.setInt(2, chunk.getX());
                    statement.setInt(3, chunk.getZ());
                    statement.setString(4, chunk.getWorld().getName());
                    statement.addBatch();
                }

                statement.executeBatch();

                ResultSet resultSet = statement.getGeneratedKeys();
                List<Claim> results = new ArrayList<>();

                for (Chunk chunk : chunks) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        Claim claim = new Claim(id, owner, chunk.getX(), chunk.getZ(), chunk.getWorld());
                        claim.setChunkX(chunk.getX());
                        claim.setChunkZ(chunk.getZ());
                        claim.setChunk(chunk); // Set the chunk after setting the chunk x and z
                        claim.setWorld(chunk.getWorld());
                        claim.setSettings(Claim.getDefaultSettings());
                        results.add(claim);
                    }
                }

                callback.accept(results);
            }
        }));
    }

    /**
     * Update a claim in the database
     *
     * @param claim The claim to update
     */
    public void saveClaim(@NotNull Claim claim) {
        this.claims.remove(claim.getId());

        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "UPDATE " + this.getTablePrefix() + "claims SET owner = ?, chunk_x = ?, chunk_z = ?, world = ? WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, claim.getOwner().toString());
                statement.setInt(2, claim.getChunkX());
                statement.setInt(3, claim.getChunkZ());
                statement.setString(4, claim.getWorld().getName());
                statement.setInt(5, claim.getId());
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Delete a claim from the database by owner and id
     *
     * @param owner The owner of the claim
     * @param id    The id of the claim
     */
    public void deleteClaim(@NotNull UUID owner, int id) {
        this.claims.remove(id);

        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "DELETE FROM " + this.getTablePrefix() + "claims WHERE owner = ? AND id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, owner.toString());
                statement.setInt(2, id);
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Delete a claim from the database by chunk
     *
     * @param chunk The chunk to delete
     */
    public void deleteClaim(Chunk chunk) {

        this.claims.values().removeIf(claim -> claim.getChunkX() == chunk.getX()
                && claim.getChunkZ() == chunk.getZ()
                && claim.getWorld().equals(chunk.getWorld()));

        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "DELETE FROM " + this.getTablePrefix() + "claims WHERE chunk_x = ? AND chunk_z = ? AND world = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, chunk.getX());
                statement.setInt(2, chunk.getZ());
                statement.setString(3, chunk.getWorld().getName());
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Delete all claims from the database by owner
     *
     * @param owner The owner of the claims
     */
    public void deleteClaims(@NotNull UUID owner) {
        this.claims.values().removeIf(claim -> claim.getOwner().equals(owner));

        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "DELETE FROM " + this.getTablePrefix() + "claims WHERE owner = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, owner.toString());
                statement.executeUpdate();
            }
        }));
    }

    @Override
    public List<Class<? extends DataMigration>> getDataMigrations() {
        return List.of(_1_CreateTables.class);
    }

    public Map<Integer, Claim> getClaims() {
        return claims;
    }

    public Map<UUID, Member> getMembers() {
        return members;
    }

    /**
     * Run a task asynchronously
     *
     * @param callback The callback to run
     */
    private void async(Consumer<BukkitTask> callback) {
        this.rosePlugin.getServer().getScheduler().runTaskAsynchronously(rosePlugin, callback);
    }

}
