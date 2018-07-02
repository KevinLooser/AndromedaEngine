package engine.objects;

import java.util.ArrayList;
import java.util.List;

public class ObstacleGroup extends GameObject {

    List<Obstacle> obstacleList;

    public ObstacleGroup() {
        obstacleList = new ArrayList<>();
    }

    public void add (Obstacle obstacle) {
        obstacleList.add(obstacle);
    }

    public void addAll (List<Obstacle> obstacles) {
        obstacleList.addAll(obstacles);
    }

    public void remove (int id) {
        obstacleList.remove(id);
    }

    public void remove (Obstacle obstacle) {
        obstacleList.remove(obstacle);
    }

    public boolean contains (Obstacle obstacle) {
        return obstacleList.contains(obstacle);
    }

    public int size () {
        return obstacleList.size();
    }

    public Obstacle get (int id) {
        return obstacleList.get(id);
    }
}
