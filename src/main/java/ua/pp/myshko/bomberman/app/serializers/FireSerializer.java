package ua.pp.myshko.bomberman.app.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ua.pp.myshko.bomberman.server.objects.Fire;

import java.lang.reflect.Type;

/**
 * @author M. Chernenko
 */
public class FireSerializer implements JsonSerializer<Fire> {

    @Override
    public JsonElement serialize(Fire src,
                                 Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject object = new JsonObject();
        object.addProperty("type", src.getType());
        object.addProperty("state", src.getState().toString());
        object.addProperty("owner", src.getOwner().getId());
        return object;
    }
}
