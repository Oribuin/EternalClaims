package xyz.oribuin.eternalclaims.storage.serializer;

import java.util.HashSet;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class HashSetSerializer extends ObjectSerializer<HashSet<UUID>> {

    public HashSetSerializer(final HashSet<UUID> hashSet) {
        super((Class<HashSet<UUID>>) hashSet.getClass());
    }

}
