package ua.pp.myshko.bomberman.server;

import ua.pp.myshko.bomberman.app.BombermanException;
import ua.pp.myshko.bomberman.app.GameEventListener;
import ua.pp.myshko.bomberman.app.GameMessage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author M. Chernenko
 */
public class GameServer {

    private final Map<String, Game> gameList = new ConcurrentHashMap<>();
    private final GameEventListener gameEventListener;

    public GameServer(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }

    private Game startGame(String gameId) throws BombermanException {

        Game game;
        try {
            game = new Game(gameId, gameEventListener, new GameWorldParams());
        } catch (IOException e) {
            throw new BombermanException(e);
        }
        gameList.put(gameId, game);
        return game;
    }

    public Game getGame(String gameId) {
        return gameList.get(gameId);
    }

    public void handlePlayerMessage(GameMessage gameMessage) throws BombermanException {

        Game game;

        switch (gameMessage.getCommand()) {
            case START:
                synchronized (gameList) {
                    game = getGame(gameMessage.getGameId());
                    if (game == null) {
                        try {
                            game = startGame(gameMessage.getGameId());
                        } catch (BombermanException e) {
                            throw new BombermanException(e);
                        }
                        game.addPlayer(gameMessage.getPlayerId());
                        game.addAIPlayers();
                    }
                }
                break;
            case QUIT:
                synchronized (gameList) {
                    game = getGame(gameMessage.getGameId());
                    handleQuit(gameMessage.getPlayerId(), game);
                }
                break;
            default:
                game = getGame(gameMessage.getGameId());
                if (game != null) {
                    game.processMessage(gameMessage.getPlayerId(), gameMessage.getCommand());
                }
                break;
        }
    }

    public void handleQuit(String playerId) {
        Game game =  getGame(playerId);
        if (game != null) {
            handleQuit(playerId, game);
        }
    }

    private void handleQuit(String playerId, Game game) {

        game.playerQuit(playerId);
        if (!game.hasRealPlayers()) {
            game.close();
            gameList.remove(playerId);
        }
    }
}
