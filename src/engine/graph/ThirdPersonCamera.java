package engine.graph;

import engine.input.MouseInput;
import engine.objects.GameObject;
import org.joml.Vector2f;

public class ThirdPersonCamera extends Camera {

    private GameObject person;
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private float distance;
    private float angle;

    public void init(GameObject person, float distance) {
        this.person = person;
        this.distance = distance;
        angle = 0;
    }

    public void moveAlong(MouseInput mouseInput) {

        // manually move camera around person
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            moveHorizontally(rotVec.y * MOUSE_SENSITIVITY);
            moveVertically(rotVec.x * MOUSE_SENSITIVITY);
        }

        // calculate y
        float horizontalDistance = (float) (distance * Math.cos(Math.toRadians(rotation.x)));
        float verticalDistance = (float) (distance * Math.sin(Math.toRadians(rotation.x)));

        // calculate z and x distance from camera
        float theta = person.getRotation().y + angle;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        // calculate the camera position
        position.x = person.getPosition().x - offsetX;
        position.y = person.getPosition().y + verticalDistance;
        position.z = person.getPosition().z - offsetZ;

        // rotate the camera to always look at the person
        rotation.y = 180 - theta;
    }

    private void moveVertically(float offsetX) {
        rotation.x += offsetX;
    }

    private void moveHorizontally(float offsetY) {
        angle -= offsetY;
    }
}
