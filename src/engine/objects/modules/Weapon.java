package engine.objects.modules;

import engine.graph.Mesh;

public abstract class Weapon extends Module {

    private float interval;
    private float range;
    protected Mesh mesh;

    public Weapon(ModPosition position, float interval, float range, Mesh mesh) {
        super(position);
        this.interval = interval;
        this.range = range;
        this.mesh = mesh;
    }
}
