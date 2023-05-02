package xyz.oribuin.eternalclaims.storage.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class ObjectSerializer<T> {

    protected Class<T> tClass;

    public ObjectSerializer(Class<T> tClass) {
        this.tClass = tClass;
    }

    public <E extends Enum<E>> ObjectSerializer(final HashMap<E, Boolean> eBooleanHashMap) {
    }

    /**
     * Serialize an object to a string using Base64
     *
     * @param object The object to serialize
     * @return The serialized object
     */
    public final String serialize(T object) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)) {
            outputStream.writeObject(object);
            outputStream.flush();
            return Base64.getEncoder().encodeToString(byteStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deserialize a string to an object using Base64
     *
     * @param data The string to deserialize
     * @return The deserialized object
     */
    @NotNull
    public final Optional<T> deserialize(String data) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             ObjectInputStream inputStream = new ObjectInputStream(byteStream)) {
            return Optional.ofNullable((T) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


}
