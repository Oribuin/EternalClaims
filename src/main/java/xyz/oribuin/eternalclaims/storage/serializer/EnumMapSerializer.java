package xyz.oribuin.eternalclaims.storage.serializer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EnumMapSerializer<E extends Enum<E>> extends ObjectSerializer<HashMap<E, Boolean>> {

    public EnumMapSerializer(HashMap<E, Boolean> map) {
        super((Class<HashMap<E, Boolean>>) map.getClass());
    }

    public EnumMapSerializer() {
        super((Class<HashMap<E, Boolean>>) new HashMap<>().getClass());
    }

}

//public class EnumMapSerializer<E extends Enum<E>> {
//    private final Class<E> eClass;
//
//    public EnumMapSerializer(Class<E> eClass) {
//        this.eClass = eClass;
//    }
//
//    public String serialize(Map<E, Boolean> map) {
//        try (
//                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//                ObjectOutputStream outputStream = new ObjectOutputStream(byteStream)
//        ) {
//            outputStream.writeObject(map);
//            outputStream.flush();
//            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @SuppressWarnings("unchecked")
//    public Map<E, Boolean> deserialize(String data) {
//        try (
//                ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
//                ObjectInputStream inputStream = new ObjectInputStream(byteStream)
//        ) {
//            return (Map<E, Boolean>) inputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//}
