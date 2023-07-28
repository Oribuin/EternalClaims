package xyz.oribuin.eternalclaims.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import xyz.oribuin.eternalclaims.claim.Claim;
import xyz.oribuin.eternalclaims.manager.ClaimManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UntrustCommand extends RoseCommand {

    public UntrustCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target, @Optional String allClaims) {
        final ClaimManager manager = this.rosePlugin.getManager(ClaimManager.class);
        final Player player = (Player) context.getSender();

        List<Claim> claims = new ArrayList<>();

        if (allClaims != null) {
            claims.addAll(manager.getClaims(player.getUniqueId()));
        } else {
            Claim claim = manager.getClaim(player.getLocation().getChunk());

            if (claim == null) {
                return;
            }

            claims.add(claim);
        }

        if (claims.isEmpty()) {
            player.sendMessage(Component.text("There are no claims to untrust this player in"));
            return;
        }

        CompletableFuture.runAsync(() -> claims.stream().filter(claim -> claim.untrust(player)).forEach(manager::save))
                .thenRun(() ->
                        player.sendMessage(Component.text("Untrusted player " + target.getName() + " in all claims"))
                );
    }

    @Override
    protected String getDefaultName() {
        return "trust";
    }

    @Override
    public String getDescriptionKey() {
        return "command-trust-description";
    }

    @Override
    public String getRequiredPermission() {
        return "command.trust";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }
}
