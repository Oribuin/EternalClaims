package dev.oribuin.eternalclaims.storage

enum class ClaimSetting {
    ANIMAL_SPAWNING,  // Check if animals can spawn in the claim
    MONSTER_SPAWNING,  // Check if monsters can spawn in the claim
    PVP,  // Check if players can attack each other in the claim
    FIRE_SPREAD,  // Check if fire can spread in the claim
    EXPLOSIONS,  // Check if explosions can occur in the claim
    MOB_GRIEFING; // Check if mobs can grief in the claim

    val isDefaultValue: Boolean

    constructor() {
        this.isDefaultValue = false
    }

    constructor(defaultValue: Boolean) {
        this.isDefaultValue = defaultValue
    }

    companion object {
        /**
         * Create a map of all the default settings
         *
         * @return The map of default settings
         */
        fun createDefault(): Map<ClaimSetting, Boolean> {
            val map: MutableMap<ClaimSetting, Boolean> = HashMap()
            for (settingType in entries) {
                map[settingType] = settingType.isDefaultValue
            }

            return map
        }
    }
}
