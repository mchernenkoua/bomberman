package ua.pp.myshko.bomberman.server;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.pp.myshko.bomberman.app.*;
import ua.pp.myshko.bomberman.server.objects.Fire;
import ua.pp.myshko.bomberman.server.objects.GameObjectState;
import ua.pp.myshko.bomberman.server.objects.Player;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;
import ua.pp.myshko.bomberman.utils.EventAccumulator;
import ua.pp.myshko.bomberman.utils.PlainSymbolMessageParser;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author M. Chernenko
 */
public class GameTest {

    private static final String PLAYER_ID = "S_ID100";

    private EventAccumulator<String> gameEventListener;
    private GameMessageParser parser;
    private GameWorldParams gameWorldParams;
    private Random random;

    public GameTest() throws IOException {
        gameWorldParams = new GameWorldParams();
        parser = new PlainSymbolMessageParser(gameWorldParams);
    }

    @Before
    public void setUp() throws Exception {

        random = Mockito.mock(Random.class);
        Mockito.when(random.nextInt(Mockito.anyInt())).thenReturn(1, 2, 0);

        gameEventListener = new EventAccumulator<String>() {
            @Override
            public String convertObject(Object gameObject) {
                return parser.getStringFromObject(gameObject);
            }
        };

    }

    @Test
    public void shouldSendGeneratedMap() throws Exception {

        Game game = new Game(PLAYER_ID, gameEventListener, gameWorldParams) {

            @Override
            protected void initGame() {
                dashboard.generateMap(random);
                initResponseTimer();
            }
        };
        game.addPlayer(PLAYER_ID);

        String lineSeparator = System.getProperty("line.separator");
        List<String> result = gameEventListener.getResult();

        assertEquals(1, result.size());

        assertEquals(   "$ @          " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator
                , result.get(0));
    }

    @Test
    public void shouldMovePlayerRight() throws Exception {

        Game game = new Game(PLAYER_ID, gameEventListener, gameWorldParams) {

            @Override
            protected void initGame() {
                dashboard.generateMap(random);
                initResponseTimer();
            }

            @Override
            protected void notifyAboutChanges(PlaceMap gameObjects) {
                gameEventListener.handleEvent(PLAYER_ID, PLAYER_ID, gameObjects);
            }
        };
        game.addPlayer(PLAYER_ID);
        game.processMessage(PLAYER_ID, GameCommands.ACT_RIGHT);

        Thread.sleep(gameWorldParams.getGameResponseDelay());

        String lineSeparator = System.getProperty("line.separator");
        List<String> result = gameEventListener.getResult();

        assertEquals(2, result.size());

        assertEquals(   " $@          " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator
                        , result.get(1));
    }

    @Test
    public void shouldRemoveBarrier_whenFireAdded() throws Exception {

        Game game = new Game(PLAYER_ID, gameEventListener, gameWorldParams) {

            @Override
            protected void initGame() {
                dashboard.generateMap(random);

                Identifiable player = Mockito.mock(Player.class);
                Mockito.when(player.getState()).thenReturn(GameObjectState.LIVE);
                Mockito.when(player.getPoint()).thenReturn(new Point(0, 0));

                Point point = new Point(2, 0);
                dashboard.addGameObject(point, new Fire(point, player, this, gameWorldParams));

                initResponseTimer();
            }

            @Override
            protected void notifyAboutChanges(PlaceMap gameObjects) {
                gameEventListener.handleEvent(PLAYER_ID, PLAYER_ID, gameObjects);
            }
        };

        String lineSeparator = System.getProperty("line.separator");
        List<String> result = gameEventListener.getResult();

        Thread.sleep(gameWorldParams.getFireLiveTime());
        Thread.sleep(gameWorldParams.getGameResponseDelay());

        assertEquals(2, result.size());

        assertEquals(   "  &          " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator +
                        " # # # # # # " + lineSeparator +
                        "             " + lineSeparator
                        , result.get(0));

        assertEquals(   "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator +
                        "             " + lineSeparator
                        , result.get(1));
    }
}