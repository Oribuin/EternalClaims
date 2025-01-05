package dev.oribuin.eternalclaims.storage

import com.jeff_media.morepersistentdatatypes.DataType
import org.bukkit.Chunk
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import java.util.*

/**
 * Create a new claim at a specific position. This will be used to store the claim data.
 *
 * @param position The position of the claim.
 * @param owner The owner of the claim.
 * @param trusted The trusted players of the claim.
 * @param settings The settings of the claim.
 * @return The new claim object.
 */
data class Claim(
    val position: ChunkPosition,
    val owner: UUID,
    val trusted: MutableSet<UUID> = mutableSetOf(),
    val settings: MutableMap<ClaimSetting, Boolean> = mutableMapOf()
) {

    /**
     * Save the claim data to a [PersistentDataContainer].
     *
     * @param container The container to save the data to.
     */
    fun save(container: PersistentDataContainer) {
        container.set(CLAIM_OWNER, DataType.UUID, this.owner)
        container.set(TRUSTED, DataType.asList(DataType.UUID), this.trusted.toList())
        container.set(SETTINGS, DataType.asMap(DataType.asEnum(ClaimSetting::class.java), DataType.BOOLEAN), this.settings)
    }

    /**
     * Save the claim data to a [Chunk].
     *
     * @param chunk The chunk to save the data to.
     */
    fun save(chunk: Chunk) = this.save(chunk.persistentDataContainer)

    /**
     * Save the claim data to a [Chunk].
     */
    fun save() = this.position.asChunk()?.let { this.save(it) }

    /**
     * Delete the claim data from the [PersistentDataContainer]
     *
     * @param container The container to delete the data from
     */
    fun delete(container: PersistentDataContainer) {
        container.remove(CLAIM_OWNER)
        container.remove(TRUSTED)
        container.remove(SETTINGS)
    }
    /**
     * Delete the claim data from the [Chunk]
     *
     * @param chunk The chunk to delete the data from
     */
    fun delete(chunk: Chunk) = this.delete(chunk.persistentDataContainer)

    /**
     * Delete the claim data from the chunk position
     */
    fun delete() = this.position.asChunk()?.let { this.delete(it) }

    /**
     * Check if the claim has a specific setting enabled.
     *
     * @param setting The setting to check.
     * @return If the setting is enabled.
     */
    fun hasSetting(setting: ClaimSetting): Boolean {
        return this.settings[setting] ?: false
    }

    /**
     * Set a setting to a specific value.
     *
     * @param setting The setting to set.
     * @param value The value to set.
     */
    fun setSetting(setting: ClaimSetting, value: Boolean) {
        this.settings[setting] = value
    }

    /**
     * Add a trusted player to the claim.
     *
     * @param player The player to add.
     */
    fun addTrusted(player: UUID): Boolean {
        return this.trusted.add(player)
    }

    /**
     * Remove a trusted player from the claim.
     *
     * @param player The player to remove.
     */
    fun removeTrusted(player: UUID): Boolean {
        return this.trusted.remove(player)
    }

    /**
     * Check if a player is trusted in the claim.
     *
     * @param player The player to check.
     * @return If the player is trusted.
     */
    fun isTrusted(player: UUID): Boolean {
        return this.isOwner(player) || this.trusted.contains(player)
    }

    /**
     * Check if a player is trusted in the claim.
     *
     * @param player The player to check
     * @return If the player is trusted
     */
    fun isTrusted(player: Player): Boolean = this.isTrusted(player.uniqueId)

    /**
     * Check if a player is the owner of the claim.
     *
     * @param player The player to check.
     * @return If the player is the owner.
     */
    fun isOwner(player: UUID): Boolean {
        return this.owner == player
    }

    /**
     * All the keys used to store the claim data.
     *
     * @property CLAIM_OWNER The key used to store the claim owner.
     * @property SETTINGS The key used to store the claim settings.
     * @property TRUSTED The key used to store the trusted players.
     */
    companion object {
        val CLAIM_OWNER: NamespacedKey = NamespacedKey("eternalclaims", "claim_owner")
        val SETTINGS: NamespacedKey = NamespacedKey("eternalclaims", "settings")
        val TRUSTED: NamespacedKey = NamespacedKey("eternalclaims", "trusted")

        /**
         * Load a claim from a [PersistentDataContainer].
         *
         * @param container The container to load the data from.
         * @param position The position of the claim.
         * @return The loaded claim object.
         */
        fun load(container: PersistentDataContainer, position: ChunkPosition): Claim? {
            val owner = container.get(CLAIM_OWNER, DataType.UUID) ?: return null
            val trusted = container.getOrDefault(TRUSTED, DataType.asSet(DataType.UUID), mutableSetOf())
            val settings = container.getOrDefault(SETTINGS, DataType.asMap(DataType.asEnum(ClaimSetting::class.java), DataType.BOOLEAN), mutableMapOf())

            return Claim(position, owner, trusted, settings)
        }

        /**
         * Load a claim from a [Chunk].
         *
         * @param chunk The chunk to load the data from.
         * @return The loaded claim object.
         */
        fun load(chunk: Chunk): Claim? = load(chunk.persistentDataContainer, ChunkPosition(chunk.world.name, chunk.x, chunk.z))
    }

}
