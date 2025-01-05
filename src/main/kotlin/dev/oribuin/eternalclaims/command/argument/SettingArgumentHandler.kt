package dev.oribuin.eternalclaims.command.argument

import dev.oribuin.eternalclaims.storage.ClaimSetting
import dev.oribuin.eternalclaims.util.parseEnum
import dev.rosewood.rosegarden.command.framework.Argument
import dev.rosewood.rosegarden.command.framework.ArgumentHandler
import dev.rosewood.rosegarden.command.framework.ArgumentHandler.HandledArgumentException
import dev.rosewood.rosegarden.command.framework.CommandContext
import dev.rosewood.rosegarden.command.framework.InputIterator
import dev.rosewood.rosegarden.utils.StringPlaceholders
import java.util.*

class SettingArgumentHandler : ArgumentHandler<ClaimSetting>(ClaimSetting::class.java) {

    @Throws(HandledArgumentException::class)
    override fun handle(commandContext: CommandContext, argument: Argument, inputIterator: InputIterator): ClaimSetting? {
        val input = inputIterator.next()

        return parseEnum(ClaimSetting::class, input) ?: throw HandledArgumentException("argument-handler-setting", StringPlaceholders.of("input", input))
    }

    override fun suggest(commandContext: CommandContext, argument: Argument, strings: Array<String>): List<String> {
        return Arrays.stream(ClaimSetting.entries.toTypedArray()).map { setting: ClaimSetting -> setting.name.lowercase(Locale.getDefault()) }.toList()
    }
}
