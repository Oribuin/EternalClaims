package xyz.oribuin.eternalclaims.claim;

import java.util.UUID;

public record FriendPair(UUID first, UUID second) {

    public boolean contains(UUID uuid) {
        return first.equals(uuid) || second.equals(uuid);
    }

    public UUID getOther(UUID uuid) {
        return first.equals(uuid) ? second : first;
    }

}
