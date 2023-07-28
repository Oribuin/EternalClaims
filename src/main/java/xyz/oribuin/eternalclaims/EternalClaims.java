package xyz.oribuin.eternalclaims;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.plugin.PluginManager;
import xyz.oribuin.eternalclaims.hook.PAPI;
import xyz.oribuin.eternalclaims.listener.ClaimListeners;
import xyz.oribuin.eternalclaims.manager.ClaimManager;
import xyz.oribuin.eternalclaims.manager.CommandManager;
import xyz.oribuin.eternalclaims.manager.ConfigurationManager;
import xyz.oribuin.eternalclaims.manager.LocaleManager;

import java.util.List;

public class EternalClaims extends RosePlugin {

    private static EternalClaims instance;

    public EternalClaims() {
        super(-1, -1, ConfigurationManager.class, null, LocaleManager.class, CommandManager.class);

        instance = this;
    }

    public static EternalClaims getInstance() {
        return instance;
    }

    @Override
    protected void enable() {

        // Register PlaceholderAPI
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI(this).register();
        }

        // Register Listeners
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ClaimListeners(this), this);

    }

    @Override
    protected void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(ClaimManager.class);
    }

}
