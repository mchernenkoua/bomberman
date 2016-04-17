package ua.pp.myshko.bomberman.server.objects.abstractions;

import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.GameObjectState;
import ua.pp.myshko.bomberman.server.objects.interfaces.Movable;

/**
 * @author M. Chernenko
 */
public class LimitedMoveBehavior implements Movable {

    private static final int MOVE_DELAY = 250; // ~ max speed == 4p/s

    private long lastMove;
    private transient GameObject gameObject;

    public LimitedMoveBehavior(GameObject gameObject) {
        this.gameObject = gameObject;
    }
    @Override
    public boolean canMove() {
        return gameObject.getState() == GameObjectState.LIVE
                && (lastMove + MOVE_DELAY <= System.currentTimeMillis());
    }

    @Override
    public void move(Point point) {
        lastMove = System.currentTimeMillis();
        gameObject.setPoint(point);
    }

    @Override
    public Point getPoint() {
        return gameObject.getPoint();
    }
}
