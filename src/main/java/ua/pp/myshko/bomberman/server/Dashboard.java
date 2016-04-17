package ua.pp.myshko.bomberman.server;

import ua.pp.myshko.bomberman.server.objects.Barrier;
import ua.pp.myshko.bomberman.server.objects.Wall;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Movable;

import java.util.*;

/**
 * @author M. Chernenko
 */
public class Dashboard {

    private final PlaceMap placeMap;
    private final GameWorldParams gameWorldParams;
    private GameObjectsEventListener eventListener;

    public Dashboard(GameObjectsEventListener eventListener, GameWorldParams gameWorldParams) {
        this.eventListener = eventListener;
        this.gameWorldParams = gameWorldParams;
        this.placeMap = new PlaceMap(eventListener);
    }

    public void generateMap(Random random) {
        placeMap.clear();
        int vSize = gameWorldParams.getDashboardVerticalSize();
        int hSize = gameWorldParams.getDashboardHorizontalSize();
        // walls
        for (int y = 1; y < vSize; y+=2) {
            for (int x = 1; x < hSize; x+=2) {
                Point point = new Point(x, y);
                Wall wall = new Wall(point);
                addGameObject(point, wall);
            }
        }
        // barriers
        for (int y = 0; y < vSize; y++) {
            int barriersCount = random.nextInt(gameWorldParams.getMaxBarriersCountPerLine() + 1);
            for (int i = 0; i < barriersCount; i++) {
                int x = random.nextInt(hSize);
                Point point = new Point(x, y);
                if (isPointEmpty(point) && !isCornerPoint(point)) {
                    Barrier barrier = new Barrier(point, eventListener);
                    addGameObject(point, barrier);
                }
            }
        }
    }

    public void generateMap() {
        Random random = new Random(System.currentTimeMillis());
        random = new Random(random.nextInt());
        generateMap(random);
    }

    private boolean isCornerPoint(Point point) {
        int vSize = gameWorldParams.getDashboardVerticalSize();
        int hSize = gameWorldParams.getDashboardHorizontalSize();
        return  point.getX() == 0         && point.getY() == 0
             || point.getX() == 0         && point.getY() == vSize - 1
             || point.getX() == hSize - 1 && point.getY() == 0
             || point.getX() == hSize - 1 && point.getY() == vSize - 1
        ;
    }

    private boolean isPointValid(Point point) {
        int vSize = gameWorldParams.getDashboardVerticalSize();
        int hSize = gameWorldParams.getDashboardHorizontalSize();
        return point.getX() >= 0 && point.getX() < hSize
                && point.getY() >= 0 && point.getY() < vSize;
    }

    public Point getEmptyCornerPoint() {
        int vSize = gameWorldParams.getDashboardVerticalSize();
        int hSize = gameWorldParams.getDashboardHorizontalSize();
        Point point;
        point = new Point(0, 0);
        if (isPointEmpty(point)) {
            return point;
        }
        point = new Point(hSize - 1, 0);
        if (isPointEmpty(point)) {
            return point;
        }
        point = new Point(0, vSize - 1);
        if (isPointEmpty(point)) {
            return point;
        }
        point = new Point(hSize - 1, vSize - 1);
        if (isPointEmpty(point)) {
            return point;
        }
        return null;
    }

    public boolean isPointEmpty(Point point) {
        return isPointValid(point) && placeMap.isPointEmpty(point);
    }

    public void moveGameObject(GameObject gameObject, int xDelta, int yDelta) {
        if (gameObject instanceof Movable) {
            Movable movableGameObject = (Movable) gameObject;
            if (movableGameObject.canMove()) {
                Point gameObjectPoint = gameObject.getPoint();
                Point newPoint = new Point(gameObjectPoint.getX() + xDelta, gameObjectPoint.getY() + yDelta);
                if (isPointValid(newPoint)) {
                    synchronized (placeMap) {
                        if (placeMap.isMoveAllowed(newPoint)) {
                            placeMap.removeGameObject(gameObjectPoint, gameObject);
                            movableGameObject.move(newPoint);
                            placeMap.addGameObject(newPoint, gameObject);
                        }
                    }
                }
            }
        }
    }

    public PlaceMap getChangedLocations(Point ... points) {
        return placeMap.getChangedLocations(points);
    }

    public Point addGameObject(Point point, GameObject gameObject) {

        if (!isPointValid(point) || isPointHasObject(point, Wall.class)) {
            return null;
        }

        placeMap.addGameObject(point, gameObject);
        return point;
    }

    private boolean isPointHasObject(Point point, Class clazz) {
        return placeMap.isPointHasObject(point, clazz);
    }

    public void removeGameObject(GameObject gameObject) {
        placeMap.removeGameObject(gameObject.getPoint(), gameObject);
    }

    public PlaceMap getMap() {
        return getChangedLocations(placeMap.allPoints());
    }

    public void destroy() {
        placeMap.clear();
    }
}
