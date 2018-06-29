package engine.objects;

import engine.graph.Mesh;

public class Obstacle extends GameObject {

    public Obstacle(Mesh mesh, float radius) {
        super(mesh, radius);
    }

    public Obstacle(Mesh mesh, float radius, float speed, float acceleration, float durability) {
        super(mesh, radius, speed, acceleration, durability);
    }
}
