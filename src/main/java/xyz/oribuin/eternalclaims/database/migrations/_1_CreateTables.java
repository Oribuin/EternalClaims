package xyz.oribuin.eternalclaims.database.migrations;

import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import dev.rosewood.rosegarden.database.MySQLConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _1_CreateTables extends DataMigration {

    public _1_CreateTables() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        final String autoIncrement = connector instanceof MySQLConnector ? "AUTO_INCREMENT" : "";

        try (Statement statement = connection.createStatement()) {

            // id, owner, chunk_x, chunk_z, world, settings
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + tablePrefix + "claims (" +
                    "id INTEGER PRIMARY KEY " + autoIncrement + ", " +
                    "owner VARCHAR(36) NOT NULL, " +
                    "chunk_x INTEGER NOT NULL, " +
                    "chunk_z INTEGER NOT NULL, " +
                    "world VARCHAR(36) NOT NULL, " +
                    "settings TEXT); ");

            // uuid, name, permission, settings, trustedUsers
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + tablePrefix + "members (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "`name` VARCHAR(32) NOT NULL, " +
                    "settings TEXT, " +
                    "trustedUsers TEXT); ");

            statement.executeBatch();
        }

    }

}
