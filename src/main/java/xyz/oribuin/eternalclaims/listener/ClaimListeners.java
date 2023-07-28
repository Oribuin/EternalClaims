package xyz.oribuin.eternalclaims.listener;

import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.claim.ClaimSetting;
import xyz.oribuin.eternalclaims.manager.ClaimManager;
import xyz.oribuin.eternalclaims.manager.ConfigurationManager.Setting;

public class ClaimListeners implements Listener {

    private final ClaimManager manager;

    // All reasons why a player would intentionally spawn an entity in a claim
    public ClaimListeners(EternalClaims plugin) {
        this.manager = plugin.getManager(ClaimManager.class);
    }

    // TODO: Prevent flying machines going through claims
    // TODO: Prevent sheep from eating grass in claims
    // TODO: Prevent players from using ender pearls in claims
    // TODO: Prevent

    /**
     * Loads the claim when the chunk loads.
     */
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Claim claim = this.manager.getClaim(event.getChunk());

        if (claim != null && !this.manager.getCachedClaims().containsKey(claim.getId())) {
            this.manager.getCachedClaims().put(claim.getId(), claim);
        }
    }

    /**
     * Prevents players from breaking blocks in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        Claim claim = this.manager.getClaim(event.getBlock().getChunk());
        if (claim == null) return;

        if (!claim.isTrusted(event.getPlayer()) && !this.manager.isBypassing(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players from placing blocks in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Claim claim = this.manager.getClaim(event.getBlock().getChunk());
        if (claim == null) return;

        if (!claim.isTrusted(event.getPlayer()) && !this.manager.isBypassing(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players from interacting with anything in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction().isLeftClick()) return;

        Claim claim = this.manager.getClaim(event.getClickedBlock().getChunk());
        if (claim == null) return;

        if (!claim.isTrusted(event.getPlayer()) && !this.manager.isBypassing(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players from placing signs in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent event) {
        Claim claim = this.manager.getClaim(event.getBlock().getChunk());
        if (claim == null) return;

        if (!claim.isTrusted(event.getPlayer()) && !this.manager.isBypassing(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents mobs from spawning in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSpawn(CreatureSpawnEvent event) {

        // Ignore if the spawn reason is in the ignored list
        if (Setting.CLAIMS_IGNORED_SPAWN_REASONS.getStringList().contains(event.getSpawnReason().name()))
            return;

        Claim claim = this.manager.getClaim(event.getLocation().getChunk());
        if (claim == null) return;

        if (!claim.checkSetting(ClaimSetting.ANIMAL_SPAWNING) && event.getEntity() instanceof Animals) {
            event.setCancelled(true);
            return;
        }

        if (!claim.checkSetting(ClaimSetting.MONSTER_SPAWNING) && event.getEntity() instanceof Monster) {
            event.setCancelled(true);
        }

    }

    /**
     * Prevents fire from spreading in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBurn(BlockBurnEvent event) {
        Claim claim = this.manager.getClaim(event.getBlock().getChunk());
        if (claim == null) return;

        if (claim.checkSetting(ClaimSetting.FIRE_SPREAD)) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents fire from being ignited in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onIgnite(BlockIgniteEvent event) {
        Claim claim = this.manager.getClaim(event.getBlock().getChunk());
        if (claim == null) return;

        if (claim.checkSetting(ClaimSetting.FIRE_SPREAD)) {
            event.setCancelled(true);
        }
    }


    /**
     * Prevent entities from exploding in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityExplosion(EntityExplodeEvent event) {
        Claim claim = this.manager.getClaim(event.getLocation().getChunk());
        if (claim == null) return;

        if (claim.checkSetting(ClaimSetting.EXPLOSIONS)) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevent blocks from exploding in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockExplode(BlockExplodeEvent event) {
        Claim claim = this.manager.getClaim(event.getBlock().getChunk());
        if (claim == null) return;

        if (claim.checkSetting(ClaimSetting.EXPLOSIONS)) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents players from taking damage from explosions in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onExplosionDamage(EntityDamageEvent event) {
        Claim claim = this.manager.getClaim(event.getEntity().getLocation().getChunk());
        if (claim == null) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            if (claim.checkSetting(ClaimSetting.EXPLOSIONS)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Prevents players from taking damage from other players in a claim.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPVP(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Claim claim = this.manager.getClaim(player.getLocation().getChunk());
        if (claim == null) return;

        if (claim.checkSetting(ClaimSetting.PVP)) {
            event.setCancelled(true);
        }
    }

    /**
     * Prevents entities from changing blocks in a claim. (FBI OPEN UP EVENT)
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChangeBlock(EntityChangeBlockEvent event) {
        Claim claim = this.manager.getClaim(event.getEntity().getLocation().getChunk());
        if (claim == null) return;

        if (claim.checkSetting(ClaimSetting.MOB_GRIEFING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Claim claim = this.manager.getClaim(event.getRightClicked().getLocation().getChunk());
        if (claim == null) return;

        if (claim.isTrusted(event.getPlayer())) return;

        EntityType type = event.getRightClicked().getType();

        if (!type.isAlive() || type == EntityType.ARMOR_STAND) {
            event.setCancelled(true);
        }

    }

}
