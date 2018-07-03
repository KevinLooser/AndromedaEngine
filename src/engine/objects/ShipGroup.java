package engine.objects;

import java.util.ArrayList;
import java.util.List;

public class ShipGroup extends GameObject {

    List<Ship> shipList;

    public ShipGroup() {
        shipList = new ArrayList<>();
    }

    public void add (Ship ship) {
        shipList.add(ship);
    }

    public void addAll (List<Ship> ship) {
        shipList.addAll(ship);
    }

    public void remove (int id) {
        shipList.remove(id);
    }

    public void remove (Ship ship) {
        shipList.remove(ship);
    }

    public boolean contains (Ship ship) {
        return shipList.contains(ship);
    }

    public int size () {
        return shipList.size();
    }

    public Ship get (int id) {
        return shipList.get(id);
    }
}
