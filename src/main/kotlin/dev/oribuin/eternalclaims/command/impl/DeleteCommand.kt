package dev.oribuin.eternalclaims.command.impl

import dev.oribuin.eternalclaims.storage.Claim
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand
import dev.rosewood.rosegarden.command.framework.CommandContext
import dev.rosewood.rosegarden.command.framework.CommandInfo
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable
import org.bukkit.entity.Player

class DeleteCommand(private val rosePlugin: RosePlugin) : BaseRoseCommand(rosePlugin) {

    @RoseExecutable
    fun execute(context: CommandContext) {
        val player = context.sender as Player
        var claim = Claim.load(player.location.chunk)

        if (claim == null) {
            player.sendMessage("This chunk is not claimed")
            return
        }

        claim.delete(player.chunk)
        player.sendMessage("Deleted chunk claim at ${player.chunk.x}, ${player.chunk.z}")
    }

    override fun createCommandInfo(): CommandInfo {
        return CommandInfo.builder("delete")
            .descriptionKey("command-delete-description")
            .permission("eternalclaims.delete")
            .playerOnly(true)
            .build()
    }

}
//public class CreateCommand extends RoseCommand {
//
//    public CreateCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
//        super(rosePlugin, parent);
//    }
//
//    @RoseExecutable
//    public void execute(CommandContext context) {
//        final ClaimManager manager = this.rosePlugin.getManager(ClaimManager.class);
//        final Player player = (Player) context.getSender();
//
//        if (manager.isClaimed(player.getLocation().getChunk())) {
//            player.sendMessage(Component.text("This chunk is already claimed"));
//            return;
//        }
//
//        Claim claim = new Claim(player.getUniqueId(), player.getLocation().getChunk());
//        CompletableFuture.runAsync(() -> manager.create(claim))
//                .thenRun(() ->
//                        player.sendMessage(Component.text("Created claim"))
//                );
//    }
//
//
//    @Override
//    protected String getDefaultName() {
//        return "create";
//    }
//
//    @Override
//    public String getDescriptionKey() {
//        return "command-create-description";
//    }
//
//    @Override
//    public String getRequiredPermission() {
//        return "eternalclaims.create";
//    }
//
//    @Override
//    public boolean isPlayerOnly() {
//        return true;
//    }
//
//}

