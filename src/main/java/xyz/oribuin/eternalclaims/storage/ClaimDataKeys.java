package xyz.oribuin.eternalclaims.storage;

import org.bukkit.NamespacedKey;

/**
 * All data keys used in the plugin.
 */
public final class ClaimDataKeys {

    public static final NamespacedKey CLAIM_ID = new NamespacedKey("eternalclaims", "claim_id");
    public static final NamespacedKey CLAIM_OWNER = new NamespacedKey("eternalclaims", "claim_owner");
    public static final NamespacedKey SETTINGS = new NamespacedKey("eternalclaims", "settings");
    public static final NamespacedKey TRUSTED = new NamespacedKey("eternalclaims", "trusted");

}
