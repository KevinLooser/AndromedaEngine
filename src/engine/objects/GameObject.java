package engine.objects;

import engine.graph.Mesh;
import org.joml.Vector3f;

import static engine.objects.GameObject.LifeState.ALIVE;
import static engine.objects.GameObject.LifeState.DEAD;

public class GameObject {

    private Mesh mesh;
    protected final Vector3f position;
    protected float scale;
    protected final Vector3f rotation;
    protected float radius;
    protected LifeState lifeState = ALIVE;

    protected float speed;
    protected float maxSpeed;
    protected float acceleration;
    protected float durability;

    public GameObject() {
        radius = 0f;
        scale = 1;
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh, float radius) {
        this.mesh = mesh;
        this.radius = radius;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh, float radius, float speed, float acceleration, float durability) {
        this.mesh = mesh;
        this.radius = radius;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        this.speed = speed;
        this.acceleration = acceleration;
        this.durability = durability;
        maxSpeed = calculateMaxSpeed();
    }

    public enum LifeState {
        ALIVE, DEAD
    }

    public void moveForward() {
        if(speed != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y -90)) * -1.0f * speed;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * speed;
        }
    }

    public void moveBackward() {
        if(speed != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y -90)) * speed;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * -1.0f * speed;
        }
    }

    public void accelerate() {
        if(speed <= calculateMaxSpeed()) {
            speed += acceleration;
        }
        if(speed > calculateMaxSpeed()) {
            speed = calculateMaxSpeed();
        }
    }

    public void decelerate() {
        speed -= acceleration;
        if (speed < 0) {
            speed = 0;
        }
    }

    public float calculateMaxSpeed() {
        return acceleration * 100;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean collides(GameObject collidingObj) {
        float minDistance = this.radius + collidingObj.radius;
        float xDistance = Math.abs(this.position.x - collidingObj.position.x);
        float zDistance = Math.abs(this.position.z - collidingObj.position.z);
        float currentDistance = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(zDistance, 2));
        return currentDistance < minDistance;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    public Mesh getMesh() {
        return mesh;
    }

    public void destroy() {
        lifeState = DEAD;
    }

    public void revive() {
        lifeState = ALIVE;
    }

    public LifeState getLifeState() {
        return lifeState;
    }

    public float getDurability() {
        return durability;
    }

    public void setDurability(float durability) {
        this.durability = durability;
    }

    public void dealDamage(float damage) {
        durability -= damage;
    }
}
