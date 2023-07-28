package xyz.oribuin.eternalclaims.storage;

import xyz.oribuin.eternalclaims.claim.ClaimSetting;

import java.util.Map;

public record ClaimSettingHolder(Map<ClaimSetting, Boolean> settings) {
    // Empty class
}
