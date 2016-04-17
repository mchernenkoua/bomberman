package ua.pp.myshko.bomberman.app.serializers;

import com.google.gson.*;
import ua.pp.myshko.bomberman.server.PlaceMap;
import ua.pp.myshko.bomberman.server.Point;

import java.lang.reflect.Type;
/**
 * @author M. Chernenko
 */
public class PlaceMapSerializer implements JsonSerializer<PlaceMap> {

    @Override
    public JsonElement serialize(PlaceMap src,
                                 Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject placeMapObject = new JsonObject();
        JsonArray list = new JsonArray();
        for (Point point: src.allPoints()) {
            JsonObject object = new JsonObject();
            object.add("point", jsonSerializationContext.serialize(point));
            object.add("objects", jsonSerializationContext.serialize(src.getPointFilling(point)));
            list.add(object);
        }
        placeMapObject.add("objects", list);
        return placeMapObject;
    }
}
