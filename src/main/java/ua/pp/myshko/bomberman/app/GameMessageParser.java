package ua.pp.myshko.bomberman.app;

/**
 * @author M. Chernenko
 */
public interface GameMessageParser {
    GameMessage getMessageFromString(String message) throws BombermanException;

    String getStringFromMessage(GameMessage gameMessage);

    String getStringFromObject(Object object);
}
