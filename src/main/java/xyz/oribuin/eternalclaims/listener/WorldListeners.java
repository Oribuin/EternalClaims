package xyz.oribuin.eternalclaims.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.manager.ClaimManager;
import xyz.oribuin.eternalclaims.manager.DataManager;

public class WorldListeners implements Listener {

    private final EternalClaims plugin;
    private final ClaimManager claimManager;
    private final DataManager dataManager;

    public WorldListeners(EternalClaims plugin) {
        this.plugin = plugin;
        this.claimManager = this.plugin.getManager(ClaimManager.class);
        this.dataManager = this.plugin.getManager(DataManager.class);
    }

}
