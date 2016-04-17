package ua.pp.myshko.bomberman.server;

import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;

import java.util.Set;

/**
 * @author M. Chernenko
 */
public interface GameObjectsEventListener {
    void handlePointChanging(Point point);
    void handleObjectStateChanging(GameObject... gameObjects);
    void handleObjectsAdding(GameObject addedGameObject, Set<GameObject> gameObjectsOnPoint);

}
