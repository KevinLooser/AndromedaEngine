package engine;

import engine.objects.GameObject;

public interface IHud {

    GameObject[] getGameItems();

    default void cleanup() {
        GameObject[] gameItems = getGameItems();
        for (GameObject gameObject : gameItems) {
            gameObject.getMesh().cleanup();
        }
    }
}