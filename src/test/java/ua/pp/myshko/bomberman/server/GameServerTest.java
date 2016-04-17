package ua.pp.myshko.bomberman.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.pp.myshko.bomberman.app.BombermanException;
import ua.pp.myshko.bomberman.app.GameEventListener;
import ua.pp.myshko.bomberman.app.GameMessage;

import static org.junit.Assert.*;

/**
 * @author M. Chernenko
 */
public class GameServerTest {

    private static final String PLAYER_ID = "S_ID100";

    private GameEventListener gameEventListener;

    @Before
    public void setUp() throws Exception {

        gameEventListener = Mockito.mock(GameEventListener.class);
    }

    @Test
    public void shouldReturnNull_whenGameDoesNotStarted() throws Exception {

        GameServer gameServer = new GameServer(gameEventListener);
        Game game = gameServer.getGame(PLAYER_ID);
        assertNull(game);
    }

    @Test
    public void shouldReturnNotNull_whenGameStartCommandSent() throws Exception {

        GameServer gameServer = new GameServer(gameEventListener);

        GameMessage gameMessage = new GameMessage(PLAYER_ID, PLAYER_ID);
        gameMessage.setCommand(GameCommands.START);
        try {
            gameServer.handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            Assert.fail("Should not have thrown any exception");
        }
        Game game = gameServer.getGame(PLAYER_ID);
        assertNotNull(game);
    }

    @Test
    public void shouldStopGameOnCommand() throws Exception {

        GameServer gameServer = new GameServer(gameEventListener);

        GameMessage gameMessage = new GameMessage(PLAYER_ID, PLAYER_ID);
        gameMessage.setCommand(GameCommands.START);
        try {
            gameServer.handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            Assert.fail("Should not have thrown any exception");
        }
        Game game = gameServer.getGame(PLAYER_ID);
        assertNotNull(game);

        gameMessage.setCommand(GameCommands.QUIT);
        try {
            gameServer.handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            Assert.fail("Should not have thrown any exception");
        }
        game = gameServer.getGame(PLAYER_ID);
        assertNull(game);


    }

    @Test
    public void shouldTransferMessageToGame() throws Exception {

        Game game = Mockito.mock(Game.class);

        GameServer gameServer = new GameServer(gameEventListener) {
            @Override
            public Game getGame(String gameId) {
                return game;
            }
        };

        GameMessage gameMessage = new GameMessage(PLAYER_ID, PLAYER_ID);
        gameMessage.setCommand(GameCommands.ACT_BOMB);
        try {
            gameServer.handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            Assert.fail("Should not have thrown any exception");
        }

        Mockito.verify(game).processMessage(PLAYER_ID, GameCommands.ACT_BOMB);
    }

    @Test
    public void shouldStopGameOnQuit() throws Exception {

        GameServer gameServer = new GameServer(gameEventListener);

        GameMessage gameMessage = new GameMessage(PLAYER_ID, PLAYER_ID);
        gameMessage.setCommand(GameCommands.START);
        try {
            gameServer.handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            Assert.fail("Should not have thrown any exception");
        }
        Game game = gameServer.getGame(PLAYER_ID);
        assertNotNull(game);

        gameServer.handleQuit(PLAYER_ID);
        game = gameServer.getGame(PLAYER_ID);
        assertNull(game);


    }

}