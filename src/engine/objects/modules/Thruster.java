package engine.objects.modules;

public class Thruster extends Module {

    private float boost;

    public Thruster(ModPosition position, float boost) {
        super(position);
        this.boost = boost;
    }
}
