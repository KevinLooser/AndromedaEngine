package engine.objects.modules;

import engine.graph.Mesh;
import engine.objects.Ship;

public abstract class Weapon extends Module {

    protected long interval;
    protected long lastTime;
    protected float missileRadius;
    protected float missileSpeed;
    protected float missileRange;
    protected float missileDamage;
    protected float missileAcceleration;
    protected Ship owner;

    protected Mesh mesh;

    public Weapon(ModPosition position, long interval, Mesh mesh, float missileRadius, float missileSpeed, float missileRange, float missileDamage, float missileAcceleration, Ship owner) {
        super(position);
        this.interval = interval;
        this.mesh = mesh;
        this.missileRadius = missileRadius;
        this.missileSpeed = missileSpeed;
        this.missileRange = missileRange;
        this.missileDamage = missileDamage;
        this.missileAcceleration = missileAcceleration;
        this.owner = owner;
        lastTime = System.currentTimeMillis();
    }
}
