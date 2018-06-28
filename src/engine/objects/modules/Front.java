package engine.objects.modules;

import engine.graph.Mesh;
import engine.objects.Missile;
import engine.objects.modules.Weapon;
import org.joml.Vector3f;

public class Front extends Weapon {

    public Front(ModPosition position, float interval, float range, Mesh mesh) {
        super(position, interval, range, mesh);
    }

    public Missile load(Vector3f position, Vector3f rotation) {
        Missile bullet = new Missile(super.mesh, 0.9f, 0.08f);
        bullet.setPosition(position.x, position.y, position.z);
        bullet.setRotation(rotation.x, rotation.y, rotation.z);
        System.out.println("Missile loaded.");
        bullet.setScale(0.1f);
        return bullet;
    }
}
