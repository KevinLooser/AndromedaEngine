package game;

import engine.*;
import engine.graph.*;
import engine.input.MouseInput;
import engine.objects.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static engine.objects.GameObject.LifeState.DEAD;
import static org.lwjgl.glfw.GLFW.*;

public class DemoGame implements IGameLogic {

    private final Renderer renderer;

    private static final float SHIP_ACCELERATION = 0.001f;

    List<GameObject> gameObjects;
    GameObject player;
    ObstacleGroup obstacles;
    MissileGroup missiles;

    private Scene scene;
    private ThirdPersonCamera camera;
    private Hud hud;

    public DemoGame() {
        renderer = new Renderer();
        scene = new Scene();
        camera = new ThirdPersonCamera();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        // load textures and define colours
        float reflectance = 1f;
        Texture texture = new Texture("/resources/textures/asteroid.png");
        Texture block = new Texture("/resources/textures/grassblock.png");
        Material asteroidMaterial = new Material(block, reflectance);
        Material shipMaterial = new Material(texture, reflectance);
        Material missileMaterial = new Material(texture, reflectance);

        // load meshes
        Mesh mesh = OBJLoader.loadMesh("../resources/models/cube.obj");
        mesh.setMaterial(asteroidMaterial);

        Mesh missileMesh = OBJLoader.loadMesh("../resources/models/missile.obj");
        missileMesh.setMaterial(missileMaterial);

        Mesh shipMesh = OBJLoader.loadMesh("../resources/models/ship.obj");
        shipMesh.setMaterial(shipMaterial);

        // skybox
        SkyBox skyBox = new SkyBox("../resources/models/skybox.obj", "/resources/textures/skybox.png");
        skyBox.setScale(50f);
        scene.setSkyBox(skyBox);

        // create game objects
        float cameraDistance = 7f;
        GameObject ship = new Ship(shipMesh, missileMesh, missileMesh, 0, SHIP_ACCELERATION, 1);
        GameObject asteroid = new Obstacle(mesh, 1f);
        asteroid.setDurability(100.0f);
        asteroid.setPosition(-4, 0, -1);
        ship.setPosition(0, 0, -cameraDistance);

        // add game objects
        gameObjects = new ArrayList<>();
        gameObjects.add(ship);
        gameObjects.add(asteroid);
        gameObjects.add(skyBox);
        scene.setGameObjectsMeshes(gameObjects);
        camera.init(ship, cameraDistance);

        // add player
        player = ship;

        // add obstacles
        obstacles = new ObstacleGroup();
        obstacles.add((Obstacle) asteroid);

        // prepare missiles
        missiles = new MissileGroup();

        // lights
        setupLights();

        // create hud
        setupHud();

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        if (window.isKeyPressed(GLFW_KEY_W)) {
            player.accelerate();
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            player.decelerate();
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            ((Ship) player).steer(-1);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            ((Ship) player).steer(1);
        }
        if (window.isKeyPressed(GLFW_KEY_F)) {
            Missile missile = ((Ship) player).shootFrontal();
            gameObjects.add(missile);
            missiles.add(missile);
        }
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            List<Missile> missileListLeft = ((Ship) player).shootLeftSide();
            gameObjects.addAll(missileListLeft);
            missiles.addAll(missileListLeft);
        }
        if (window.isKeyPressed(GLFW_KEY_E)) {
            List<Missile> missileListRight = ((Ship) player).shootRightSide();
            gameObjects.addAll(missileListRight);
            missiles.addAll(missileListRight);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            // TODO: activate boost
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            // TODO: activate gear
        }
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        sceneLight.setAmbientLight(new Vector3f(0.8f, 0.8f, 0.8f));
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(1, 0, 0);
        float lightIntensity = 1.0f;
        // point light
        sceneLight.setPointLightList(new PointLight[0]);
        // spot light
        sceneLight.setSpotLightList(new SpotLight[0]);
        // directional light
        sceneLight.setDirectionalLight(new DirectionalLight(lightColour, lightPosition, lightIntensity));
        scene.setSceneLight(sceneLight);
    }

    private void setupHud() throws Exception {
        hud = new Hud("AndromedaEngine v0.12");
        hud.addStatusText("Asteroid Durability: " + gameObjects.get(1).getDurability());
        hud.getGameObjects().get(1).setPosition(5, 10, 0);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.moveAlong(mouseInput);
        scene.getSkyBox().moveAlong(player.getPosition().x, player.getPosition().y, player.getPosition().z);

        updateAll();
        updateMissiles();

        scene.getGameMeshes().clear();
        scene.setGameObjectsMeshes(gameObjects);
    }

    private void updateAll() {
        for(int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).moveForward();
            if(gameObjects.get(i).getLifeState() == DEAD) {
                gameObjects.remove(i);
                i--;
            }
        }
    }

    private void updateMissiles() {
        for(int j = 0; j < missiles.size(); j++) {
            Missile currentMissile = missiles.get(j);
            if(currentMissile.isExhausted()) {
                currentMissile.destroy();
                missiles.remove(j);
                j--;
            }
            for (int k = 0; k < obstacles.size(); k++) {
                Obstacle currentObstacle = obstacles.get(k);
                if(currentMissile.collides(currentObstacle)) {
                    currentObstacle.dealDamage(currentMissile.getDamage());
                    currentMissile.destroy();
                    missiles.remove(j);
                    j--;
                }
                hud.setStatusText("Asteroid Durability: " + currentObstacle.getDurability(), (TextItem) hud.getGameObjects().get(1));
                if(currentObstacle.getDurability() <= 0) {
                    currentObstacle.destroy();
                    obstacles.remove(k);
                    k--;
                }
            }
        }
    }

    @Override
    public void render(Window window) {
        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<Mesh, List<GameObject>> mapMeshes = scene.getGameMeshes();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.cleanup();
        }
        hud.cleanup();
    }
}
