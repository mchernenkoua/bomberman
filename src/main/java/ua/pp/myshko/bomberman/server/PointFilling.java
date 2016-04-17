package ua.pp.myshko.bomberman.server;

import ua.pp.myshko.bomberman.server.objects.interfaces.Blockable;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author M. Chernenko
 */
public class PointFilling implements Serializable {

    private transient final GameObjectsEventListener eventListener;
    private boolean fillingAllowed = true;
    private Set<GameObject> objects = new HashSet<>();

    public PointFilling(GameObjectsEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void add(GameObject gameObject) {
        objects.add(gameObject);
        checkFillAllowing();
        Set<GameObject> currentObjects = new HashSet<>();
        currentObjects.addAll(objects);
        eventListener.handleObjectsAdding(gameObject, currentObjects);
    }

    public boolean empty() {
        return objects == null
                || objects.size() == 0;
    }

    public boolean isFillingAllowed() {
        return fillingAllowed;
    }

    public void remove(GameObject gameObject) {
        objects.remove(gameObject);
        checkFillAllowing();
        eventListener.handlePointChanging(gameObject.getPoint());
    }

    private void checkFillAllowing() {
        fillingAllowed = true;
        for (GameObject otherGameObject: objects) {
            if (otherGameObject instanceof Blockable) {
                fillingAllowed = false;
                break;
            }
        }
    }

    public boolean containObject(Class clazz) {
        for (GameObject gameObject: objects) {
            if (clazz.isInstance(gameObject)) {
                return true;
            }
        }
        return false;
    }

    public Set<GameObject> getObjects() {
        return objects;
    }

    public void clear() {
        objects.forEach(GameObject::destroy);
        objects.clear();
    }
}
