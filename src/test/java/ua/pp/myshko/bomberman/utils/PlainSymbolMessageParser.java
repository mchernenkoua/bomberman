package ua.pp.myshko.bomberman.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ua.pp.myshko.bomberman.app.BombermanException;
import ua.pp.myshko.bomberman.app.GameMessage;
import ua.pp.myshko.bomberman.app.GameMessageParser;
import ua.pp.myshko.bomberman.server.*;
import ua.pp.myshko.bomberman.server.objects.*;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;

/**
 * @author M. Chernenko
 */
public class PlainSymbolMessageParser implements GameMessageParser {

    private GameWorldParams gameWorldParams;

    public PlainSymbolMessageParser(GameWorldParams gameWorldParams) {
        this.gameWorldParams = gameWorldParams;
    }

    @Override
    public GameMessage getMessageFromString(String message) throws BombermanException {

        Gson gs = new Gson();
        GameMessage gameMessage = null;
        try {
            gameMessage = gs.fromJson(message, GameMessage.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return gameMessage;
    }

    @Override
    public String getStringFromMessage(GameMessage gameMessage) {
        return getStringFromObject(gameMessage);
    }

    @Override
    public String getStringFromObject(Object object) {

        String lineSeparator = System.getProperty("line.separator");

        if (object instanceof String) {
            return (String) object;
        } else if (object == null) {
            return  " ";
        } else if (object instanceof PointFilling) {
            PointFilling pointFilling = (PointFilling) object;
            if (!pointFilling.empty()) {
                for (GameObject gameObject: pointFilling.getObjects()) {
                    // TODO: handle sevelar objects on point
                    return getObjectSymbol(gameObject);
                }
            }
            return  " ";
        } else if (object instanceof PlaceMap) {
            PlaceMap placeMap = (PlaceMap) object;
            String resultString = "";
            for (int y = 0; y < gameWorldParams.getDashboardVerticalSize(); y++) {
                for (int x = 0; x < gameWorldParams.getDashboardHorizontalSize(); x++) {
                    PointFilling pointFilling = placeMap.getPointFilling(new Point(x, y));
                    resultString += getStringFromObject(pointFilling);
                }
                resultString += lineSeparator;
            }
            return resultString;
        }
        return  null;
    }

    public String getObjectSymbol(GameObject gameObject) {
        if (gameObject instanceof Wall) {
            return "#";
        } else if (gameObject instanceof Barrier) {
            return "@";
        } else if (gameObject instanceof Bomb) {
            return "*";
        } else if (gameObject instanceof Fire) {
            return "&";
        } else if (gameObject instanceof AIPlayer) {
            return "8";
        } else if (gameObject instanceof Player) {
            return "$";
        }
        return  "?";
    }
}
