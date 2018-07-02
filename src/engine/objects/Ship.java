package engine.objects;

import engine.graph.Mesh;
import engine.objects.modules.*;
import engine.objects.modules.Module;

import java.util.List;

public class Ship extends GameObject {

    private Weapon frontWeapon;
    private Weapon broadsidesWeapon;
    private Gear gear;
    private Thruster thruster;
    private float length = 4f;
    private float width = 2f;

    public Ship(Mesh mesh, Mesh frontMesh, Mesh broadsidesMesh) {
        super(mesh, 3f);
        this.frontWeapon = new Front(Module.ModPosition.FRONT, 0, length, frontMesh);
        this.broadsidesWeapon = new Broadsides(Module.ModPosition.BROADSIDES, 1, 3, 1f, broadsidesMesh);
        this.gear = new Gear(Module.ModPosition.DECK, 0);
        this.thruster = new Thruster(Module.ModPosition.THRUSTERS, 0);
    }

    public Ship(Mesh mesh, Mesh frontMesh, Mesh broadsidesMesh, float speed, float acceleration, float durability) {
        super(mesh, 4f, speed, acceleration, durability);
        this.frontWeapon = new Front(Module.ModPosition.FRONT, 0, length, frontMesh);
        this.broadsidesWeapon = new Broadsides(Module.ModPosition.BROADSIDES, 1, 3, 1f, broadsidesMesh);
        this.gear = new Gear(Module.ModPosition.DECK, 0);
        this.thruster = new Thruster(Module.ModPosition.THRUSTERS, 0);
    }

    public void steer(float offsetY) {
        super.rotation.y += offsetY;
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
        // TODO: movement speed boost with a magnitude and duration
    }

    public void useGear() {
        // TODO: effect (eg. shield) with magnitude and duration
    }
}
