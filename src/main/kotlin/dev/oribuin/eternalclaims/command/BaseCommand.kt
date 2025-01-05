package dev.oribuin.eternalclaims.command

import dev.oribuin.eternalclaims.command.impl.CreateCommand
import dev.oribuin.eternalclaims.command.impl.DeleteCommand
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand
import dev.rosewood.rosegarden.command.framework.CommandInfo

class BaseCommand(private val rosePlugin: RosePlugin) : BaseRoseCommand(rosePlugin) {

    override fun createCommandInfo(): CommandInfo {
        return CommandInfo.builder("claims")
            .arguments(this.createArguments())
            .aliases("eternalclaims", "claim")
            .descriptionKey("command-base-description")
            .permission("eternalclaims.base")
            .playerOnly(true)
            .build()
    }

    private fun createArguments(): ArgumentsDefinition {
        return ArgumentsDefinition.builder().requiredSub(CreateCommand(this.rosePlugin), DeleteCommand(this.rosePlugin))
    }

}
