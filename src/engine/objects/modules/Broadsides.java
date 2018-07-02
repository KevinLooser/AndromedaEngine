package engine.objects.modules;

import engine.graph.Mesh;
import engine.objects.Missile;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Broadsides extends Weapon {

    private int amount;
    private float spread;

    public enum Side {
        LEFT, RIGHT
    }

    public Broadsides(ModPosition position, float interval, int amount, float spread, Mesh mesh) {
        super(position, interval, mesh);
        this.amount = amount;
        this.spread = spread;
    }

    public List<Missile> load(Vector3f position, Vector3f rotation, float length, float width, Side side) {
        List<Missile> missiles = new ArrayList<>();

        float padding = length * 0.1f;
        float row = length - (2 * padding);
        float distanceBetween = row / amount;
        float distance;
        float offsetX = (float) Math.cos(Math.toRadians(rotation.y)) * (row / 2);
        float offsetZ = (float) Math.sin(Math.toRadians(rotation.y)) * (row / 2);
        Vector3f firstPosition = new Vector3f(position.x + offsetX, position.y, position.z + offsetZ);

        for(int i = 0; i < amount; i++) {
            for(int j = -1; j <= 1; j++) {
                Missile missile = new Missile(super.mesh, 0.2f, 0.6f, 20f, 0.08f);
                missile.setAcceleration(-0.01f);
                float sideRotation = 0;
                float sideOffset = 0;
                switch (side) {
                    case LEFT:
                        sideRotation = -90;
                        sideOffset = -(width / 2) - 0.3f;
                        break;
                    case RIGHT:
                        sideRotation = 90;
                        sideOffset = width / 2 + 0.3f;
                        break;
                }
                distance = (-1) * i * distanceBetween;
                offsetX = (float) Math.cos(Math.toRadians(rotation.y)) * distance;
                offsetZ = (float) Math.sin(Math.toRadians(rotation.y)) * distance;

                // TODO fix'n'calculate
                float offsetXSide = (float) Math.cos(Math.toRadians(rotation.y)) * sideOffset;
                float offsetZSide = (float) Math.sin(Math.toRadians(rotation.y)) * sideOffset;
                missile.setPosition(firstPosition.x + offsetX, firstPosition.y, firstPosition.z + offsetZ);
                missile.setOrigin(position.x, position.y, position.z);
                missile.setRotation(rotation.x, rotation.y + sideRotation + (spread * j), rotation.z);
                missile.setScale(0.1f);
                missiles.add(missile);
            }
        }
        return missiles;
    }
}
