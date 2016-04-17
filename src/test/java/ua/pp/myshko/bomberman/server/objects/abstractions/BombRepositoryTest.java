package ua.pp.myshko.bomberman.server.objects.abstractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.GameWorldParams;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author M. Chernenko
 */
public class BombRepositoryTest {

    private GameWorldParams gameWorldParams;
    private GameObjectsEventListener gameObjectsEventListener;
    private BombRepository bombRepository;

    public BombRepositoryTest() throws IOException {
        gameWorldParams = new GameWorldParams();
    }

    @Before
    public void setUp() throws Exception {

        Identifiable gameObject = Mockito.mock(Identifiable.class);
        gameObjectsEventListener = Mockito.mock(GameObjectsEventListener.class);
        bombRepository = new BombRepository(gameObject, gameObjectsEventListener, gameWorldParams);

    }

    @Test
    public void shouldIncreaseBombCount() throws Exception {

        ArgumentCaptor<Identifiable> argument = ArgumentCaptor.forClass(Identifiable.class);
        assertEquals(1, bombRepository.getCount());

        Thread.sleep(gameWorldParams.getBombIncreasingInterval());
        /*// wait for end task
        long currentTime = System.currentTimeMillis();
        while (bombRepository.getNextIncreaseTime() < currentTime) {
            System.out.println(" " + bombRepository.getNextIncreaseTime() + " vs " + currentTime);
            Thread.sleep(1);
        }*/

        assertEquals(2, bombRepository.getCount());
        Thread.sleep(10);// TODO: why it is happens?.. why is it need?..
        Mockito.verify(gameObjectsEventListener).handleObjectStateChanging(argument.capture());
        assertEquals(bombRepository.getOwner(), argument.getValue());

    }

    @Test
    public void shouldDecreaseBombCount_whenUseWeapon() throws Exception {

        assertEquals(1, bombRepository.getCount());

        GameObject bomb = bombRepository.useWeapon();

        assertEquals(0, bombRepository.getCount());
        assertNotNull(bomb);
    }

    @Test
    public void shouldStopIncreasingBombCount_whenDestroy() throws Exception {

        assertEquals(1, bombRepository.getCount());

        bombRepository.destroy();

        Thread.sleep(gameWorldParams.getBombIncreasingInterval());
        Thread.sleep(gameWorldParams.getBombIncreasingInterval());

        assertEquals(1, bombRepository.getCount());
        Mockito.verify(gameObjectsEventListener, Mockito.never()).handleObjectStateChanging(Mockito.any());

    }
}