package dev.oribuin.eternalclaims.listener

import dev.oribuin.eternalclaims.EternalClaims
import dev.oribuin.eternalclaims.config.Setting
import dev.oribuin.eternalclaims.manager.ClaimManager
import dev.oribuin.eternalclaims.storage.ChunkPosition.Companion.matches
import dev.oribuin.eternalclaims.storage.Claim
import dev.oribuin.eternalclaims.storage.ClaimSetting
import dev.oribuin.eternalclaims.util.getManager
import org.bukkit.entity.Animals
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockBurnEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.block.BlockPistonExtendEvent
import org.bukkit.event.block.BlockPistonRetractEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class ClaimListeners(private val plugin: EternalClaims) : Listener {

    // TODO: Prevent sheep from eating grass in claims (?)
    // TODO: Prevent players from using ender pearls in claims
    /**
     * Prevents players from breaking blocks in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onPlace(event: BlockPlaceEvent) {
        val claim = Claim.load(event.block.chunk) ?: return
        val manager = this.plugin.getManager<ClaimManager>()

        if (!claim.isTrusted(event.player) && !manager.isBypassing(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents players from placing blocks in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onBreak(event: BlockBreakEvent) {
        val claim = Claim.load(event.block.chunk) ?: return
        val manager = this.plugin.getManager<ClaimManager>()

        if (!claim.isTrusted(event.player) && !manager.isBypassing(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents players from interacting with anything in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) return

        event.clickedBlock?.let { block ->
            val claim = Claim.load(block.chunk) ?: return
            val manager = this.plugin.getManager<ClaimManager>()

            if (!claim.isTrusted(event.player) && !manager.isBypassing(event.player.uniqueId)) {
                event.isCancelled = true
            }
        }
    }

    /**
     * Prevents players from placing signs in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onSignChange(event: SignChangeEvent) {
        val claim = Claim.load(event.block.chunk) ?: return
        val manager = this.plugin.getManager<ClaimManager>()

        if (!claim.isTrusted(event.player) && !manager.isBypassing(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents mobs from spawning in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onSpawn(event: CreatureSpawnEvent) {

        // Ignore if the spawn reason is in the ignored list
//        if (Setting.CLAIMS_IGNORED_SPAWWN_REASONS.get().contains(event.spawnReason.name)) return // TODO: Implement this

        val claim = Claim.load(event.location.chunk) ?: return

        if (!claim.hasSetting(ClaimSetting.ANIMAL_SPAWNING) && event.entity is Animals) {
            event.isCancelled = true
            return
        }

        if (!claim.hasSetting(ClaimSetting.MONSTER_SPAWNING) && event.entity is Monster) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents fire from spreading in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onBlockBurn(event: BlockBurnEvent) {
        val claim = Claim.load(event.block.chunk) ?: return

        if (claim.hasSetting(ClaimSetting.FIRE_SPREAD)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents fire from being ignited in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onIgnite(event: BlockIgniteEvent) {
        val claim = Claim.load(event.block.chunk) ?: return

        if (!claim.hasSetting(ClaimSetting.FIRE_SPREAD)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevent entities from exploding in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onEntityExplosion(event: EntityExplodeEvent) {
        val claim = Claim.load(event.location.chunk) ?: return

        if (!claim.hasSetting(ClaimSetting.EXPLOSIONS)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevent blocks from exploding in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onBlockExplode(event: BlockExplodeEvent) {
        val claim = Claim.load(event.block.chunk) ?: return

        if (!claim.hasSetting(ClaimSetting.EXPLOSIONS)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents players from taking damage from explosions in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onExplosionDamage(event: EntityDamageEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            val claim = Claim.load(event.entity.location.chunk) ?: return

            if (!claim.hasSetting(ClaimSetting.EXPLOSIONS)) {
                event.isCancelled = true
            }
        }
    }

    /**
     * Prevents players from taking damage from other players in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onPVP(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player) return
        if (event.damager !is Player) return

        val claim = Claim.load(event.entity.location.chunk) ?: Claim.load(event.damager.location.chunk) ?: return

        if (!claim.hasSetting(ClaimSetting.PVP)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents entities from changing blocks in a claim. (FBI OPEN UP EVENT)
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onChangeBlock(event: EntityChangeBlockEvent) {
        val claim = Claim.load(event.block.chunk) ?: return

        if (!claim.hasSetting(ClaimSetting.MOB_GRIEFING)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevents players from interacting with entities in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onEntityInteract(event: PlayerInteractEntityEvent) {
        val type = event.rightClicked.type
        if (type.isAlive || type == EntityType.ARMOR_STAND || type == EntityType.PLAYER) return

        val claim = Claim.load(event.rightClicked.location.chunk) ?: return
        val manager = this.plugin.getManager<ClaimManager>()

        if (!claim.isTrusted(event.player) && !manager.isBypassing(event.player.uniqueId)) {
            event.isCancelled = true
        }
    }

    /**
     * Prevent flying machines going fuckywucky
     */
    @EventHandler(ignoreCancelled = true)
    fun onPistonRetract(event: BlockPistonRetractEvent) {
        val current = event.block
        val next = current.getRelative(event.direction.oppositeFace)

        // If they're the same who gives a fuck!!!
        if (current.chunk.matches(next.chunk)) return

        // make sure they're both chunks
        Claim.load(current.chunk) ?: return
        Claim.load(next.chunk) ?: return

        event.isCancelled = true
    }

    /**
     * Prevent flying machines going fuckywucky
     */
    @EventHandler
    fun onPistonExtend(event: BlockPistonExtendEvent) {
        val current = event.block
        val next = current.getRelative(event.direction.oppositeFace)

        // If they're the same who gives a fuck!!!
        if (current.chunk.matches(next.chunk)) return

        // make sure they're both chunks
        Claim.load(current.chunk) ?: return
        Claim.load(next.chunk) ?: return

        event.isCancelled = true
    }

}
