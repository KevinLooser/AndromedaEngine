package game;

import engine.*;
import engine.graph.*;
import engine.input.MouseInput;
import engine.objects.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static engine.objects.GameObject.LifeState.DEAD;
import static org.lwjgl.glfw.GLFW.*;

public class DemoGame implements IGameLogic {

    private final Renderer renderer;

    private static final float SHIP_ACCELERATION = 0.001f;

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
        //mesh.setTexture(texture);
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
        GameObject plane = new Obstacle(mesh, 1f);
        asteroid.setPosition(-4, 0, -1);
        plane.setPosition(0, -5, 0);
        ship.setPosition(0, 0, -cameraDistance);

        // add game objects
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(ship);
        gameObjects.add(asteroid);
        gameObjects.add(plane);
        gameObjects.add(skyBox);
        scene.setGameObjects(gameObjects);
        camera.init(ship, cameraDistance);

        // lights
        SceneLight sceneLight = new SceneLight();
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
        scene.setSceneLight(sceneLight);

        // create hud
        hud = new Hud("AndromedaEngine v0.12");
        hud.addStatusText("Asteroid Durability: " + scene.getGameObjects().get(1).getDurability());
        hud.getGameObjects().get(1).setPosition(1, 10, 0);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        if (window.isKeyPressed(GLFW_KEY_W)) {
            scene.getGameObjects().get(0).accelerate();
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            scene.getGameObjects().get(0).decelerate();
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            ((Ship) scene.getGameObjects().get(0)).steer(-1);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            ((Ship) scene.getGameObjects().get(0)).steer(1);
        }
        if (window.isKeyPressed(GLFW_KEY_F)) {
            scene.getGameObjects().add(((Ship) scene.getGameObjects().get(0)).shootFrontal());
        }
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            scene.getGameObjects().addAll(((Ship) scene.getGameObjects().get(0)).shootLeftSide());
        }
        if (window.isKeyPressed(GLFW_KEY_E)) {
            scene.getGameObjects().addAll(((Ship) scene.getGameObjects().get(0)).shootRightSide());
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.moveAlong(mouseInput);
        scene.getSkyBox().moveAlong(scene.getGameObjects().get(0).getPosition().x, scene.getGameObjects().get(0).getPosition().y, scene.getGameObjects().get(0).getPosition().z);
        for(int i = 0; i < scene.getGameObjects().size(); i++) {
            scene.getGameObjects().get(i).moveForward();
            if(scene.getGameObjects().get(i).getClass() == Missile.class && (scene.getGameObjects().get(i).collides(scene.getGameObjects().get(1)) || ((Missile) scene.getGameObjects().get(i)).isExhausted())){
                scene.getGameObjects().get(i).destroy();
                scene.getGameObjects().get(1).dealDamage(((Missile)  scene.getGameObjects().get(i)).getDamage());
                hud.setStatusText("Asteroid Durability: " + scene.getGameObjects().get(1).getDurability(), (TextItem) hud.getGameObjects().get(1));
                if(scene.getGameObjects().get(i).getLifeState() == DEAD) {
                    scene.getGameObjects().remove(i);
                    i--;
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
        for (GameObject gameObject : scene.getGameObjects()) {
            gameObject.getMesh().cleanup();
        }
        hud.cleanup();
    }
}
