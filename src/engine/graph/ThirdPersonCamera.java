package engine.graph;

import engine.input.MouseInput;
import engine.objects.GameObject;
import org.joml.Vector2f;

public class ThirdPersonCamera extends Camera {

    private GameObject actor;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private float distance;
    private float angleAroundActor;

    public void init(GameObject actor, float distance) {
        this.actor = actor;
        this.distance = distance;
        angleAroundActor = 0;
    }

    public void changeDistance(float offset) {
        distance += offset;
    }

    public void moveAlong(MouseInput mouseInput) {
        // manually move camera around person
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            moveHorizontally(rotVec.y * MOUSE_SENSITIVITY);
            moveVertically(rotVec.x * MOUSE_SENSITIVITY);
        }

        float scrollOffset = mouseInput.getScrollDiff();
        scrollOffset *= 0.3;
        if(distance >= 3f && distance <= 12f) {
            distance -= scrollOffset;
            if(distance < 3f) {
                distance = 3f;
            } else if (distance > 12f) {
                distance = 12f;
            }
        }
        mouseInput.resetScrollDiff();

        // calculate y
        float horizontalDistance = (float) (distance * Math.cos(Math.toRadians(rotation.x)));
        float verticalDistance = (float) (distance * Math.sin(Math.toRadians(rotation.x)));

        // calculate z and x distance from camera
        float theta = angleAroundActor;
        // this is disabled because it causes stuttering
        //float theta =  angleAroundActor - actor.getRotation().y;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        // calculate the camera position
        position.x = actor.getPosition().x - offsetX;
        position.y = actor.getPosition().y + verticalDistance;
        position.z = actor.getPosition().z - offsetZ;

        // rotate the camera to always look at the person
        rotation.y = 180 - theta;
    }

    private void moveVertically(float offsetX) {
        rotation.x += offsetX;
    }

    private void moveHorizontally(float offsetY) {
        angleAroundActor -= offsetY;
    }
}
