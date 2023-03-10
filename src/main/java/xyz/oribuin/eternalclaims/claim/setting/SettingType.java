package xyz.oribuin.eternalclaims.claim.setting;

public enum SettingType {
    MOB_SPAWNING, // Can mobs spawn in the claim (only natural spawns)
    PVP, // Can players attack each other in the claim
    FIRE_SPREAD, // Can fire spread in the claim
    EXPLOSIONS, // Can explosions occur in the claim
    MOB_GRIEFING, // Can mobs grief in the claim
    ;

    public static SettingType from(String name) {
        for (SettingType type : values()) {
            if (type.name().equalsIgnoreCase(name))
                return type;
        }

        return null;
    }

}
