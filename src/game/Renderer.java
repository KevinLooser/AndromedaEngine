package game;

import engine.*;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.Shader;
import engine.graph.Transformation;
import engine.objects.GameObject;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private Shader shader;
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    // constants for uniforms
    private static final String PROJECTION_MATRIX = "projectionMatrix";
    private static final String MODEL_VIEW_MATRIX = "modelViewMatrix";
    private static final String TEXTURE_SAMPLER = "texture_sampler";
    private static final String COLOUR = "colour";
    private static final String USE_COLOUR = "useColour";

    private Transformation transformation;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        shader = new Shader();
        shader.createVertexShader(Utils.loadResource("../resources/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("../resources/fragment.fs"));
        shader.link();

        shader.createUniform(PROJECTION_MATRIX);
        shader.createUniform(MODEL_VIEW_MATRIX);
        shader.createUniform(TEXTURE_SAMPLER);
        shader.createUniform(COLOUR);
        shader.createUniform(USE_COLOUR);

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, List<GameObject> gameObjects) {
        clear();

        if(window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shader.bind();

        // update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniforms(PROJECTION_MATRIX, projectionMatrix);
        shader.setUniforms(TEXTURE_SAMPLER, 0);

        // update view matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // render game objects
        for(GameObject gameObject : gameObjects) {
            Mesh mesh = gameObject.getMesh();
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shader.setUniforms(MODEL_VIEW_MATRIX, modelViewMatrix);
            // render the mesh
            shader.setUniforms(COLOUR, mesh.getColour());
            // if the mesh is without texture, use the color
            shader.setUniforms(USE_COLOUR, mesh.isTextured() ? 0 : 1);
            mesh.render();
        }

        shader.unbind();
    }

    public void cleanup() {
        if(shader != null) {
            shader.cleanup();
        }
    }
}
