package game;

import engine.*;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.Texture;
import engine.graph.ThirdPersonCamera;
import engine.input.MouseInput;
import engine.objects.GameObject;
import engine.objects.Obstacle;
import engine.objects.Ship;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class DemoGame implements IGameLogic {

    private final Renderer renderer;
    private List<GameObject> gameObjects;
    private ThirdPersonCamera camera;
    private static final float SHIP_ACCELERATION = 0.001f;

    public DemoGame() {
        renderer = new Renderer();
        camera = new ThirdPersonCamera();
        gameObjects = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        // load textures and define colours
        //Texture texture = new Texture("../resources/textures/asteroid.png");

        // load meshes
        Mesh mesh = OBJLoader.loadMesh("../resources/models/cube.obj");
        //mesh.setTexture(texture);
        mesh.setColour(new Vector3f(0.5f, 0.0f, 0.0f));

        Mesh missileMesh = OBJLoader.loadMesh("../resources/models/missile.obj");
        missileMesh.setColour(new Vector3f(0.5f, 0.5f, 0.0f));

        Mesh shipMesh = OBJLoader.loadMesh("../resources/models/ship.obj");
        shipMesh.setColour(new Vector3f(0.0f, 0.5f, 0.5f));

        // create game objects
        float cameraDistance = 5f;
        GameObject ship = new Ship(shipMesh, missileMesh, missileMesh, 0, SHIP_ACCELERATION, 1);
        GameObject asteroid = new Obstacle(mesh);
        GameObject plane = new Obstacle(mesh);
        asteroid.setPosition(-4, 0, -1);
        plane.setPosition(0, -5, 0);
        ship.setPosition(0, 0, -cameraDistance);

        // add game objects
        gameObjects.add(ship);
        gameObjects.add(asteroid);
        gameObjects.add(plane);
        camera.init(ship, cameraDistance);
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
        for(GameObject gameObject : gameObjects) {
            gameObject.moveForward();
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameObjects);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanup();
        }
    }
}
