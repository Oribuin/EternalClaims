package dev.oribuin.eternalclaims.manager

import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.manager.Manager
import java.util.*

class ClaimManager(rosePlugin: RosePlugin) : Manager(rosePlugin) {

    private val bypassing = mutableSetOf<UUID>()

    /**
     * Check if a player is bypassing
     *
     * @param uuid The UUID of the player
     *
     * @return If the player is bypassing
     */
    fun isBypassing(uuid: UUID): Boolean {
        return bypassing.contains(uuid)
    }

    /**
     * Toggle bypassing for a player
     *
     * @param uuid The UUID of the player
     *
     * @return If the player is bypassing
     */
    fun toggleBypass(uuid: UUID): Boolean {
        return if (!bypassing.remove(uuid)) {
            bypassing.add(uuid)
        } else {
            false
        }
    }

    fun bypassing(): Set<UUID> {
        return bypassing
    }

    override fun reload() {
    }

    override fun disable() {
    }
}
