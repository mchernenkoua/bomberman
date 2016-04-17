package ua.pp.myshko.bomberman.server.objects;

import ua.pp.myshko.bomberman.server.objects.interfaces.Blockable;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.abstractions.AbstractGameObject;

/**
 * @author M. Chernenko
 */
public class Wall extends AbstractGameObject implements GameObject, Blockable {

    private static final String TYPE = "Wall";

    public Wall(Point point) {
        super(point, TYPE);
        init(GameObjectState.LIVE, 0);
    }

    @Override
    protected void stateChanged() {

    }
}
