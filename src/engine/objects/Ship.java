package engine.objects;

import engine.graph.Mesh;
import engine.objects.modules.*;
import engine.objects.modules.Module;
import org.joml.Vector3f;

import java.util.List;

public class Ship extends GameObject {

    private Weapon frontWeapon;
    private Weapon broadsidesWeapon;
    private Shield shield;
    private Thruster thruster;

    private boolean weaponsActive;
    private boolean shieldActive;
    private boolean boostActive;

    private float defaultAcceleration;
    private float length = 4f;
    private float width = 2f;
    private static final float MANEUVERABILITY = 0.75f;

    public Ship(Mesh mesh, float speed, float acceleration, float durability) {
        super(mesh, 4f, speed, acceleration, durability);
        defaultAcceleration = acceleration;
    }

    public void init(Front frontWeapon, Broadsides broadsidesWeapon, Shield shield, Thruster thruster) {
        this.frontWeapon = frontWeapon;
        this.broadsidesWeapon = broadsidesWeapon;
        this.shield = shield;
        this.thruster = thruster;
    }

    public void steer(float mod) {
        super.rotation.y += MANEUVERABILITY * mod / (super.speed + 1);
    }

    public Missile shootFrontal() {
        return ((Front) frontWeapon).load(getPosition(), getRotation());
    }

    public List<Missile> shootLeftSide() {
        return ((Broadsides) broadsidesWeapon).load(getPosition(), getRotation(), length, width, Broadsides.Side.LEFT);
    }

    public List<Missile> shootRightSide() {
        return ((Broadsides) broadsidesWeapon).load(getPosition(), getRotation(), length, width, Broadsides.Side.RIGHT);
    }

    public void boost() {
        weaponsActive = false;
        boostActive = true;
        acceleration = thruster.getMagnitude();
        if(speed < calculateMaxSpeed()) {
            accelerate();
        }
        thruster.drainFuel();
    }

    public void stopBoost() {
        weaponsActive = true;
        boostActive = false;
        acceleration = defaultAcceleration;
        if(speed > calculateMaxSpeed()) {
            decelerate();
        }
        thruster.unload();
        thruster.rechargeFuel();
    }

    public void useShield() {
        weaponsActive = false;
        shieldActive = true;
    }

    public void stopShield() {
        weaponsActive = true;
        shieldActive = false;
        shield.unload();
        shield.rechargeShield();
    }

    public boolean isFrontReady() {
        return ((Front) frontWeapon).isReady();
    }

    public boolean isLeftBroadsideReady() {
        return ((Broadsides) broadsidesWeapon).isReady(Broadsides.Side.LEFT);
    }

    public boolean isRightBroadsideReady() {
        return ((Broadsides) broadsidesWeapon).isReady(Broadsides.Side.RIGHT);
    }

    public boolean isBoostReady() {
        return thruster.hasFuel();
    }

    public boolean isShieldReady() {
        return shield.isReady();
    }

    public float getShield() {
        return shield.getMagnitude();
    }

    public float getFuel() {
        return thruster.getFuel();
    }

    public boolean weaponsActive() {
        return weaponsActive;
    }

    public boolean shieldActive() {
        return shieldActive;
    }

    public boolean boostActive() {
        return boostActive;
    }

    @Override
    public void dealDamage(float damage) {
        if(shieldActive()) {
            durability -= shield.dealDamage(damage);
        } else {
            durability -= damage;
        }

        if(durability < 0) {
            durability = 0;
        }
    }

    public float getAngle() {
        return this.rotation.y % 360;
    }

    public float evade(GameObject collidingObj) {
        float xDistance = Math.abs(this.position.x - collidingObj.position.x);
        float zDistance = Math.abs(this.position.z - collidingObj.position.z);
        float lengthDirectionalVector = (float) Math.sqrt(Math.pow(xDistance, 2) + Math.pow(zDistance, 2));
        Vector3f directionalVector = new Vector3f(xDistance, 0, zDistance);
        Vector3f rotationalVector = new Vector3f((float) (Math.cos(this.rotation.y % 360) / this.radius), 0, ((float) Math.sin(this.rotation.y % 360) / this.radius));
        float lengthRotationalVector = (float) Math.sqrt(Math.pow(rotationalVector.x, 2) + Math.pow(rotationalVector.z, 2));
        float angle = (directionalVector.x * rotationalVector.x + directionalVector.y * rotationalVector.y + directionalVector.z * rotationalVector.z)
                / (lengthDirectionalVector * lengthRotationalVector);
        // TODO calculate proper angle and define left and right
        return 1;
    }
}
