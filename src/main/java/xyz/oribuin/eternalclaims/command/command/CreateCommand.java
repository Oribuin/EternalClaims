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

public class CreateCommand extends RoseCommand {

    public CreateCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final ClaimManager manager = this.rosePlugin.getManager(ClaimManager.class);
        final Player player = (Player) context.getSender();

        if (manager.isClaimed(player.getLocation().getChunk())) {
            player.sendMessage(Component.text("This chunk is already claimed"));
            return;
        }

        Claim claim = new Claim(player.getUniqueId(), player.getLocation().getChunk());
        CompletableFuture.runAsync(() -> manager.create(claim))
                .thenRun(() ->
                        player.sendMessage(Component.text("Created claim"))
                );
    }


    @Override
    protected String getDefaultName() {
        return "create";
    }

    @Override
    public String getDescriptionKey() {
        return "command-create-description";
    }

    @Override
    public String getRequiredPermission() {
        return "eternalclaims.create";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
