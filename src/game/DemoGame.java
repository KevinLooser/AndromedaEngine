package game;

import engine.*;
import engine.graph.*;
import engine.input.MouseInput;
import engine.objects.*;
import engine.objects.modules.Broadsides;
import engine.objects.modules.Front;
import engine.objects.modules.Shield;
import engine.objects.modules.Thruster;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static engine.objects.GameObject.LifeState.ALIVE;
import static engine.objects.GameObject.LifeState.DEAD;
import static engine.objects.modules.Module.ModPosition.*;
import static org.lwjgl.glfw.GLFW.*;

public class DemoGame implements IGameLogic {

    private final Renderer renderer;

    private static final float SHIP_ACCELERATION = 0.001f;

    private List<GameObject> gameObjects;
    private GameObject player;
    private ShipGroup ships;
    private ObstacleGroup obstacles;
    private MissileGroup missiles;

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
        float reflectance = 0.001f;
        float reflactanceMissile = 10f;
        Texture texture = new Texture("/resources/textures/asteroid.png");
        Texture block = new Texture("/resources/textures/grassblock.png");
        Material asteroidMaterial = new Material(block, reflectance);
        Material shipMaterial = new Material(texture, reflectance);
        Material missileMaterial = new Material(texture, reflactanceMissile);

        // load meshes
        Mesh mesh = OBJLoader.loadMesh("/resources/models/cube.obj");
        mesh.setMaterial(asteroidMaterial);

        Mesh missileMesh = OBJLoader.loadMesh("/resources/models/missile.obj");
        missileMesh.setMaterial(missileMaterial);

        Mesh shipMesh2 = OBJLoader.loadMesh("/resources/models/ship.obj");
        shipMesh2.setMaterial(shipMaterial);
        Mesh shipMesh = OBJLoader.loadMesh("/resources/models/stardestroyer.obj");
        shipMesh.setMaterial(shipMaterial);

        // skybox
        SkyBox skyBox = new SkyBox("/resources/models/skybox.obj", "/resources/textures/skybox.png");
        skyBox.setScale(100f);
        scene.setSkyBox(skyBox);

        // create game objects
        float cameraDistance = 7f;

        // create player ship
        GameObject ship = new Ship(shipMesh, 0, SHIP_ACCELERATION, 10f);
        Front frontWeapon = new Front(FRONT, 250, 4f, missileMesh, (Ship) ship, 0.2f, 1.1f, 36f, 0.08f, 0.001f);
        Broadsides broadsidesWeapon = new Broadsides(BROADSIDES, 1_000, 3, 1f, missileMesh, (Ship) ship, 0.2f, 0.5f, 12f, 0.3f, 0.005f);
        Shield shield = new Shield(DECK, 2f, 0.01f,  10_000);
        Thruster thruster = new Thruster(THRUSTERS, 0.002f, 1f, 0.0001f, 1_000);
        ((Ship) ship).init(frontWeapon, broadsidesWeapon, shield, thruster);

        // create enemy ships
        GameObject enemyShip = new Ship(shipMesh2,0, SHIP_ACCELERATION, 5f);
        Front frontWeaponEnemy = new Front(FRONT, 5_000, 4f, missileMesh, (Ship) enemyShip, 0.2f, 1.1f, 36f, 0.08f, 0.001f);
        Broadsides broadsidesWeaponEnemy = new Broadsides(BROADSIDES, 1_000, 3, 1.5f, missileMesh, (Ship) enemyShip, 0.2f, 0.5f, 12f, 0.3f, 0.005f);
        Shield shieldEnemy = new Shield(DECK, 2f, 0.01f, 10_000);
        Thruster thrusterEnemy = new Thruster(THRUSTERS, 0.002f, 0.5f, 0.0001f, 1_000);
        ((Ship) enemyShip).init(frontWeaponEnemy, broadsidesWeaponEnemy, shieldEnemy, thrusterEnemy);

