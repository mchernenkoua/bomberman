package ua.pp.myshko.bomberman.server.objects;

import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.objects.interfaces.Blockable;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.abstractions.AbstractGameObject;

/**
 * @author M. Chernenko
 */
public class Barrier extends AbstractGameObject implements GameObject, Blockable {

    private static final String TYPE = "Barrier";
    private GameObjectsEventListener eventListener;

    public Barrier(Point point, GameObjectsEventListener eventListener) {
        super(point, TYPE);
        this.eventListener = eventListener;
        init(GameObjectState.LIVE, 0);
    }

    @Override
    protected void stateChanged() {
        eventListener.handleObjectStateChanging(this);
    }
}
