package xyz.oribuin.eternalclaims.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.manager.ClaimManager;

public class WorldListeners implements Listener {

    private final EternalClaims plugin;
    private final ClaimManager manager;

    public WorldListeners(EternalClaims plugin) {
        this.plugin = plugin;
        this.manager = this.plugin.getManager(ClaimManager.class);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Claim claim = this.manager.getClaim(event.getChunk());

        if (claim != null) {
            this.manager.getCachedClaims().put(claim.getId(), claim);
        }

    }

}
