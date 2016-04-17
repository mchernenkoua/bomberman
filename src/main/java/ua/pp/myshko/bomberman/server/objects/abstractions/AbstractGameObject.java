package ua.pp.myshko.bomberman.server.objects.abstractions;

import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.GameObjectState;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author M. Chernenko
 */
public abstract class AbstractGameObject implements GameObject {

    protected final String type;
    protected Point point;
    protected GameObjectState state;
    protected transient Timer liveTimer;

    protected AbstractGameObject(Point point, String type) {
        this.type = type;
        this.point = point;
    }

    @Override
    public void init(GameObjectState state, int liveTime) {

        this.state = state;
        if (liveTimer != null) {
            liveTimer.cancel();
        }

        if (liveTime != 0) {
            liveTimer = new Timer();
            liveTimer.schedule(new TimerTask() {

                private AbstractGameObject gameObject = AbstractGameObject.this;

                @Override
                public void run() {
                    gameObject.destroy();
                    gameObject.stateChanged();
                }
            }, liveTime, liveTime);
        }
    }

    abstract protected void stateChanged();

    @Override
    public void setPoint(Point point) {
         this.point = point;
    }

    @Override
    public Point getPoint() {
        return point;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public GameObjectState getState() {
        return state;
    }

    @Override
    public void destroy() {
        if (liveTimer != null) {
            liveTimer.cancel();
        }
        this.state = getNextState();
    }

    protected GameObjectState getNextState() {
        return GameObjectState.REMOVE;
    }
}
