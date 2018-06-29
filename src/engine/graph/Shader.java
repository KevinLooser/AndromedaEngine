package engine.graph;

import javafx.scene.effect.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public Shader() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create shader");
        }
        uniforms = new HashMap<>();
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if(shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if(uniformLocation < 0) {
            throw new Exception("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void createPointLightListUniforms(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createPointLightUniforms(uniformName + "[" + i + "]");
        }
    }

    public void createPointLightUniforms(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createSpotLightListUniforms(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createSpotLightUniforms(uniformName);
        }
    }

    public void createSpotLightUniforms(String uniformName) throws Exception {
        createPointLightUniforms(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void createDirectionalLightUniforms(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createMaterialUniforms(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }


    public void setUniforms(String uniformName, Matrix4f value) {
        // use automated memory allocation
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            FloatBuffer fb = memoryStack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void setUniforms(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniforms(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniforms(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniforms(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniforms(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniforms(uniformName, pointLights[i], i);
        }
    }

    public void setUniforms(String uniformName, PointLight pointLight, int pos) {
        setUniforms(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniforms(String uniformName, PointLight pointLight) {
        setUniforms(uniformName + ".colour", pointLight.getColor());
        setUniforms(uniformName + ".position", pointLight.getPosition());
        setUniforms(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniforms(uniformName + ".att.constant", att.getConstant());
        setUniforms(uniformName + ".att.linear", att.getLinear());
        setUniforms(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniforms(String uniformName, SpotLight spotLight) {
        setUniforms(uniformName + ".pl", spotLight.getPointLight());
        setUniforms(uniformName + ".conedir", spotLight.getConeDirection());
        setUniforms(uniformName + ".cutoff", spotLight.getCutOff());
    }

    public void setUniforms(String uniformName, DirectionalLight dirLight) {
        setUniforms(uniformName + ".colour", dirLight.getColor() );
        setUniforms(uniformName + ".direction", dirLight.getDirection());
        setUniforms(uniformName + ".intensity", dirLight.getIntensity());
    }

    public void setUniforms(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniforms(uniformName, spotLights[i], i);
        }
    }

    public void setUniforms(String uniformName, SpotLight spotLight, int pos) {
        setUniforms(uniformName + "[" + pos + "]", spotLight);
    }

    public void setUniforms(String uniformName, Material material) {
        setUniforms(uniformName + ".ambient", material.getAmbientColour());
        setUniforms(uniformName + ".diffuse", material.getDiffuseColour());
        setUniforms(uniformName + ".specular", material.getSpecularColour());
        setUniforms(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniforms(uniformName + ".reflectance", material.getReflectance());
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader code: " + glGetProgramInfoLog(programId, 1024));
        }
        if(vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if(fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }
        // for debugging purposes, remove once product is finished
        glValidateProgram(programId);
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            throw new Exception("Warning validating shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if(programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
