package xyz.oribuin.eternalclaims.manager;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.claim.FriendPair;
import xyz.oribuin.eternalclaims.claim.Member;
import xyz.oribuin.eternalclaims.database.migrations._1_CreateTables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class DataManager extends AbstractDataManager {

    private final Table<UUID, Integer, Claim> claims = HashBasedTable.create();
    private final Map<UUID, Member> members = new HashMap<>();
    private final Set<FriendPair> friends = new HashSet<>();

    public DataManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }


    /**
     * Load all the database values
     */
    public void loadDatabaseValues() {
        this.claims.clear();
        this.members.clear();
        this.friends.clear();


        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "SELECT * FROM " + this.getTablePrefix() + "claims;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    UUID owner = UUID.fromString(resultSet.getString("owner"));
                    int chunkX = resultSet.getInt("chunk_x");
                    int chunkZ = resultSet.getInt("chunk_z");
                    String world = resultSet.getString("world");

                    Claim claim = new Claim(id, owner);
                    claim.setChunkX(chunkX);
                    claim.setChunkZ(chunkZ);
                    claim.setWorld(this.rosePlugin.getServer().getWorld(world));
                    this.claims.put(owner, id, claim);
                }
            }

            final String query2 = "SELECT * FROM " + this.getTablePrefix() + "members;";
            try (PreparedStatement statement = connection.prepareStatement(query2)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    String name = resultSet.getString("name");
                    String permissions = resultSet.getString("permissions");
                    String friends = resultSet.getString("friends");

                    Member member = new Member(uuid);
                    this.members.put(uuid, member);
                }
            }
        }));
    }

    /**
     * Create a player claim at a chunk location
     *
     * @param owner    The owner of the claim
     * @param chunk    The chunk location of the claim
     * @param callback The callback to run after the claim is created
     */
    public void createClaim(@NotNull UUID owner, @NotNull Chunk chunk, Consumer<Claim> callback) {
        this.async(task -> this.databaseConnector.connect(connection -> {
            final String query = "INSERT INTO " + this.getTablePrefix() + "claims (owner, chunk_x, chunk_z, world) VALUES (?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, owner.toString());
                statement.setInt(2, chunk.getX());
                statement.setInt(3, chunk.getZ());
                statement.setString(4, chunk.getWorld().getName());
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    Claim claim = new Claim(id, owner);
                    claim.setChunkX(chunk.getX());
                    claim.setChunkZ(chunk.getZ());
                    claim.setWorld(chunk.getWorld());
                    this.claims.put(owner, id, claim);

                    callback.accept(claim);
                }
            }
        }));
    }

    /**
     * Update a claim in the database
     *
     * @param claim The claim to update
     */
    public void saveClaim(@NotNull Claim claim) {
        this.claims.put(claim.getOwner(), claim.getId(), claim);

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
        this.claims.remove(owner, id);

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

        this.claims.row(chunk.getWorld().getUID()).values()
                .removeIf(claim -> claim.getChunkX() == chunk.getX()
                        && claim.getChunkZ() == chunk.getZ()
                        && claim.getWorld().equals(chunk.getWorld())
                );

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
        this.claims.row(owner).clear();

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

    public Table<UUID, Integer, Claim> getClaims() {
        return claims;
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