        // setup objects
        // TODO read positions and rotations from save file
        enemyShip.setPosition(-10f, 0f, 0f);
        enemyShip.setRadius(2f);
        GameObject asteroid1 = new Obstacle(mesh, 1f);

        GameObject asteroid2 = new Obstacle(mesh, 1f);
        GameObject asteroid3 = new Obstacle(mesh, 1f);
        GameObject asteroid4 = new Obstacle(mesh, 1f);
        GameObject asteroid5 = new Obstacle(mesh, 1f);
        GameObject asteroid6 = new Obstacle(mesh, 1f);
        GameObject asteroid7 = new Obstacle(mesh, 1f);
        GameObject asteroid8 = new Obstacle(mesh, 1f);
        GameObject asteroid9 = new Obstacle(mesh, 1f);
        GameObject asteroid10 = new Obstacle(mesh, 1f);
        GameObject asteroid11 = new Obstacle(mesh, 1f);
        GameObject asteroid12 = new Obstacle(mesh, 1f);
        GameObject asteroid13 = new Obstacle(mesh, 1f);
        GameObject asteroid14 = new Obstacle(mesh, 1f);
        GameObject asteroid15 = new Obstacle(mesh, 1f);
        GameObject asteroid16 = new Obstacle(mesh, 1f);
        GameObject asteroid17 = new Obstacle(mesh, 1f);
        GameObject asteroid18 = new Obstacle(mesh, 1f);
        GameObject asteroid19 = new Obstacle(mesh, 1f);

        asteroid1.setDurability(6.0f);
        asteroid1.setPosition(-4f, 0, 2f);
        asteroid2.setDurability(6.0f);
        asteroid2.setPosition(-6f, 0, 27f);
        asteroid3.setDurability(6.0f);
        asteroid3.setPosition(-8f, 0, 18f);
        asteroid4.setDurability(6.0f);
        asteroid4.setPosition(20f, 0, 12f);
        asteroid5.setDurability(6.0f);
        asteroid5.setPosition(20f, 0, -14f);
        asteroid6.setDurability(6.0f);
        asteroid6.setPosition(8f, 0, -6f);
        asteroid7.setDurability(6.0f);
        asteroid7.setPosition(14f, 0, -16f);
        asteroid8.setDurability(6.0f);
        asteroid8.setPosition(-20f, 0, -22f);
        asteroid9.setDurability(6.0f);
        asteroid9.setPosition(-30f, 0, -10f);
        asteroid10.setDurability(6.0f);
        asteroid10.setPosition(-20f, 0, -15f);

        asteroid11.setDurability(6.0f);
        asteroid11.setPosition(-10f, 10f, -10f);
        asteroid12.setDurability(6.0f);
        asteroid12.setPosition(-12f, 6f, 7f);
        asteroid13.setDurability(6.0f);
        asteroid13.setPosition(-5f, -10f, 5f);
        asteroid14.setDurability(6.0f);
        asteroid14.setPosition(-2f, -20f, 0);
        asteroid15.setDurability(6.0f);
        asteroid15.setPosition(9f, -7f, 15f);
        asteroid16.setDurability(6.0f);
        asteroid16.setPosition(5f, -11f, -14f);
        asteroid17.setDurability(6.0f);
        asteroid17.setPosition(0, 15f, 2f);
        asteroid18.setDurability(6.0f);
        asteroid18.setPosition(7f, 14f, -4f);
        asteroid19.setDurability(6.0f);
        asteroid19.setPosition(13f, -12f, -9f);
        ship.setPosition(0, 0, -cameraDistance);
        ship.setRotation(0, 90, 0);

