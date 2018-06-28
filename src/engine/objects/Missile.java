package engine.objects;

import engine.graph.Mesh;

public class Missile extends GameObject {

    private float damage;
    private float start;
    private float range;

    public Missile(Mesh mesh, float speed, float damage) {
        super(mesh);
        super.speed = speed;
        this.damage = damage;
    }

    public Missile(Mesh mesh, float speed, float acceleration, float durability, float damage) {
        super(mesh, speed, acceleration, durability);
        this.damage = damage;
    }
}
