package ua.pp.myshko.bomberman.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import ua.pp.myshko.bomberman.app.serializers.*;
import ua.pp.myshko.bomberman.server.PlaceMap;
import ua.pp.myshko.bomberman.server.PointFilling;
import ua.pp.myshko.bomberman.server.objects.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author myshko
 */
public class GsonGameMessageParser implements GameMessageParser {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public GameMessage getMessageFromString(String message) throws BombermanException {

        Gson gs = new Gson();
        GameMessage gameMessage;
        try {
            gameMessage = gs.fromJson(message, GameMessage.class);
        } catch (JsonSyntaxException e) {
            logger.log(Level.SEVERE, "Error parsing msg " + message, e);
            throw new BombermanException(e);
        }
        return gameMessage;
    }

    @Override
    public String getStringFromMessage(GameMessage gameMessage) {
        return getStringFromObject(gameMessage);
    }

    @Override
    public String getStringFromObject(Object object) {
        if (object instanceof String) {
            return (String) object;
        } else {
            Gson gs = new GsonBuilder()
                    .enableComplexMapKeySerialization()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Barrier.class, new AbstractGameObjectSerializer())
                    .registerTypeAdapter(Wall.class, new AbstractGameObjectSerializer())
                    .registerTypeAdapter(Bomb.class, new BombSerializer())
                    .registerTypeAdapter(Fire.class, new FireSerializer())
                    .registerTypeAdapter(Player.class, new PlayerSerializer())
                    .registerTypeAdapter(AIPlayer.class, new PlayerSerializer())
                    .registerTypeAdapter(PointFilling.class, new PointFillingSerializer())
                    .registerTypeAdapter(PlaceMap.class, new PlaceMapSerializer())
                    .create();
            return gs.toJson(object);
        }
    }
}
