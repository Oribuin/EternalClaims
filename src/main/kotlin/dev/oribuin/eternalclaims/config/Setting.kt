package dev.oribuin.eternalclaims.config

import dev.oribuin.eternalclaims.EternalClaims
import dev.rosewood.rosegarden.config.CommentedConfigurationSection
import dev.rosewood.rosegarden.config.RoseSetting
import dev.rosewood.rosegarden.config.RoseSettingSerializer
import dev.rosewood.rosegarden.config.RoseSettingSerializers.STRING_LIST

object Setting {

    val keys = mutableListOf<RoseSetting<*>>()

    val CLAIMS_SECTION = create("claims", "The global settings for all claims")
    val CLAIMS_DISABLED_WORLDS = create("claims.disabled-worlds", STRING_LIST, listOf("disabled_world"), "The worlds where claims are disabled")
    val CLAIMS_IGNORED_SPAWWN_REASONS = create(
        "claims.ignored-spawn-reasons", STRING_LIST, listOf(
            "BEEHIVE",
            "BUILD_IRONGOLEM",
            "BUILD_SNOWMAN",
            "VILLAGE_DEFENSE",
            "BREEDING",
            "EGG",
            "SPAWNER"
        ), "If an entity is spawned with one of these reasons, it will not be cleared"
    )



    /**
     * Initialize the configuration settings for the plugin
     *
     * @param key The key of the setting
     * @param serializer The serializer for the setting
     * @param default The default value for the setting
     * @param comments The comments for the setting
     * @return The setting
     */
    private fun <T> create(key: String, serializer: RoseSettingSerializer<T>, default: T, vararg comments: String = arrayOf()): RoseSetting<T> {
        val setting = RoseSetting.backed(EternalClaims.instance, key, serializer, default, *comments)
        keys.add(setting)
        return setting
    }


    /**
     * Create a new configuration section
     *
     * @param key The key of the section
     * @param comments The comments for the section
     */
    private fun create(key: String, vararg comments: String): RoseSetting<CommentedConfigurationSection> {
        val setting = RoseSetting.backedSection(EternalClaims.instance, key, *comments)
        keys.add(setting)
        return setting
    }

}