        // add game objects
        gameObjects = new ArrayList<>();
        gameObjects.add(ship);
        gameObjects.add(enemyShip);
        gameObjects.add(asteroid1);
        gameObjects.add(asteroid2);
        gameObjects.add(asteroid3);
        gameObjects.add(asteroid4);
        gameObjects.add(asteroid5);
        gameObjects.add(asteroid6);
        gameObjects.add(asteroid7);
        gameObjects.add(asteroid8);
        gameObjects.add(asteroid9);
        gameObjects.add(asteroid10);
        gameObjects.add(asteroid11);
        gameObjects.add(asteroid12);
        gameObjects.add(asteroid13);
        gameObjects.add(asteroid14);
        gameObjects.add(asteroid15);
        gameObjects.add(asteroid16);
        gameObjects.add(asteroid17);
        gameObjects.add(asteroid18);
        gameObjects.add(asteroid19);
        gameObjects.add(skyBox);
        scene.setGameObjectsMeshes(gameObjects);
        camera.init(ship, cameraDistance);

        // add player and ships
        ships = new ShipGroup();
        ships.add((Ship) enemyShip);
        ships.add((Ship) ship);
        player = ship;

        // add obstacles
        obstacles = new ObstacleGroup();
        obstacles.add((Obstacle) asteroid1);
        obstacles.add((Obstacle) asteroid2);
        obstacles.add((Obstacle) asteroid3);
        obstacles.add((Obstacle) asteroid4);
        obstacles.add((Obstacle) asteroid5);
        obstacles.add((Obstacle) asteroid6);
        obstacles.add((Obstacle) asteroid7);
        obstacles.add((Obstacle) asteroid8);
        obstacles.add((Obstacle) asteroid9);
        obstacles.add((Obstacle) asteroid10);

        // prepare missiles
        missiles = new MissileGroup();

        // lights
        setupLights();

