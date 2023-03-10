package xyz.oribuin.eternalclaims.claim.permission;

import java.util.Map;

// A terrible object to store permissions in json
public record ClaimPermission(Map<PermissionType, PermissionHolder> permissions) {
    // Empty
}
