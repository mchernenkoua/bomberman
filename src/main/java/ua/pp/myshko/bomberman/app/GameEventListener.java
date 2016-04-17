package ua.pp.myshko.bomberman.app;


/**
 * @author M. Chernenko
 */
public interface GameEventListener {

    void handleEvent(String gameId, String id, Object gameObjects);
}