        // create hud
        setupHud();

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
        hud.addStatusText("Player Durability: " + player.getDurability() * 100);
        hud.getGameObjects().get(1).setPosition(5, 10, 0);
        hud.addStatusText("Player Shield: " + ((Ship) player).getShield() * 100);
        hud.getGameObjects().get(2).setPosition(5, 40, 0);
        hud.addStatusText("Player Speed: " + player.getSpeed() * 100);
        hud.getGameObjects().get(3).setPosition(5, 70, 0);
        hud.addStatusText("Player Fuel: " + ((Ship) player).getFuel() * 100);
        hud.getGameObjects().get(4).setPosition(5, 100, 0);
        hud.addStatusText("Player Rotation: " + ((Ship) player).getAngle());
        hud.getGameObjects().get(5).setPosition(5, 160, 0);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        if(player.getLifeState() == ALIVE) {
            if (window.isKeyPressed(GLFW_KEY_W) && !((Ship) player).boostActive()) {
                player.accelerate();
            } else if (window.isKeyPressed(GLFW_KEY_S) && !((Ship) player).boostActive()) {
                player.decelerate();
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                ((Ship) player).steer(-1);
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                ((Ship) player).steer(1);
            }
            if (window.isKeyPressed(GLFW_KEY_F) && ((Ship) player).isFrontReady() && ((Ship) player).weaponsActive()) {
                Missile missile = ((Ship) player).shootFrontal();
                gameObjects.add(missile);
                missiles.add(missile);
            }
            if (window.isKeyPressed(GLFW_KEY_Q) && ((Ship) player).isLeftBroadsideReady() && ((Ship) player).weaponsActive()) {
                List<Missile> missileListLeft = ((Ship) player).shootLeftSide();
                gameObjects.addAll(missileListLeft);
                missiles.addAll(missileListLeft);
            }
            if (window.isKeyPressed(GLFW_KEY_E) && ((Ship) player).isRightBroadsideReady() && ((Ship) player).weaponsActive()) {
                List<Missile> missileListRight = ((Ship) player).shootRightSide();
                gameObjects.addAll(missileListRight);
                missiles.addAll(missileListRight);
            }
            if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT) && ((Ship) player).isBoostReady()) {
                ((Ship) player).boost();
            } else {
                ((Ship) player).stopBoost();
            }
            if (window.isKeyPressed(GLFW_KEY_SPACE) && ((Ship) player).isShieldReady()) {
                ((Ship) player).useShield();
            } else {
                ((Ship) player).stopShield();
            }
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.moveAlong(mouseInput);
        scene.getSkyBox().moveAlong(player.getPosition().x, player.getPosition().y, player.getPosition().z);

        updateAll();
        controlEnemies();
        updateShips();
        updateMissiles();
        hud.setStatusText("Player Durability: " + player.getDurability() * 100, (TextItem) hud.getGameObjects().get(1));
        hud.setStatusText("Player Shield: " + ((Ship) player).getShield() * 100, (TextItem) hud.getGameObjects().get(2));
        hud.setStatusText("Player Speed: " + player.getSpeed() * 100, (TextItem) hud.getGameObjects().get(3));
        hud.setStatusText("Player Fuel: " + ((Ship) player).getFuel() * 100, (TextItem) hud.getGameObjects().get(4));
        hud.setStatusText("Player Rotation: " + ((Ship) player).getAngle(), (TextItem) hud.getGameObjects().get(5));

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

    private void updateShips() {
        for(int i = 0; i < ships.size(); i++) {
            Ship currentShip = ships.get(i);
            for(int j = 0; j < obstacles.size(); j++) {
                Obstacle currentObstacle = obstacles.get(j);
                if(currentShip.collides(currentObstacle)) {
                    currentShip.moveBackward();
                    currentShip.steer(currentShip.evade(currentObstacle));
                }
            }
            for(int s = 0; s < ships.size(); s++) {
                Ship currentEnemy = ships.get(s);
                if(s != i && currentShip.collides(currentEnemy)) {
                    currentShip.moveBackward();
                    currentShip.steer(currentShip.evade(currentEnemy));
                }
            }
        }
    }

    private void updateMissiles() {
        for(int j = 0; j < missiles.size(); j++) {
            Missile currentMissile = missiles.get(j);
            if(currentMissile.isExhausted()) {
                currentMissile.destroy();
                missiles.remove(currentMissile);
                j--;
            }
            for (int k = 0; k < obstacles.size(); k++) {
                Obstacle currentObstacle = obstacles.get(k);
                if(currentMissile.collides(currentObstacle) && currentMissile.getLifeState() == ALIVE) {
                    currentObstacle.dealDamage(currentMissile.getDamage());
                    currentMissile.destroy();
                    missiles.remove(currentMissile);
                    j--;
                }
                if(currentObstacle.getDurability() <= 0) {
                    currentObstacle.destroy();
                    obstacles.remove(currentObstacle);
                    k--;
                }
            }
            for (int s = 0; s < ships.size(); s++) {
                Ship currentShip = ships.get(s);
                if(currentMissile.collides(currentShip) && !currentMissile.isOwner(currentShip) && currentMissile.getLifeState() == ALIVE) {
                    currentShip.dealDamage(currentMissile.getDamage());
                    currentMissile.destroy();
                    missiles.remove(currentMissile);
                    j--;
                }
                if(currentShip.getDurability() <= 0) {
                        currentShip.destroy();
                        ships.remove(currentShip);
                        s--;
                }
            }
        }
    }

    private void controlEnemies() {
        for (int e = 0; e < ships.size(); e++) {
            Ship currentShip = ships.get(e);
            if(currentShip != player) {
                if(player.getPosition().z - currentShip.getPosition().z < 0 && currentShip.isLeftBroadsideReady()) {
                    List<Missile> missileList = currentShip.shootLeftSide();
                    gameObjects.addAll(missileList);
                    missiles.addAll(missileList);
                } else if (player.getPosition().z - currentShip.getPosition().z > 0 && currentShip.isRightBroadsideReady()) {
                    List<Missile> missileList = currentShip.shootRightSide();
                    gameObjects.addAll(missileList);
                    missiles.addAll(missileList);
                } else {
                    if(currentShip.isFrontReady()) {
                        Missile missile = currentShip.shootFrontal();
                        gameObjects.add(missile);
                        missiles.add(missile);
                    }
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
