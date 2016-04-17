package ua.pp.myshko.bomberman.app;

import ua.pp.myshko.bomberman.server.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author M. Chernenko
 */
@ServerEndpoint(value = "/websocket/bomberman")
public class GameServerEndpoint implements GameEventListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Map<String, Session> playerList = new ConcurrentHashMap<>();
    private GameServer gameServer = new GameServer(this);
    private GameMessageParser gameMessageParser = new GsonGameMessageParser();
    ExecutorService executorService = Executors.newCachedThreadPool();

    @OnOpen
    public void onOpen(Session session) {

        logger.info("Connected ... " + session.getId());
        String playerId = session.getId();
        playerList.put(playerId, session);
    }

    @OnMessage
    public String onMessage(String message, Session session) {

        String playerId = session.getId();
        GameMessage gameMessage;
        try {
            //logger.info("Parsing msg from session " + playerId + ": " + message);
            gameMessage = gameMessageParser.getMessageFromString(message);
        } catch (BombermanException e) {
            logger.log(Level.SEVERE, "Error parsing msg from session " + playerId, e);
            return null;
        }

        if (gameMessage.getCommand() == null) {
            logger.info("Unknown command from " + playerId + " (" + message + ")");
            return "";
        }
        if (gameMessage.getPlayerId() == null) {
            gameMessage.setPlayerId(playerId);
        }
        if (gameMessage.getGameId() == null) {
            gameMessage.setGameId(playerId);
        }
        try {
            gameServer.handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            // TODO: handle it
            e.printStackTrace();
        }

        return "";
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {

        String playerId = session.getId();
        gameServer.handleQuit(playerId);
        playerList.remove(playerId);

        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    @Override
    public void handleEvent(String gameId, String playerId, Object msg) {

        Session session = playerList.get(playerId);

        if (session != null) {
            executorService.execute(() -> sendToClient(session, gameId, playerId, msg));
        }
    }

    protected void sendToClient(Session session, String gameId, String playerId, Object msg) {
        try {

            GameMessage gameMessage = new GameMessage(gameId, playerId);
            gameMessage.setMessage(gameMessageParser.getStringFromObject(msg));
            String stringMessage = gameMessageParser.getStringFromMessage(gameMessage);

            //logger.info("Sending msg to player " + gameMessage.getPlayerId() + ": " + stringMessage);
            session.getBasicRemote().sendText(stringMessage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error sending msg to session " + session.getId(), e);
            CloseReason cr = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, e.getMessage());
            try {
                session.close(cr);
            } catch (IOException exc) {
                // Ignore
            }
        }
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

}
