package engine.objects.modules;

import engine.graph.Mesh;
import engine.objects.Missile;
import engine.objects.Ship;
import org.joml.Vector3f;

public class Front extends Weapon {

    private float length;

    public Front(ModPosition position, long interval, float length, Mesh mesh, Ship owner, float missileRadius, float missileSpeed, float missileRange, float missileDamage, float missileAcceleration) {
        super(position, interval, mesh, missileRadius, missileSpeed, missileRange, missileDamage, missileAcceleration, owner);
        this.length = length;
    }

    public Missile load(Vector3f position, Vector3f rotation) {
        Missile missile = new Missile(super.mesh, missileRadius, missileSpeed, missileRange, missileDamage, owner);
        missile.setAcceleration(missileAcceleration);
        lastTime = System.currentTimeMillis();

        float offsetX = (float) Math.cos(Math.toRadians(rotation.y)) * ((length / 2) + 0.3f);
        float offsetZ = (float) Math.sin(Math.toRadians(rotation.y)) * ((length / 2) + 0.3f);

        missile.setPosition(position.x + offsetX, position.y, position.z + offsetZ);
        missile.setOrigin(position.x, position.y, position.z);
        missile.setRotation(rotation.x, rotation.y, rotation.z);
        missile.setScale(0.1f);

        return missile;
    }

    public boolean isReady() {
        return System.currentTimeMillis() - lastTime >= interval;
    }
}
