package ua.pp.myshko.bomberman.app.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ua.pp.myshko.bomberman.server.PointFilling;

import java.lang.reflect.Type;

/**
 * @author M. Chernenko
 */
public class PointFillingSerializer implements JsonSerializer<PointFilling> {

    @Override
    public JsonElement serialize(PointFilling src,
                                 Type type, JsonSerializationContext jsonSerializationContext) {

        return jsonSerializationContext.serialize(src.getObjects());
    }
}
