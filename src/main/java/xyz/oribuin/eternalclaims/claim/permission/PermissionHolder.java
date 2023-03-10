package xyz.oribuin.eternalclaims.claim.permission;

public enum PermissionHolder {
    OWNER_ONLY, // The owner of the claim
    FRIEND_ONLY, // Only friends & owner can build in the claim
    ALL, // Everyone can build in the claim
    ;

    public static PermissionHolder fromString(String string) {
        for (PermissionHolder holder : values()) {
            if (holder.name().equalsIgnoreCase(string))
                return holder;
        }

        return null;
    }


}
