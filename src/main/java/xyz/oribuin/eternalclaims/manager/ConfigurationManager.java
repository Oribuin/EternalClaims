package xyz.oribuin.eternalclaims.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import xyz.oribuin.eternalclaims.EternalClaims;

import java.util.List;
import java.util.Map;

public class ConfigurationManager extends AbstractConfigurationManager {

    public enum Setting implements RoseSetting {

        // Claim Settings
        CLAIMS("claims", null, "Global settings for all the claims."),
        CLAIMS_SETTINGS_DEFAULT("claims.default-settings", List.of("MOB_SPAWNING"), "These are the default settings for claims when they are first created.", "Any settings not specified here will be set to false."),
        CLAIMS_LOCKED_SETTINGS("claims.locked-settings", List.of("PVP"), "Settings that cannot be changed by players."),
        CLAIMS_DISABLED_WORLDS("claims.disabled-worlds", List.of("world_nether", "world_the_end"), "Worlds where claims are disabled."),

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

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return EternalClaims.getInstance().getManager(ConfigurationManager.class).getConfig();
        }
    }

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{
                "\n" +
                        "__________                    ___________                                         ",
                "\\______   \\ ____  ______ ____ \\_   _____/ ____  ____   ____   ____   _____ ___.__.",
                " |       _//  _ \\/  ___// __ \\ |    __)__/ ___\\/  _ \\ /    \\ /  _ \\ /     <   |  |",
                " |    |   (  <_> )___ \\\\  ___/ |        \\  \\__(  <_> )   |  (  <_> )  Y Y  \\___  |",
                " |____|_  /\\____/____  >\\___  >_______  /\\___  >____/|___|  /\\____/|__|_|  / ____|",
                "        \\/           \\/     \\/        \\/     \\/           \\/             \\/\\/     "
        };
    }

}
