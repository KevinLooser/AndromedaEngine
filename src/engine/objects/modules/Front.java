package engine.objects.modules;

import engine.graph.Mesh;
import engine.objects.Missile;
import org.joml.Vector3f;

public class Front extends Weapon {

    private float length;

    public Front(ModPosition position, float interval, float length, Mesh mesh) {
        super(position, interval, mesh);
        this.length = length;
    }

    public Missile load(Vector3f position, Vector3f rotation) {
        Missile missile = new Missile(super.mesh, 0.2f, 0.9f, 30f, 0.08f);
        missile.setAcceleration(-0.001f);

        float offsetX = (float) Math.cos(Math.toRadians(rotation.y)) * ((length / 2) + 0.3f);
        float offsetZ = (float) Math.sin(Math.toRadians(rotation.y)) * ((length / 2) + 0.3f);

        missile.setPosition(position.x + offsetX, position.y, position.z + offsetZ);
        missile.setOrigin(position.x, position.y, position.z);
        missile.setRotation(rotation.x, rotation.y, rotation.z);
        missile.setScale(0.1f);

        return missile;
    }
}
