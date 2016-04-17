package ua.pp.myshko.bomberman.server;

import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author M. Chernenko
 */
public class PlaceMap implements Serializable {

    private transient GameObjectsEventListener eventListener;
    private Map<Point, PointFilling> objects = new HashMap<>();

    public PlaceMap(GameObjectsEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void addGameObject(Point point, GameObject gameObject) {

        PointFilling gameObjects = objects.get(point);
        if (gameObjects == null) {
            gameObjects = new PointFilling(eventListener);
            objects.put(point, gameObjects);
        }
        gameObject.setPoint(point);
        gameObjects.add(gameObject);
    }

    public void addGameObject(Point point, PointFilling pointFilling) {
        objects.put(point, pointFilling);
    }

    public boolean isPointEmpty(Point point) {
        return objects.get(point) == null
                || objects.get(point).empty();
    }

    public boolean isMoveAllowed(Point point) {
        return isPointEmpty(point)
                || objects.get(point).isFillingAllowed();
    }

    public void removeGameObject(Point gameObjectPoint, GameObject gameObject) {
        PointFilling gameObjects = objects.get(gameObjectPoint);
        if (gameObjects != null) {
            gameObjects.remove(gameObject);
        }
    }

    public PlaceMap getChangedLocations(Point... points) {
        PlaceMap changedObjects = new PlaceMap(null);
        for(Point point: points ) {
            changedObjects.addGameObject(point, objects.get(point));
        }
        return changedObjects;
    }

    public void clear() {
        for(Point point: objects.keySet() ) {
            objects.get(point).clear();
        }
        objects.clear();
    }

    public boolean isPointHasObject(Point point, Class clazz) {
        return objects.get(point) != null
                && objects.get(point).containObject(clazz);
    }

    public Point[] allPoints() {
        Point[] points = new Point[objects.keySet().size()];
        return objects.keySet().toArray(points);
    }

    public PointFilling getPointFilling(Point point) {
        return objects.get(point);
    }
}
