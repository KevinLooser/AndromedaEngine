package engine.objects.modules;

import engine.objects.modules.Module;

public class Gear extends Module {

    private float cooldown;

    public Gear(ModPosition position, float cooldown) {
        super(position);
        this.cooldown = cooldown;
    }
}
