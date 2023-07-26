package xyz.oribuin.eternalclaims.claim;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ClaimSetting {

    ANIMAL_SPAWNING, // Check if animals can spawn in the claim
    MONSTER_SPAWNING, // Check if monsters can spawn in the claim
    PVP, // Check if players can attack each other in the claim
    FIRE_SPREAD, // Check if fire can spread in the claim
    EXPLOSIONS, // Check if explosions can occur in the claim
    MOB_GRIEFING; // Check if mobs can grief in the claim

    private final boolean defaultValue;

    ClaimSetting() {
        this.defaultValue = false;
    }

    /**
     * Create a map of all the default settings
     *
     * @return The map of default settings
     */
    public static Map<String, Boolean> createDefault() {

        Map<String, Boolean> map = new HashMap<>();

        Arrays.stream(ClaimSetting.values()).
                forEach(settingType ->
                        map.put(settingType.name(), settingType.isDefaultValue())
                );

        return map;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

}
