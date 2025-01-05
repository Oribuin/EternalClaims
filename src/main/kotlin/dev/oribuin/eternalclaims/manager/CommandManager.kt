package dev.oribuin.eternalclaims.manager

import dev.oribuin.eternalclaims.command.BaseCommand
import dev.rosewood.rosegarden.RosePlugin
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand
import dev.rosewood.rosegarden.manager.AbstractCommandManager
import java.util.function.Function

class CommandManager(rosePlugin: RosePlugin) : AbstractCommandManager(rosePlugin) {

    override fun getRootCommands(): List<Function<RosePlugin, BaseRoseCommand>> {
        return listOf(Function { BaseCommand(it) })
    }

}
