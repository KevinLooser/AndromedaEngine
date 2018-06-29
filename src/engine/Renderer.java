package engine;

import engine.graph.*;
import engine.objects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private Shader shader;
    private Shader hudShader;

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;
    private static final int MAX_POINT_LIGHTS = 0;
    private static final int MAX_SPOT_LIGHTS = 0;

    private Transformation transformation;
    private float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }

    public void init(Window window) throws Exception {
        setupSceneShader();
        setupHudShader();
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void setupSceneShader() throws Exception {
        shader = new Shader();
        shader.createVertexShader(Utils.loadResource("../resources/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("../resources/shaders/fragment.fs"));
        shader.link();

        // create uniforms for matrices and texture
        shader.createUniform("projectionMatrix");
        shader.createUniform("modelViewMatrix");
        shader.createUniform("texture_sampler");
        // create material uniforms
        shader.createMaterialUniforms("material");
        // create light uniforms
        shader.createUniform("specularPower");
        shader.createUniform("ambientLight");
        shader.createPointLightListUniforms("pointLight", MAX_POINT_LIGHTS);
        shader.createSpotLightListUniforms("spotLight", MAX_SPOT_LIGHTS);
        shader.createDirectionalLightUniforms("directionalLight");
    }

    public void setupHudShader() throws Exception {
        hudShader = new Shader();
        hudShader.createVertexShader(Utils.loadResource("../resources/shaders/hud_vertex.vs"));
        hudShader.createFragmentShader(Utils.loadResource("../resources/shaders/hud_fragment.fs"));
        hudShader.link();

        // Create uniforms for Ortographic-model projection matrix and base colour
        hudShader.createUniform("projModelMatrix");
        hudShader.createUniform("colour");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, List<GameObject> gameObjects, SceneLight sceneLight, IHud hud) {
        clear();

        if(window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        renderScene(window, camera, gameObjects, sceneLight);
        renderHud(window, hud);
    }

    private void renderScene(Window window, Camera camera, List<GameObject> gameObjects, SceneLight sceneLight) {
        shader.bind();

        // update projection matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniforms("projectionMatrix", projectionMatrix);
        shader.setUniforms("texture_sampler", 0);

        // update view matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // update light uniforms
        renderLights(viewMatrix, sceneLight.getAmbientLight(), sceneLight.getPointLightList(), sceneLight.getSpotLightList(), sceneLight.getDirectionalLight());
        shader.setUniforms("texture_sampler", 0);

        // render game objects
        for(GameObject gameObject : gameObjects) {
            Mesh mesh = gameObject.getMesh();
            // set matrix for this object
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameObject, viewMatrix);
            shader.setUniforms("modelViewMatrix", modelViewMatrix);
            // render the mesh
            shader.setUniforms("material", mesh.getMaterial());
            mesh.render();
        }

        shader.unbind();
    }

    private void renderLights(Matrix4f viewMatrix, Vector3f ambientLight, PointLight[] pointLightList, SpotLight[] spotLightList, DirectionalLight directionalLight) {

        shader.setUniforms("ambientLight", ambientLight);
        shader.setUniforms("specularPower", specularPower);

        // Process Point Lights
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shader.setUniforms("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        numLights = spotLightList != null ? spotLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLight.getPointLight().getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            shader.setUniforms("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shader.setUniforms("directionalLight", currDirLight);

    }

    private void renderHud(Window window, IHud hud) {
        hudShader.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for(GameObject gameObject : hud.getGameItems()) {
            Mesh mesh = gameObject.getMesh();
            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameObject, ortho);
            hudShader.setUniforms("projModelMatrix", projModelMatrix);
            hudShader.setUniforms("colour", gameObject.getMesh().getMaterial().getAmbientColour());

            mesh.render();
        }
    }

    public void cleanup() {
        if(shader != null) {
            shader.cleanup();
        }
    }
}
