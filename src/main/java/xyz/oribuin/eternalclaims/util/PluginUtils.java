package xyz.oribuin.eternalclaims.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Get an enum from a string value
     *
     * @param enumClass The enum class
     * @param name      The name of the enum
     * @param <T>       The enum type
     * @return The enum
     */
    public static <T extends Enum<T>> T getEnum(@NotNull Class<T> enumClass, @Nullable String name) {
        if (name == null)
            return null;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase()); // Could just ignore intellij warning
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }


}
