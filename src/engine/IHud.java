package engine;

import engine.objects.GameObject;

import java.util.List;

public interface IHud {

    List<GameObject> getGameObjects();

    default void cleanup() {
        List<GameObject> gameObjects = getGameObjects();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanup();
        }
    }
}