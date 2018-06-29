package engine.objects;

import engine.graph.Mesh;
import org.joml.Vector3f;

public class Missile extends GameObject {

    private float damage;
    private float range;
    private Vector3f startPosition;

    public Missile(Mesh mesh, float radius, float speed, float range, float damage) {
        super(mesh, radius);
        super.speed = speed;
        this.damage = damage;
        this.range = range;
        startPosition = new Vector3f(position.x, position.y, position.z);
    }

    public Missile(Mesh mesh, float radius, float speed, float range, float acceleration, float durability, float damage) {
        super(mesh, radius, speed, acceleration, durability);
        this.damage = damage;
        this.range = range;
        startPosition = new Vector3f(position.x, position.y, position.z);
    }

    @Override
    public void moveForward() {
        if(!isExhausted()) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * speed;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * speed;
        }
    }

    public boolean isExhausted() {
        float xDistance = Math.abs(position.x - startPosition.x);
        float zDistance = Math.abs(position.z - startPosition.z);
        float currentDistance = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(zDistance, 2));
        return currentDistance > range;
    }

    public void setOrigin(float x, float y, float z) {
        startPosition.x = x;
        startPosition.y = y;
        startPosition.z = z;
    }

    public float getDamage() {
        return damage;
    }
}
