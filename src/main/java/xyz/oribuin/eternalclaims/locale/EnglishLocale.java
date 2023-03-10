package xyz.oribuin.eternalclaims.locale;

import dev.rosewood.rosegarden.locale.Locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnglishLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "en_US";
    }

    @Override
    public String getTranslatorName() {
        return null;
    }

    @Override
    public Map<String, Object> getDefaultLocaleValues() {
        return new LinkedHashMap<>() {{ // #a6b2fc
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "#a6b2fc&lEternalClaims &8| &f");

            this.put("#1", "Base Command Message");
            this.put("base-command-color", "&f");
            this.put("base-command-help", "&fUse #a6b2fc/%cmd% help &efor command information.");

            this.put("#5", "Help Command");
            this.put("command-help-title", "&fAvailable Commands:");
            this.put("command-help-description", "Displays the help menu.");
            this.put("command-help-list-description", "&8 - #a6b2fc/%cmd% %subcmd% %args% &7- %desc%");
            this.put("command-help-list-description-no-args", "&8 - #a6b2fc/%cmd% %subcmd% &7- %desc%");
            
            this.put("#3", "Reload Command");
            this.put("command-reload-description", "Reloads the plugin");
            this.put("command-reload-reloaded", "Configuration and locale files were reloaded");

            this.put("#4", "Generic Command Messages");
            this.put("no-permission", "&cYou don't have permission for that!");
            this.put("only-player", "&cThis command can only be executed by a player.");
            this.put("unknown-command", "&cUnknown command, use #a6b2fc/%cmd% help &cfor more info.");
            this.put("unknown-command-error", "&cAn unknown error occurred; details have been printed to console. Please contact a server administrator.");
            this.put("invalid-subcommand", "&cInvalid subcommand.");
            this.put("invalid-argument", "&cInvalid argument: %message%.");
            this.put("invalid-argument-null", "&cInvalid argument: %name% was null.");
            this.put("missing-arguments", "&cMissing arguments, #a6b2fc%amount% &crequired.");
            this.put("missing-arguments-extra", "&cMissing arguments, #a6b2fc%amount%+ &crequired.");
        }};
    }

}
