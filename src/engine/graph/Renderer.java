package engine.graph;

import engine.*;
import engine.objects.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private Shader shader;
    private Shader hudShader;
    private Shader skyBoxShader;

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
        setupSkyboxShader();
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

    public void setupSkyboxShader() throws Exception {
        skyBoxShader = new Shader();
        skyBoxShader.createVertexShader(Utils.loadResource("../resources/shaders/skybox_vertex.vs"));
        skyBoxShader.createFragmentShader(Utils.loadResource("../resources/shaders/skybox_fragment.fs"));
        skyBoxShader.link();

        skyBoxShader.createUniform("projectionMatrix");
        skyBoxShader.createUniform("modelViewMatrix");
        skyBoxShader.createUniform("texture_sampler");
        skyBoxShader.createUniform("ambientLight");
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

    public void render(Window window, Camera camera, Scene scene, IHud hud) {
        clear();

        if(window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        renderScene(window, camera, scene);
        // TODO: FIX the skybox moves faster than the ship it is centered on. temporarily, it will be treated as a gameobject until fixed
        //renderSkyBox(window, camera, scene);
        renderHud(window, hud);
    }

    private void renderScene(Window window, Camera camera, Scene scene) {
        shader.bind();

        // update projection matrix
        Matrix4f projectionMatrix = transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniforms("projectionMatrix", projectionMatrix);
        shader.setUniforms("texture_sampler", 0);

        // update view matrix
        Matrix4f viewMatrix = transformation.updateViewMatrix(camera);

        // update light uniforms
        renderLights(viewMatrix, scene.getSceneLight());
        shader.setUniforms("texture_sampler", 0);

        // render game objects
        Map<Mesh, List<GameObject>> mapMeshes = scene.getGameMeshes();
        for(Mesh mesh : mapMeshes.keySet()) {
            shader.setUniforms("material", mesh.getMaterial());
            mesh.renderList(mapMeshes.get(mesh), (GameObject gameObject) -> {
                Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameObject, viewMatrix);
                shader.setUniforms("modelViewMatrix", modelViewMatrix);
            });
        }

        shader.unbind();
    }

    private void renderSkyBox(Window window, Camera camera, Scene scene) {
        skyBoxShader.bind();

        skyBoxShader.setUniforms("texture_sampler", 0);

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        skyBoxShader.setUniforms("projectionMatrix", projectionMatrix);
        SkyBox skyBox = scene.getSkyBox();
        Matrix4f viewMatrix = transformation.updateViewMatrix(camera);
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(skyBox, viewMatrix);
        skyBoxShader.setUniforms("modelViewMatrix", modelViewMatrix);
        skyBoxShader.setUniforms("ambientLight", scene.getSceneLight().getAmbientLight());

        scene.getSkyBox().getMesh().render();

        skyBoxShader.unbind();
    }

    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

        shader.setUniforms("ambientLight", sceneLight.getAmbientLight());
        shader.setUniforms("specularPower", specularPower);

        // Process Point Lights
        int numLights = sceneLight.getPointLightList()!= null ? sceneLight.getPointLightList().length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(sceneLight.getPointLightList()[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shader.setUniforms("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        numLights = sceneLight.getSpotLightList() != null ? sceneLight.getSpotLightList().length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(sceneLight.getSpotLightList()[i]);
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
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shader.setUniforms("directionalLight", currDirLight);

    }

    private void renderHud(Window window, IHud hud) {
        hudShader.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for(GameObject gameObject : hud.getGameObjects()) {
            Mesh mesh = gameObject.getMesh();
            Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(gameObject, ortho);
            hudShader.setUniforms("projModelMatrix", projModelMatrix);
            hudShader.setUniforms("colour", gameObject.getMesh().getMaterial().getAmbientColour());

            mesh.render();
        }

        hudShader.unbind();
    }

    public void cleanup() {
        if(shader != null) {
            shader.cleanup();
        }
        if(skyBoxShader != null) {
            skyBoxShader.cleanup();
        }
        if(hudShader != null) {
            hudShader.cleanup();
        }
    }
}
