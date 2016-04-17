package ua.pp.myshko.bomberman.server.objects.interfaces;

import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.GameObjectState;

import java.io.Serializable;

/**
 * @author M. Chernenko
 */
public interface GameObject extends Serializable {

    void init(GameObjectState state, int liveTime);
    String getType();
    Point getPoint();
    void setPoint(Point point);
    GameObjectState getState();
    void destroy();
}
