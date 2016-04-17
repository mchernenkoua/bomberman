package ua.pp.myshko.bomberman.app.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ua.pp.myshko.bomberman.server.objects.Player;

import java.lang.reflect.Type;

/**
 * @author M. Chernenko
 */
public class PlayerSerializer implements JsonSerializer<Player> {

    @Override
    public JsonElement serialize(Player src,
                                 Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject object = new JsonObject();
        object.addProperty("type", src.getType());
        object.addProperty("state", src.getState().toString());
        object.addProperty("id", src.getId());
        object.addProperty("bombCount", src.getBombCount());
        object.addProperty("score", src.getScore());
        return object;
    }
}
