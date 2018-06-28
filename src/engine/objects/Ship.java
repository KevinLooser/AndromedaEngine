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
        super(mesh);
        this.frontWeapon = new Front(Module.ModPosition.FRONT, 0, 0, frontMesh);
        this.broadsidesWeapon = new Broadsides(Module.ModPosition.BROADSIDES, 0, 10f, 3, 1f, broadsidesMesh);
        this.gear = new Gear(Module.ModPosition.DECK, 0);
        this.thruster = new Thruster(Module.ModPosition.THRUSTERS, 0);
    }

    public Ship(Mesh mesh, Mesh frontMesh, Mesh broadsidesMesh, float speed, float acceleration, float durability) {
        super(mesh, speed, acceleration, durability);
        this.frontWeapon = new Front(Module.ModPosition.FRONT, 0, 0, frontMesh);
        this.broadsidesWeapon = new Broadsides(Module.ModPosition.BROADSIDES, 0, 7f, 2, 1f, broadsidesMesh);
        this.gear = new Gear(Module.ModPosition.DECK, 0);
        this.thruster = new Thruster(Module.ModPosition.THRUSTERS, 0);
    }

    public void steer(float offsetY) {
        super.rotation.y += offsetY;
    }

    public Missile shootFrontal() {
        Missile missile = ((Front) frontWeapon).load(getPosition(), getRotation());
        System.out.println("Missile released.");
        return missile;
    }

    public List<Missile> shootLeftSide() {
        List<Missile> missiles = ((Broadsides) broadsidesWeapon).load(getPosition(), getRotation(), length, width, Broadsides.Side.LEFT);
        return missiles;
    }

    public List<Missile> shootRightSide() {
        List<Missile> missiles = ((Broadsides) broadsidesWeapon).load(getPosition(), getRotation(), length, width, Broadsides.Side.RIGHT);
        return missiles;
    }

    public void boost() {

    }

    public void useGear() {

    }
}
