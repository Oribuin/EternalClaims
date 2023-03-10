package xyz.oribuin.eternalclaims.util;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class PluginUtils {

    public PluginUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean usingPaper() {
        try {
            Class.forName("com.destroystokyo.paper.util.VersionFetcher");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Parse a string such as 1h 30m 10s into a long
     *
     * @param time The time to parse
     * @return The parsed time
     */
    public static long parseToTime(String time) {
        return Duration.parse(time).toMillis();
    }

    /**
     * Parse a time in milliseconds into a string such as 1h 30m 10s
     *
     * @param time The time to parse
     * @return The parsed time
     */
    public static String parseFromTime(long time) {
        return Duration.ofMillis(time).toString();
    }

    /**
     * Format a location into a string
     *
     * @param location The location to format
     * @return The formatted location
     */
    public static String formatLocation(Location location) {
        if (location == null)
            return "null";

        return String.format("%s, %s, %s", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static @Nullable Location asBlockLoc(@Nullable Location location) {
        if (location == null)
            return null;

        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static @Nullable Location asCenterLoc(@Nullable Location location, float yaw, float pitch) {
        if (location == null)
            return null;

        return new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY() + 0.5, location.getBlockZ() + 0.5, yaw, pitch);
    }

    public static @Nullable Location asCenterLoc(@Nullable Location location) {
        if (location == null)
            return null;

        return new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY() + 0.5, location.getBlockZ() + 0.5);
    }

    public static @Nullable Integer asInt(@Nullable String string) {
        if (string == null)
            return null;

        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static @NotNull Color getColor(String hex) {
        try {
            java.awt.Color javaColor = java.awt.Color.decode(hex);
            return Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
        } catch (IllegalArgumentException e) {
            return Color.BLACK;
        }
    }

    /**
     * Get all the particle locations to spawn a hollow cube in between point A & Point B
     *
     * @param corner1 The first corner.
     * @param corner2 The second corner
     * @return The list of particle locations
     * @author Esophose
     * @ <a href="https://github.com/Rosewood-Development/PlayerParticles/blob/master/src/main/java/dev/esophose/playerparticles/styles/ParticleStyleOutline.java#L86">...</a>
     */
    public static List<Location> getCube(Location corner1, Location corner2, double outerAdjustment) {
        List<Location> result = new ArrayList<>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX()) + outerAdjustment;
        double maxY = Math.max(corner1.getY(), corner2.getY()) + outerAdjustment;
        double maxZ = Math.max(corner1.getZ(), corner2.getZ()) + outerAdjustment;

        for (double x = minX; x <= maxX; x += 0.5) {
            result.add(new Location(world, x, minY, minZ));
            result.add(new Location(world, x, maxY, minZ));
            result.add(new Location(world, x, minY, maxZ));
            result.add(new Location(world, x, maxY, maxZ));
        }

        for (double y = minY; y <= maxY; y += 0.5) {
            result.add(new Location(world, minX, y, minZ));
            result.add(new Location(world, maxX, y, minZ));
            result.add(new Location(world, minX, y, maxZ));
            result.add(new Location(world, maxX, y, maxZ));
        }

        for (double z = minZ; z <= maxZ; z += 0.5) {
            result.add(new Location(world, minX, minY, z));
            result.add(new Location(world, maxX, minY, z));
            result.add(new Location(world, minX, maxY, z));
            result.add(new Location(world, maxX, maxY, z));
        }

        return result;
    }

    /**
     * Get all the connected chunks to a given chunk
     * @param middle The middle chunk
     * @return The list of connected chunks
     */
    public static List<Chunk> getConnectingChunks(Chunk middle) {
        List<Chunk> chunks = new ArrayList<>();
        chunks.add(middle);

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0)
                    continue;

                chunks.add(middle.getWorld().getChunkAt(middle.getX() + x, middle.getZ() + z));
            }
        }

        return chunks;
    }

}
