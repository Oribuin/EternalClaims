package xyz.oribuin.eternalclaims.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ViewCommand extends RoseCommand {

    public ViewCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        ((Player) context.getSender()).getInventory().addItem(new ItemStack(Material.MAP));
    }

    @Override
    protected String getDefaultName() {
        return "view";
    }

    @Override
    public String getDescriptionKey() {
        return "command-view-description";
    }

    @Override
    public String getRequiredPermission() {
        return "eternalclaims.command.view";
    }

}
