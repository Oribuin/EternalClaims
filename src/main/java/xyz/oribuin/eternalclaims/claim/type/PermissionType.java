package xyz.oribuin.eternalclaims.claim.type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum PermissionType {
    BUILD, // Can build in the claim
    INTERACT, // Can interact with blocks in the claim
    CONTAINER, // Can open containers in the claim
    ACCESS, // Can access buttons and levers in the claim
    TELEPORT, // Can teleport to the claim
    KILL_ANIMALS, // Can kill animals in the claim
    ; // Nothing

    /**
     * Create the default map of permissions for a user.
     */
    public static Map<PermissionType, Boolean> createDefault() {
        Map<PermissionType, Boolean> map = new HashMap<>();
        for (PermissionType value : PermissionType.values()) {
            map.put(value, true);
        }

        return map;
    }

}
