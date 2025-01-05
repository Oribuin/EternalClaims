package dev.oribuin.eternalclaims.storage

import org.bukkit.Bukkit
import org.bukkit.Chunk

data class ChunkPosition(val world: String, val x: Int, val z: Int) {

    /**
     * Convert a [ChunkPosition] to a [Chunk] object.
     *
     * @return The [Chunk] object.
     */
    fun asChunk(): Chunk? {
        return Bukkit.getWorld(this.world)?.getChunkAt(this.x, this.z)
    }

    /**
     * Check if the [Chunk] is loaded.
     *
     * @return If the [Chunk] is loaded.
     */
    fun isLoaded(): Boolean {
        return this.asChunk()?.isLoaded ?: false
    }

    /**
     * Check if the [Chunk] matches the [ChunkPosition].
     *
     * @param chunk The [Chunk] to compare.
     * @return If the [Chunk] matches the [ChunkPosition].
     */
    fun matches(chunk: Chunk): Boolean {
        return chunk.x == this.x && chunk.z == this.z && chunk.world.name == this.world
    }

    /**
     * Check if the [Chunk] matches the [ChunkPosition].
     *
     * @param position The [ChunkPosition] to compare.
     * @return If the [Chunk] matches the [ChunkPosition].
     */
    fun Chunk.matches(position: ChunkPosition) = position.matches(this)

    /**
     * Check if the [Chunk] matches the [ChunkPosition].
     *
     * @param chunk The [ChunkPosition] to compare.
     * @return If the [Chunk] matches the [ChunkPosition].
     */
    fun matches(chunk: ChunkPosition): Boolean {
        return chunk.x == this.x && chunk.z == this.z && chunk.world == this.world
    }

    companion object {
        /**
         * Convert a [Chunk] to a [ChunkPosition] object.
         *
         * @return The [ChunkPosition] object.
         */
        fun Chunk.asPosition(): ChunkPosition {
            return ChunkPosition(this.world.name, this.x, this.z)
        }

        /**
         * Check if the [Chunk] matches the [ChunkPosition].
         *
         * @param position The [ChunkPosition] to compare.
         * @return If the [Chunk] matches the [ChunkPosition].
         */
        fun Chunk.matches(position: ChunkPosition) = position.matches(this)

        /**
         * Check if two [Chunk] are the same
         */
        fun Chunk.matches(chunk: Chunk) = this.asPosition().matches(chunk.asPosition())
    }
}
