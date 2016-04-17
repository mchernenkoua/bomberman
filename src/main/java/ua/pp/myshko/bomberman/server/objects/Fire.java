package ua.pp.myshko.bomberman.server.objects;

import ua.pp.myshko.bomberman.server.GameWorldParams;
import ua.pp.myshko.bomberman.server.objects.interfaces.Dependent;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.abstractions.AbstractGameObject;
import ua.pp.myshko.bomberman.server.objects.abstractions.DependentBehavior;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;

/**
 * @author M. Chernenko
 */
public class Fire extends AbstractGameObject implements GameObject, Dependent {

    private static final String TYPE = "Fire";

    private Dependent dependentBehavior;
    private transient GameObjectsEventListener eventListener;

    public Fire(Point point, Identifiable owner, GameObjectsEventListener eventListener, GameWorldParams gameWorldParams) {

        super(point, TYPE);
        this.dependentBehavior = new DependentBehavior(owner);
        this.eventListener = eventListener;
        init(GameObjectState.LIVE, gameWorldParams.getFireLiveTime());
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
}
