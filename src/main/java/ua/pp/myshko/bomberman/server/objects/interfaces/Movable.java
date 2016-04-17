package ua.pp.myshko.bomberman.server.objects.interfaces;

import ua.pp.myshko.bomberman.server.Point;

/**
 * @author M. Chernenko
 */
public interface Movable {

    boolean canMove();
    void move(Point point);
    Point getPoint();
}
