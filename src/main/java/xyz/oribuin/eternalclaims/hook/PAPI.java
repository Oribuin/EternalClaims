package xyz.oribuin.eternalclaims.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.eternalclaims.EternalClaims;

public class PAPI extends PlaceholderExpansion {

    private final EternalClaims plugin;

    public PAPI(EternalClaims plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "eternalclaims";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Oribuin";
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return false;
    }


}
