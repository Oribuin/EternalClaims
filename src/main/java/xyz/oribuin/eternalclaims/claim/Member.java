package xyz.oribuin.eternalclaims.claim;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.eternalclaims.claim.type.PermissionType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Member {

    private final @NotNull UUID uuid; // The UUID of the member
    private @Nullable Player cachedPlayer; // The cached player
    private @Nullable String username; // The username of the member
    private Set<UUID> trustedUsers; // The trusted users of the member
    private Map<PermissionType, Boolean> permissions; // The permissions of the member

    public Member(@NotNull UUID uuid) {
        this.uuid = uuid;
        this.trustedUsers = new HashSet<>();
        this.permissions = new HashMap<>();
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @Nullable
    public Player getCachedPlayer() {
        if (this.cachedPlayer == null) {
            this.cachedPlayer = Bukkit.getPlayer(this.uuid);
        }

        return cachedPlayer;
    }

    /**
     * Refresh the cached player of the member
     */
    public void refresh() {
        this.cachedPlayer = Bukkit.getPlayer(this.uuid);
    }

    public void setCachedPlayer(@Nullable Player cachedPlayer) {
        this.cachedPlayer = cachedPlayer;
    }

    @Nullable
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @NotNull
    public Set<UUID> getTrustedUsers() {
        return this.trustedUsers;
    }

    public void setTrustedUsers(@NotNull Set<UUID> trustedUsers) {
        this.trustedUsers = trustedUsers;
    }

    public Map<PermissionType, Boolean> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(@NotNull Map<PermissionType, Boolean> permissions) {
        this.permissions = permissions;
    }

}
