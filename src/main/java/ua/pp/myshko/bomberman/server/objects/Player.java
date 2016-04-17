package ua.pp.myshko.bomberman.server.objects;

import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.GameWorldParams;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.abstractions.AbstractGameObject;
import ua.pp.myshko.bomberman.server.objects.abstractions.BombRepository;
import ua.pp.myshko.bomberman.server.objects.abstractions.LimitedMoveBehavior;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;
import ua.pp.myshko.bomberman.server.objects.interfaces.Movable;
import ua.pp.myshko.bomberman.server.objects.interfaces.WeaponRepository;

/**
 * @author M. Chernenko
 */
public class Player implements GameObject, Movable, Identifiable {

    private static final String TYPE = "Player";

    private String id;
    private int score = 0;

    private transient GameObjectsEventListener eventListener;
    private GameObject gameObjectBehavior;
    private Movable moveBehavior;
    private WeaponRepository weaponRepository;
    private final GameWorldParams gameWorldParams;

    public Player(String playerId, GameObjectsEventListener eventListener, GameWorldParams gameWorldParams) {
        this.id = playerId;
        this.gameWorldParams = gameWorldParams;
        this.gameObjectBehavior = new AbstractGameObject(new Point(0, 0), TYPE) {
            @Override
            protected void stateChanged() {
                Player.this.stateChanged();
            }

            @Override
            protected GameObjectState getNextState() {
                if (getState() == GameObjectState.LIVE) {
                    return GameObjectState.DEAD;
                } else {
                    return GameObjectState.REMOVE;
                }
            }

            @Override
            public void destroy() {
                super.destroy();
                if (getState().equals(GameObjectState.DEAD)) {
                    Player.this.init(GameObjectState.DEAD, gameWorldParams.getPlayerDeadTime());
                } else if (getState().equals(GameObjectState.REMOVE)) {
                    Player.this.init(GameObjectState.LIVE, 0);
                }
            }
        };
        this.eventListener = eventListener;
        this.moveBehavior = new LimitedMoveBehavior(this);
        init(GameObjectState.LIVE, 0);
    }

    @Override
    public void init(GameObjectState state, int liveTime) {
        gameObjectBehavior.init(state, liveTime);

        if (weaponRepository != null) {
            weaponRepository.destroy();
        }
        if (state == GameObjectState.LIVE) {
            this.weaponRepository = new BombRepository(this, eventListener, gameWorldParams);
        }
    }

    @Override
    public String getType() {
        return gameObjectBehavior.getType();
    }

    @Override
    public Point getPoint() {
        return gameObjectBehavior.getPoint();
    }

    @Override
    public void setPoint(Point point) {
        gameObjectBehavior.setPoint(point);
    }

    @Override
    public GameObjectState getState() {
        return gameObjectBehavior.getState();
    }

    protected void stateChanged() {
        //destroy();
        eventListener.handleObjectStateChanging(this);
    }

    @Override
    public void destroy() {
        gameObjectBehavior.destroy();
    }

    public String getId() {
        return id;
    }


    @Override
    public boolean canMove() {
        return moveBehavior.canMove();
    }

    @Override
    public void move(Point point) {
        moveBehavior.move(point);
    }

    public GameObject useWeapon() {
        return weaponRepository.useWeapon();
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public int getBombCount() {
        return weaponRepository.getCount();
    }

    public int getScore() {
        return score;
    }

    public void increaseBombCount() {
        weaponRepository.increaseCount();
    }
}
