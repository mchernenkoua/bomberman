package ua.pp.myshko.bomberman.server.objects;

import ua.pp.myshko.bomberman.app.GameEventListener;
import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.GameWorldParams;

/**
 * @author M. Chernenko
 */
public class AIPlayer extends Player implements GameEventListener {

    private static final String TYPE = "AIPlayer";
    public AIPlayer(String playerId, GameObjectsEventListener eventListener, GameWorldParams gameWorldParams) {
        super(playerId, eventListener, gameWorldParams);
    }

    @Override
    public void handleEvent(String gameId, String playerID, Object msg) {

    }

    @Override
    public String getType() {
        return TYPE;
    }
}
