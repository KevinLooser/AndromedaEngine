package game;

import engine.*;
import engine.graph.*;
import engine.input.MouseInput;
import engine.objects.GameObject;
import engine.objects.Missile;
import engine.objects.Obstacle;
import engine.objects.Ship;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static engine.objects.GameObject.LifeState.DEAD;
import static org.lwjgl.glfw.GLFW.*;

public class DemoGame implements IGameLogic {

    private final Renderer renderer;
    private List<GameObject> gameObjects;
    private ThirdPersonCamera camera;
    private static final float SHIP_ACCELERATION = 0.001f;
    private SceneLight sceneLight;
    private Hud hud;

    public DemoGame() {
        renderer = new Renderer();
        camera = new ThirdPersonCamera();
        gameObjects = new ArrayList<>();
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
        //mesh.setTexture(texture);
        mesh.setMaterial(asteroidMaterial);

        Mesh missileMesh = OBJLoader.loadMesh("../resources/models/missile.obj");
        missileMesh.setMaterial(missileMaterial);

        Mesh shipMesh = OBJLoader.loadMesh("../resources/models/ship.obj");
        shipMesh.setMaterial(shipMaterial);

        // create game objects
        float cameraDistance = 7f;
        GameObject ship = new Ship(shipMesh, missileMesh, missileMesh, 0, SHIP_ACCELERATION, 1);
        GameObject asteroid = new Obstacle(mesh, 1f);
        GameObject plane = new Obstacle(mesh, 1f);
        asteroid.setPosition(-4, 0, -1);
        plane.setPosition(0, -5, 0);
        ship.setPosition(0, 0, -cameraDistance);

        // add game objects
        gameObjects.add(ship);
        gameObjects.add(asteroid);
        gameObjects.add(plane);
        camera.init(ship, cameraDistance);

        // lights
        sceneLight = new SceneLight();
        sceneLight.setAmbientLight(new Vector3f(0.8f, 0.8f, 0.8f));
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        // point light
        sceneLight.setPointLightList(new PointLight[0]);
        // spot light
        sceneLight.setSpotLightList(new SpotLight[0]);
        // directional light
        sceneLight.setDirectionalLight(new DirectionalLight(lightColour, lightPosition, lightIntensity));

        // create hud
        hud = new Hud("AndromedaEngine");
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        if (window.isKeyPressed(GLFW_KEY_W)) {
            gameObjects.get(0).accelerate();
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            gameObjects.get(0).decelerate();
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            ((Ship) gameObjects.get(0)).steer(-1);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            ((Ship) gameObjects.get(0)).steer(1);
        }
        if (window.isKeyPressed(GLFW_KEY_F)) {
            gameObjects.add(((Ship) gameObjects.get(0)).shootFrontal());
        }
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            gameObjects.addAll(((Ship) gameObjects.get(0)).shootLeftSide());
        }
        if (window.isKeyPressed(GLFW_KEY_E)) {
            gameObjects.addAll(((Ship) gameObjects.get(0)).shootRightSide());
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.moveAlong(mouseInput);
        for(int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).moveForward();
            if(gameObjects.get(i).getClass() == Missile.class && (gameObjects.get(i).collides(gameObjects.get(1)) || ((Missile) gameObjects.get(i)).isExhausted())){
                gameObjects.get(i).destroy();
                if(gameObjects.get(i).getLifeState() == DEAD) {
                    gameObjects.remove(i);
                    i--;
                }
            }
        }
    }

    @Override
    public void render(Window window) {
        //hud.updateSize(window);
        renderer.render(window, camera, gameObjects, sceneLight, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanup();
        }
        hud.cleanup();
    }
}
