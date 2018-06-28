package engine.objects;

import engine.graph.Mesh;
import org.joml.Vector3f;

public class GameObject {

    private Mesh mesh;
    private final Vector3f position;
    private float scale;
    protected final Vector3f rotation;

    protected float speed;
    protected float acceleration;
    protected float durability;

    public GameObject() {
        mesh = null;
        position = null;
        rotation = null;
    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public GameObject(Mesh mesh, float speed, float acceleration, float durability) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
        this.speed = speed;
        this.acceleration = acceleration;
        this.durability = durability;
    }

    public void moveForward() {
        if(speed != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y -90)) * -1.0f * speed;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * speed;
        }
    }

    public void accelerate() {
        if(speed < acceleration * 100) {
            speed += acceleration;
        }
    }

    public void decelerate() {
        speed -= acceleration;
        if (speed < 0) {
            speed = 0;
        }
    }

    public boolean collides(GameObject collidingObj) {
        // TODO: check if this collides with potential other object given
        return false;
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

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    public Mesh getMesh() {
        return mesh;
    }
}
