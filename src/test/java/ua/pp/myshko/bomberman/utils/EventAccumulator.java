package ua.pp.myshko.bomberman.utils;

import ua.pp.myshko.bomberman.app.GameEventListener;
import ua.pp.myshko.bomberman.server.GameObjectsEventListener;
import ua.pp.myshko.bomberman.server.Point;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author M. Chernenko
 */
public abstract class EventAccumulator<T> implements GameObjectsEventListener, GameEventListener {

    private List<T> result = new CopyOnWriteArrayList<>();

    public abstract T convertObject(Object gameObject);

    public List<T> getResult() {
        return result;
    }

    @Override
    public void handlePointChanging(Point point) {

    }

    @Override
    public void handleObjectStateChanging(GameObject... gameObjects) {
        for (GameObject gameObject: gameObjects) {
            result.add(convertObject(gameObject));
        }
    }

    @Override
    public void handleObjectsAdding(GameObject addedGameObject, Set<GameObject> gameObjectsOnPoint) {

    }

    @Override
    public void handleEvent(String gameId, String id, Object gameObjects) {
        result.add(convertObject(gameObjects));
    }
}
