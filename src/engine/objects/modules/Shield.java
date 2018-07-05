package engine.objects.modules;

public class Shield extends Module {

    private float magnitude;
    private float maxShield;
    private float rechargeRate;
    private long lastTime;
    private long rechargeDelay;

    public Shield(ModPosition position, float magnitude, float rechargeRate, long rechargeTime) {
        super(position);
        this.rechargeDelay = rechargeTime;
        this.rechargeRate = rechargeRate;
        this.magnitude = magnitude;
        maxShield = magnitude;
        unload();
    }

    public void unload() {
        lastTime = System.currentTimeMillis();
    }

    public boolean isReady() {
        return magnitude > 0;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void rechargeShield() {
        // TODO once expired time since last use is calculated properly, set -1 to rechargeDelay
        System.out.println(System.currentTimeMillis() + "                   " + lastTime);
        if(System.currentTimeMillis() - lastTime > rechargeDelay) {
            if(magnitude < maxShield) {
                magnitude += rechargeRate;
            }
            if(magnitude > maxShield) {
                magnitude = maxShield;
            }
        }
    }

    public float dealDamage(float damage) {
        magnitude -= damage;
        float overkill = 0.0f;
        if(magnitude < 0) {
            overkill = (-1) * magnitude;
            magnitude = 0;
        }
        return overkill;
    }
}
