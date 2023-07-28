package xyz.oribuin.eternalclaims.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import xyz.oribuin.eternalclaims.EternalClaims;
import xyz.oribuin.eternalclaims.claim.ClaimSetting;

import java.util.List;
import java.util.Map;

public class ConfigurationManager extends AbstractConfigurationManager {

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{
                "___________ __                             .__  _________ .__         .__                ",
                "\\_   _____//  |_  ___________  ____ _____  |  | \\_   ___ \\|  | _____  |__| _____   ______",
                " |    __)_\\   __\\/ __ \\_  __ \\/    \\\\__  \\ |  | /    \\  \\/|  | \\__  \\ |  |/     \\ /  ___/",
                " |        \\|  | \\  ___/|  | \\/   |  \\/ __ \\|  |_\\     \\___|  |__/ __ \\|  |  Y Y  \\\\___ \\",
                "/_______  /|__|  \\___  >__|  |___|  (____  /____/\\______  /____(____  /__|__|_|  /____  >",
                "        \\/           \\/           \\/     \\/             \\/          \\/         \\/     \\/ "
        };
    }

    public enum Setting implements RoseSetting {

        // Claim Settings
        CLAIMS("claims", null, "Global settings for all the claims."),

        CLAIMS_SETTINGS_DEFAULT("claims.default-settings", null,
                "These are the default settings for claims when they are first created.",
                "Any settings not specified here will be set to false."
        ),

        // All Default Claim Settings
        CLAIM_SETTINGS_DEFAULT(
                "claims.default-settings",
                ClaimSetting.createDefault(),
                "These are the default settings for claims when they are first created.", "Any settings not specified here will be set to false."
        ),

        CLAIMS_LOCKED_SETTINGS("claims.locked-settings", List.of("PVP"), "Settings that cannot be changed by players."),
        CLAIMS_DISABLED_WORLDS("claims.disabled-worlds", List.of("world_nether", "world_the_end"), "Worlds where claims are disabled."),
        CLAIMS_IGNORED_SPAWN_REASONS("claims.ignored-spawn-reasons",
                List.of(
                        "BEEHIVE",
                        "BUILD_IRONGOLEM",
                        "BUILD_SNOWMAN",
                        "VILLAGE_DEFENSE",
                        "BREEDING",
                        "EGG",
                        "SPAWNER"
                ),
                "If an entity is spawned in this chunk under this reason, it will not be removed."),
        ;

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String[] getComments() {
            return this.comments;
        }

        @Override
        public Object getCachedValue() {
            return this.value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Boolean> getMap() {
            this.loadValue();

            return (Map<String, Boolean>) this.getCachedValue();
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return EternalClaims.getInstance().getManager(ConfigurationManager.class).getConfig();
        }
    }

}
