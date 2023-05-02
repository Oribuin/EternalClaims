package xyz.oribuin.eternalclaims.listener;

import io.papermc.paper.event.block.BellRingEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.claim.type.PermissionType;
import xyz.oribuin.eternalclaims.claim.type.SettingType;
import xyz.oribuin.eternalclaims.manager.ClaimManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockListeners implements Listener {

    private final EternalClaims plugin;
    private final ClaimManager claimManager;

    public BlockListeners(EternalClaims plugin) {
        this.plugin = plugin;
        this.claimManager = this.plugin.getManager(ClaimManager.class);
    }

    @EventHandler
    public void onRing(BellRingEvent event) {
        Claim claim = this.claimManager.getClaim(event.getBlock().getChunk());
        if (!(event.getEntity() instanceof Player player))
            return;

        if (claim != null && !claim.checkPermission(player, PermissionType.INTERACT)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Claim claim = this.claimManager.getClaim(event.getBlock().getChunk());
        if (claim != null && !claim.checkPermission(event.getPlayer(), PermissionType.BUILD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onExplode(BlockExplodeEvent event) {
        Claim claim = this.claimManager.getClaim(event.getBlock().getChunk());
        if (claim != null && !claim.checkSetting(SettingType.EXPLOSIONS)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        Claim claim = this.claimManager.getClaim(event.getBlock().getChunk());
        if (claim != null && !claim.checkPermission(event.getPlayer(), PermissionType.BUILD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMultiPlace(BlockMultiPlaceEvent event) {

        List<Block> blocks = new ArrayList<>();
        blocks.add(event.getBlock());
        blocks.add(event.getBlockPlaced());
        blocks.add(event.getBlockAgainst());

        List<Claim> claims = blocks.stream().map(block -> this.claimManager.getClaim(block.getChunk()))
                .filter(Objects::nonNull)
                .toList();

        if (claims.isEmpty())
            return;

        if (claims.stream().anyMatch(claim -> !claim.checkPermission(event.getPlayer(), PermissionType.BUILD))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSignChange(SignChangeEvent event) {
        Claim claim = this.claimManager.getClaim(event.getBlock().getChunk());
        if (claim != null && !claim.checkPermission(event.getPlayer(), PermissionType.BUILD)) {
            event.setCancelled(true);
        }
    }

}
