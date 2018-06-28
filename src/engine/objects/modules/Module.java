package engine.objects.modules;

public abstract class Module {

    protected ModPosition position;

    public Module(ModPosition position) {
        this.position = position;
    }

    public enum ModPosition {
        FRONT, BROADSIDES, DECK, THRUSTERS
    }
}
