package dev.oribuin.eternalclaims

import dev.oribuin.eternalclaims.config.Setting
import dev.oribuin.eternalclaims.hook.PapiProvider
import dev.oribuin.eternalclaims.listener.ClaimListeners
import dev.oribuin.eternalclaims.manager.ClaimManager
import dev.oribuin.eternalclaims.manager.CommandManager
import dev.oribuin.eternalclaims.manager.LocaleManager
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.config.RoseSetting
import dev.rosewood.rosegarden.manager.Manager

class EternalClaims : RosePlugin(
    -1,
    -1,
    null,
    LocaleManager::class.java,
    CommandManager::class.java
) {


    override fun enable() {
        // Register PlaceholderAPI
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            PapiProvider(this).register()
        }

        // Register Listeners
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(ClaimListeners(this), this)
    }

    override fun disable() {
    }

    override fun getManagerLoadPriority(): List<Class<out Manager>> {
        return listOf(ClaimManager::class.java)
    }

    override fun getRoseConfigSettings(): MutableList<RoseSetting<*>> {
        return Setting.keys
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: EternalClaims
    }

}
