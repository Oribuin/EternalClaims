package xyz.oribuin.eternalclaims.storage;

import org.bukkit.NamespacedKey;

/**
 * Define all the namespaces used in the plugin
 */
public final class ClaimNamespace {

    public static NamespacedKey CLAIM_ID;

    static {
        CLAIM_ID = new NamespacedKey("eternalclaims", "claim_id");
    }

}
