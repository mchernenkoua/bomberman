package ua.pp.myshko.bomberman.server.objects.abstractions;

import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.GameWorldParams;
import ua.pp.myshko.bomberman.server.objects.Bomb;
import ua.pp.myshko.bomberman.server.objects.interfaces.Dependent;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;
import ua.pp.myshko.bomberman.server.objects.interfaces.WeaponRepository;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author M. Chernenko
 */
public class BombRepository implements WeaponRepository, Dependent {

    private final GameWorldParams gameWorldParams;
    private int bombCount;
    private transient Timer bombTimer;
    private transient Identifiable owner;
    private transient GameObjectsEventListener eventListener;
    private final transient Object lock = new Object();

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            BombRepository.this.increaseCount();
            eventListener.handleObjectStateChanging(owner);
        }
    };

    public BombRepository(Identifiable owner, GameObjectsEventListener eventListener, GameWorldParams gameWorldParams) {
        this.owner = owner;
        this.eventListener = eventListener;
        this.gameWorldParams = gameWorldParams;
        init();
    }

    protected void init() {

        this.bombCount = gameWorldParams.getInitialBombCount();

        if (bombTimer != null) {
            bombTimer.cancel();
        } else {
            bombTimer = new Timer();
        }
        bombTimer.schedule(timerTask, gameWorldParams.getBombIncreasingInterval(),
                                        gameWorldParams.getBombIncreasingInterval());
    }

    @Override
    public GameObject useWeapon() {
        Bomb bomb = null;
        synchronized (lock) {
            if (bombCount > 0) {
                decreaseCount();
                bomb = new Bomb(getOwner().getPoint(), getOwner(), eventListener, gameWorldParams);
            }
        }
        return bomb;
    }

    @Override
    public void destroy() {
        if (bombTimer != null) {
            bombTimer.cancel();
        }
    }

    @Override
    public int getCount() {
        return bombCount;
    }

    @Override
    public void increaseCount() {
        synchronized (lock) {
            bombCount++;
        }
    }

    public void decreaseCount() {
        synchronized (lock) {
            bombCount--;
        }
    }

    @Override
    public Identifiable getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Identifiable gameObject) {
        this.owner = gameObject;
    }
}
