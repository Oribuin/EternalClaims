package dev.oribuin.eternalclaims.hook

import dev.oribuin.eternalclaims.EternalClaims
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class PapiProvider(private val plugin: EternalClaims) : PlaceholderExpansion() {

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        return super.onRequest(player, params)
    }

    override fun getIdentifier(): String = "eternalclaims"

    override fun getAuthor(): String = "Oribuin"

    override fun getVersion(): String = "1.0.0"

    override fun persist(): Boolean = true

}
