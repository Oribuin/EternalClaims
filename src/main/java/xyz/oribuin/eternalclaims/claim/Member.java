package xyz.oribuin.eternalclaims.claim;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.claim.setting.SettingType;
import xyz.oribuin.eternalclaims.manager.ConfigurationManager.Setting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Member {

    private final @NotNull UUID uuid; // The UUID of the member
    private @Nullable Player cachedPlayer; // The cached player
    private @NotNull Map<SettingType, Boolean> settings; // These settings will apply to the claims unless overridden


    public Member(@NotNull UUID uuid) {
        this.uuid = uuid;
        this.settings = new HashMap<>();
    }

    /**
     * Load the default settings for the member
     *
     */
    public void loadDefault() {
        Setting.CLAIMS_SETTINGS_DEFAULT.getStringList().forEach(setting -> this.settings.put(SettingType.valueOf(setting), true));


    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getCachedPlayer() {
        if (this.cachedPlayer == null)
            this.cachedPlayer = Bukkit.getPlayer(this.uuid);

        return cachedPlayer;
    }

    public void setCachedPlayer(@Nullable Player cachedPlayer) {
        this.cachedPlayer = cachedPlayer;
    }

}
