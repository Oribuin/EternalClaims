package xyz.oribuin.eternalclaims.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.manager.ClaimManager;

import java.util.concurrent.CompletableFuture;

public class DeleteCommand extends RoseCommand {

    public DeleteCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final ClaimManager manager = this.rosePlugin.getManager(ClaimManager.class);
        final Player player = (Player) context.getSender();
        final Claim claim = manager.getClaim(player.getLocation().getChunk());

        if (claim == null) {
            player.sendMessage(Component.text("This chunk is not claimed"));
            return;
        }

        if (!claim.getOwner().equals(player.getUniqueId()) && !manager.isBypassing(player.getUniqueId())) {
            player.sendMessage(Component.text("You do not own this claim"));
            return;
        }

        CompletableFuture.runAsync(() -> manager.delete(claim))
                .thenRun(() ->
                        player.sendMessage(Component.text("Deleted claim"))
                );
    }

    @Override
    public String getDefaultName() {
        return "delete";
    }

    @Override
    public String getDescriptionKey() {
        return "command-delete-description";
    }

    @Override
    public String getRequiredPermission() {
        return "eternalclaims.delete";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
