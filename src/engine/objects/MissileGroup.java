package engine.objects;

import java.util.ArrayList;
import java.util.List;

public class MissileGroup extends GameObject {

    List<Missile> missileList;

    public MissileGroup() {
        missileList = new ArrayList<>();
    }

    public void add (Missile missile) {
        missileList.add(missile);
    }

    public void addAll (List<Missile> missiles) {
        missileList.addAll(missiles);
    }

    public void remove (int id) {
        missileList.remove(id);
    }

    public void remove (Missile missile) {
        missileList.remove(missile);
    }

    public boolean contains (Missile missile) {
        return missileList.contains(missile);
    }

    public int size () {
        return missileList.size();
    }

    public Missile get (int id) {
        return missileList.get(id);
    }
}
