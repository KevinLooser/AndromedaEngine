package engine.objects.modules;

import engine.graph.Mesh;
import engine.objects.Missile;
import engine.objects.Ship;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Broadsides extends Weapon {

    private int amount;
    private float spread;
    private long leftLastTime;
    private long rightLastTime;

    public enum Side {
        LEFT, RIGHT
    }

    public Broadsides(ModPosition position, long interval, int amount, float spread, Mesh mesh, Ship owner, float missileRadius, float missileSpeed, float missileRange, float missileDamage, float missileAcceleration) {
        super(position, interval, mesh, missileRadius, missileSpeed, missileRange, missileDamage, missileAcceleration, owner);
        this.amount = amount;
        this.spread = spread;
        leftLastTime = System.currentTimeMillis();
        rightLastTime = System.currentTimeMillis();
    }

    public List<Missile> load(Vector3f position, Vector3f rotation, float length, float width, Side side) {
        List<Missile> missiles = new ArrayList<>();
        switch (side) {
            case LEFT:
                leftLastTime = System.currentTimeMillis();
            case RIGHT:
                rightLastTime = System.currentTimeMillis();
        }

        float padding = length * 0.1f;
        float row = length - (2 * padding);
        float distanceBetween = row / amount;
        float distance;
        float offsetX = (float) Math.cos(Math.toRadians(rotation.y)) * (row / 2);
        float offsetZ = (float) Math.sin(Math.toRadians(rotation.y)) * (row / 2);
        Vector3f firstPosition = new Vector3f(position.x + offsetX, position.y, position.z + offsetZ);

        for(int i = 0; i < amount; i++) {
            for(int j = -1; j <= 1; j++) {
                Missile missile = new Missile(super.mesh, missileRadius, missileSpeed, missileRange, missileDamage, owner);
                missile.setAcceleration(missileAcceleration);
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

                // TODO add offset to width of model (in non parallel cases, this is difficult)
                float offsetXSide = (float) Math.cos(Math.toRadians(rotation.y)) * sideOffset;
                float offsetZSide = (float) Math.sin(Math.toRadians(rotation.y)) * sideOffset;
                offsetXSide = 0;
                offsetZSide = 0;

                missile.setPosition(firstPosition.x + offsetX + offsetXSide, firstPosition.y, firstPosition.z + offsetZ + offsetZSide);
                missile.setOrigin(position.x, position.y, position.z);
                missile.setRotation(rotation.x, rotation.y + sideRotation + (spread * j), rotation.z);
                missile.setScale(0.1f);
                missiles.add(missile);
            }
        }
        return missiles;
    }

    public boolean isReady(Side side) {
        switch(side) {
            case LEFT:
                return System.currentTimeMillis() - leftLastTime >= interval;
            case RIGHT:
                return System.currentTimeMillis() - rightLastTime >= interval;
                default:
                    return true;
        }
    }
}
