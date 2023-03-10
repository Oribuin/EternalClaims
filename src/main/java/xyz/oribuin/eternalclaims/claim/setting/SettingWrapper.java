package xyz.oribuin.eternalclaims.claim.setting;

import java.util.Map;

// Wrap settings into a class so we can store it in json
public record SettingWrapper(Map<SettingType, Boolean> settings) {
}
