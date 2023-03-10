package xyz.oribuin.eternalclaims.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.List;

public class ClaimsCommandWrapper extends RoseCommandWrapper {

    public ClaimsCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "claims";
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of("eclaims");
    }

    @Override
    public List<String> getCommandPackages() {
        return List.of("xyz.oribuin.eternalclaims.command.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return true;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }

}
