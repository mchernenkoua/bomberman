package ua.pp.myshko.bomberman.app.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ua.pp.myshko.bomberman.server.objects.abstractions.AbstractGameObject;

import java.lang.reflect.Type;

/**
 * @author M. Chernenko
 */
public class AbstractGameObjectSerializer implements JsonSerializer<AbstractGameObject> {

    @Override
    public JsonElement serialize(AbstractGameObject src,
                                 Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject object = new JsonObject();
        object.addProperty("type", src.getType());
        object.addProperty("state", src.getState().toString());
        return object;
    }
}
