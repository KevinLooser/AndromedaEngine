package engine.objects.modules;

import engine.graph.Mesh;

public abstract class Weapon extends Module {

    private float interval;
    protected Mesh mesh;

    public Weapon(ModPosition position, float interval, Mesh mesh) {
        super(position);
        this.interval = interval;
        this.mesh = mesh;
    }
}
