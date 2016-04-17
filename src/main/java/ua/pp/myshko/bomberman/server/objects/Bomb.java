package ua.pp.myshko.bomberman.server.objects;

import ua.pp.myshko.bomberman.server.GameWorldParams;
import ua.pp.myshko.bomberman.server.objects.abstractions.DependentBehavior;
import ua.pp.myshko.bomberman.server.objects.interfaces.Dependent;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.abstractions.AbstractGameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;

/**
 * @author M. Chernenko
 */
public class Bomb extends AbstractGameObject implements GameObject, Dependent {

    private static final String TYPE = "Bomb";
    private final GameWorldParams gameWorldParams;

    private Dependent dependentBehavior;
    private transient GameObjectsEventListener eventListener;

    public Bomb(Point point, Identifiable owner, GameObjectsEventListener eventListener, GameWorldParams gameWorldParams) {

        super(point, TYPE);
        this.dependentBehavior = new DependentBehavior(owner);
        this.eventListener = eventListener;
        this.gameWorldParams = gameWorldParams;
        init(GameObjectState.LIVE, gameWorldParams.getBombLiveTime());
    }

    @Override
    protected void stateChanged() {
        eventListener.handleObjectStateChanging(this);
    }

    @Override
    public Identifiable getOwner() {
        return dependentBehavior.getOwner();
    }

    @Override
    public void setOwner(Identifiable gameObject) {
        dependentBehavior.setOwner(gameObject);
    }

    @Override
    protected GameObjectState getNextState() {
        if (getState().equals(GameObjectState.LIVE)) {
            return GameObjectState.DEAD;
        } else {
            return GameObjectState.REMOVE;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (getState().equals(GameObjectState.DEAD)) {
            init(GameObjectState.DEAD, gameWorldParams.getBombDeadTime());
        }
    }
}
