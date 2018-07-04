package engine.objects.modules;

public class Thruster extends Module {

    private float magnitude;
    private long lastTime;
    private float fuel;
    private final float maxFuel;
    private float rechargeRate;
    private long rechargeDelay;

    public Thruster(ModPosition position, float magnitude, float fuel, float rechargeRate, long rechargeDelay) {
        super(position);
        this.magnitude = magnitude;
        this.fuel = fuel;
        maxFuel = fuel;
        this.rechargeRate = rechargeRate;
        this.rechargeDelay = rechargeDelay;
        unload();
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getFuel() {
        return fuel;
    }

    public void unload() {
        lastTime = System.currentTimeMillis();
    }

    public boolean hasFuel() {
        return fuel > 0;
    }

    public void drainFuel() {
        fuel -= magnitude;
        if(fuel < 0) {
            fuel = 0f;
        }
    }

    public void rechargeFuel() {
        // TODO once expired time since last use is calculated properly, set -1 to rechargeDelay
        if(System.currentTimeMillis() - lastTime > -1) {
            if(fuel < maxFuel) {
                fuel += rechargeRate;
            }
            if(fuel > maxFuel) {
                fuel = maxFuel;
            }
        }
    }
}
