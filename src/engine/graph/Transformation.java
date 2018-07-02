package engine.graph;

import engine.objects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f orthoMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f modelViewMatrix;

    public Transformation() {
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        orthoMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public final Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f updateViewMatrix(Camera camera) {
        Vector3f position = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0)).rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }

    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        orthoMatrix.identity();
        orthoMatrix.setOrtho2D(left, right, bottom, top);
        return orthoMatrix;
    }

    public Matrix4f buildModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();
        modelMatrix.identity().translate(gameObject.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());
        modelViewMatrix.set(viewMatrix);
        return modelViewMatrix.mul(modelMatrix);
    }

    public Matrix4f buildOrtoProjModelMatrix(GameObject gameObject, Matrix4f orthoMatrix) {
        Vector3f rotation = gameObject.getRotation();
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity().translate(gameObject.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameObject.getScale());
        Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
        return orthoMatrixCurr.mul(modelMatrix);
    }
}
