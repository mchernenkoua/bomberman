package ua.pp.myshko.bomberman.server.objects;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.pp.myshko.bomberman.server.GameWorldParams;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;
import ua.pp.myshko.bomberman.utils.EventAccumulator;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author M. Chernenko
 */
public class BombTest {

    private EventAccumulator<GameObjectState> gameObjectsEventListener;
    private GameWorldParams gameWorldParams;

    public BombTest() throws IOException {
        gameWorldParams = new GameWorldParams();
    }

    @Before
    public void setUp() throws Exception {

        gameObjectsEventListener = new EventAccumulator<GameObjectState>() {
            @Override
            public GameObjectState convertObject(Object gameObject) {
                return ((GameObject) gameObject).getState();
            }
        };
    }

    @Test
    public void shouldChangeState() throws Exception {

        Identifiable gameObject = Mockito.mock(Identifiable.class);

        Bomb bomb = new Bomb(new Point(0,0), gameObject, gameObjectsEventListener, gameWorldParams);
        assertEquals(GameObjectState.LIVE, bomb.getState());

        List<GameObjectState> result = gameObjectsEventListener.getResult();

        Thread.sleep(gameWorldParams.getBombLiveTime(), 999999);

        assertEquals(1, result.size());
        assertEquals(GameObjectState.DEAD, result.get(0));

        Thread.sleep(gameWorldParams.getBombDeadTime(), 999999);

        assertEquals(2, result.size());
        assertEquals(GameObjectState.REMOVE, result.get(1));

    }

}