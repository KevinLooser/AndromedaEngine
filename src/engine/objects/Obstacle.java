package engine.objects;

import engine.graph.Mesh;

public class Obstacle extends GameObject {

    public Obstacle(Mesh mesh) {
        super(mesh);
    }

    public Obstacle(Mesh mesh, float speed, float acceleration, float durability) {
        super(mesh, speed, acceleration, durability);
    }
}
