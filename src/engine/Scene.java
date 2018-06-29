package engine;

import engine.graph.Mesh;
import engine.objects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private Map<Mesh, List<GameObject>> meshMap;
    private List<GameObject> gameObjects;
    private SkyBox skyBox;
    private SceneLight sceneLight;

    public Scene() {
        meshMap = new HashMap<>();
    }

    public Map<Mesh, List<GameObject>> getGameMeshes() {
        return meshMap;
    }

    public void setGameObjectsMeshes(List<GameObject> gameObjects) {
        for (GameObject gameObject : gameObjects) {
            Mesh mesh = gameObject.getMesh();
            List<GameObject> list = meshMap.get(mesh);
            if(list == null) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameObject);
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
}
