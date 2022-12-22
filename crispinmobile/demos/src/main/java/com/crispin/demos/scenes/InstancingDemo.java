package com.crispin.demos.scenes;

import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES30.GL_ARRAY_BUFFER;
import static android.opengl.GLES30.GL_FLOAT;
import static android.opengl.GLES30.GL_STATIC_DRAW;
import static android.opengl.GLES30.GL_TEXTURE0;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glActiveTexture;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glDrawArraysInstanced;
import static android.opengl.GLES30.glEnableVertexAttribArray;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniformMatrix4fv;
import static android.opengl.GLES30.glVertexAttribDivisor;
import static android.opengl.GLES30.glVertexAttribPointer;

import static com.crispin.crispinmobile.Rendering.Utilities.RenderObject.BYTES_PER_FLOAT;

import android.opengl.GLES30;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Models.Cube;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.UserInterface.Button;
import com.crispin.crispinmobile.UserInterface.Font;
import com.crispin.crispinmobile.UserInterface.TouchEvent;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;

import java.nio.FloatBuffer;
import java.util.Random;

public class InstancingDemo extends Scene {
    class InstanceTextureShader extends Shader {
        // Attribute for the instances of model matrices
        public int modelMatrixAttributeHandle;

        public InstanceTextureShader() {
            super("Texture Instance Demo Shader", R.raw.instance_texture_vert, R.raw.instance_texture_frag);
            positionAttributeHandle = getAttribute("aPosition");
            modelMatrixAttributeHandle = getAttribute("aModel");
            textureAttributeHandle = getAttribute("aTextureCoordinates");

            materialHandles = new MaterialHandles();
            materialHandles.textureUniformHandle = getUniform("uTexture");

            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");
        }
    }

    class InstanceGlobalColourShader extends Shader {
        // Attribute for the instances of model matrices
        public int modelMatrixAttributeHandle;

        public InstanceGlobalColourShader() {
            super("Global Colour Instance Demo Shader", R.raw.instance_global_colour_vert, R.raw.instance_global_colour_frag);
            positionAttributeHandle = getAttribute("aPosition");
            modelMatrixAttributeHandle = getAttribute("aModel");

            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");

            materialHandles = new MaterialHandles();
            materialHandles.colourUniformHandle = getUniform("uColour");
        }
    }

    class InstanceColourShader extends Shader {
        // Attribute for the instances of model matrices
        public int modelMatrixAttributeHandle;

        public InstanceColourShader() {
            super("Global Colour Instance Demo Shader", R.raw.instance_colour_vert, R.raw.instance_colour_frag);
            positionAttributeHandle = getAttribute("aPosition");
            colourAttributeHandle = getAttribute("aColour");
            modelMatrixAttributeHandle = getAttribute("aModel");

            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");
        }
    }

    private final Camera camera;
    private float cameraZCount = 0.0f;
    private int instanceVBO;
    private Material crateMaterial;
    private boolean renderTextureInstances;
    private boolean renderGlobalColourInstances;

    // Cubes (for each render type)
    private Model texelCube;
    private Model plainCube;
   // private Model plainCube2;

    // Shaders
    private InstanceTextureShader textureInstanceShader;
    private InstanceGlobalColourShader instanceGlobalColourShader;
    private InstanceColourShader instanceColourShader;

    // UI
    private Camera2D uiCamera;
    private Button toggleTextureInstances;
    private Button toggleGlobalColourInstances;

    private final int NUM_INSTANCES = 200000;
    private final int vec4Size = BYTES_PER_FLOAT * 4;
    private final int stride = 4 * vec4Size; // we want to stride of 4x vec4's (jump a mat4 num bytes each time)
    private final Scale3D boxSize = new Scale3D(30.0f, 30.0f, 100.0f);
    private final int numMatrices = NUM_INSTANCES;
    private final int numFloatsPerMatrix = 16;
    private final int totalNumFloats = numMatrices * numFloatsPerMatrix;
    private final int totalBytes = totalNumFloats * 4;
    private final int numBytesPerMatrix = numFloatsPerMatrix * 4;
    private final Colour globalColour = Colour.GREEN;

    public InstancingDemo() {
        Crispin.setBackgroundColour(Colour.LIGHT_GREY);
        Crispin.setCullFaceState(true);

        renderTextureInstances = true;

        initUI();

        // Create shaders
        textureInstanceShader = new InstanceTextureShader();
        instanceGlobalColourShader = new InstanceGlobalColourShader();
        instanceColourShader = new InstanceColourShader();

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, boxSize.z));

        // Create texture cube models
        crateMaterial = new Material(R.drawable.crate_texture);
        texelCube = new Cube(true, false, false);
        texelCube.useCustomShader(textureInstanceShader);
        instanceVBO = generateInstanceData(textureInstanceShader.modelMatrixAttributeHandle, texelCube.vao);

        // Create global colour cube model
        plainCube = new Cube(false, false, false);
        plainCube.useCustomShader(instanceGlobalColourShader);
        generateInstanceData(instanceGlobalColourShader.modelMatrixAttributeHandle, plainCube.vao);

//        // Create colour cube model
//        plainCube2 = new Cube(false, false, false);
//        plainCube2.useCustomShader(instanceColourShader);
//        generateInstanceData(instanceColourShader.modelMatrixAttributeHandle, plainCube2.vao);

        setPosition(1, 0.0f, -5.0f, 35.0f);
    }

    private int generateInstanceData(int modelMatrixAttributeHandle, int meshVao) {
        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(totalNumFloats);

        Random r = new Random();
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (r.nextFloat() * boxSize.x) - (boxSize.x/2.0f);
            float y = (r.nextFloat() * boxSize.y) - (boxSize.y/2.0f);
            float z = (r.nextFloat() * boxSize.z) - (boxSize.z/2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 32f;

            ModelMatrix modelMatrix = new ModelMatrix();
            modelMatrix.translate(x, y, z);
            modelMatrix.rotate(rotateAngle, 0.4f, 0.6f, 0.8f);
            modelMatrix.scale(scale);
            modelMatrixBuffer.put(modelMatrix.getModelMatrix());
        }
        modelMatrixBuffer.position(0);

        int[] instanceVBO = new int[1];
        GLES30.glGenBuffers(1, instanceVBO, 0);
        GLES30.glBindBuffer(GL_ARRAY_BUFFER, instanceVBO[0]);
        GLES30.glBufferData(GL_ARRAY_BUFFER, totalBytes, modelMatrixBuffer, GL_STATIC_DRAW);

        bindModelMatrixInstanceVAO(modelMatrixAttributeHandle, meshVao);

        return instanceVBO[0];
    }

    private void bindModelMatrixInstanceVAO(int modelMatrixAttributeHandle, int instanceVAO) {
        glBindVertexArray(instanceVAO);

        int h = modelMatrixAttributeHandle;
        glEnableVertexAttribArray(h);
        glVertexAttribPointer(h, 4, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(h + 1);
        glVertexAttribPointer(h + 1, 4, GL_FLOAT, false, stride, vec4Size);
        glEnableVertexAttribArray(h + 2);
        glVertexAttribPointer(h + 2, 4, GL_FLOAT, false, stride, 2 * vec4Size);
        glEnableVertexAttribArray(h + 3);
        glVertexAttribPointer(h + 3, 4, GL_FLOAT, false, stride, 3 * vec4Size);

        glVertexAttribDivisor(h, 1);
        glVertexAttribDivisor(h + 1, 1);
        glVertexAttribDivisor(h + 2, 1);
        glVertexAttribDivisor(h + 3, 1);

        glBindVertexArray(0);
    }

    private void initUI() {
        uiCamera = new Camera2D();
        Font buttonFont = new Font(R.raw.aileron_regular, 24);
        toggleTextureInstances = new Button(buttonFont, "Toggle Texture Instances");
        toggleTextureInstances.setPosition(10, 10);
        toggleTextureInstances.setSize(200, 200);
        toggleTextureInstances.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                renderTextureInstances = !renderTextureInstances;
            }
        });

        toggleGlobalColourInstances = new Button(buttonFont, "Toggle Global Colour Instances");
        toggleGlobalColourInstances.setPosition(10, 220);
        toggleGlobalColourInstances.setSize(200, 200);
        toggleGlobalColourInstances.addTouchListener(e -> {
            if(e.getEvent() == TouchEvent.Event.CLICK) {
                renderGlobalColourInstances = !renderGlobalColourInstances;
            }
        });
    }

    // An example of how to set the position/model matrix of just one of the objects in the group,
    // meaning that each object can still have on-demand position, scale and rotation changes
    private void setPosition(int index, float x, float y, float z) {
        FloatBuffer modelMatrixBuffer = FloatBuffer.allocate(numFloatsPerMatrix);
        ModelMatrix modelMatrix = new ModelMatrix();
        modelMatrix.translate(x, y, z);
        modelMatrixBuffer.put(modelMatrix.getModelMatrix());
        modelMatrixBuffer.position(0);

        GLES30.glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
        GLES30.glBufferSubData(GL_ARRAY_BUFFER,index * numBytesPerMatrix, numBytesPerMatrix, modelMatrixBuffer);
    }

    @Override
    public void update(float deltaTime) {
        // Update camera position to move in out
        cameraZCount += 0.005f * deltaTime;
        float cameraZ = (((float)Math.sin(cameraZCount)) * (boxSize.z/2f)) + 10.0f;
        camera.setPosition(0.0f, 0.0f, cameraZ);
    }

    @Override
    public void render() {
        if(renderTextureInstances) {
            renderTextureInstances();
        }

        if(renderGlobalColourInstances) {
            renderGlobalColourInstances();
        }

        renderUI();
    }

    private void renderTextureInstances() {
        textureInstanceShader.enable();

        // Set texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, crateMaterial.texture.getId());
        glUniform1i(textureInstanceShader.materialHandles.textureUniformHandle, 0);

        // Set view matrices
        glUniformMatrix4fv(textureInstanceShader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(textureInstanceShader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);

        // Draw instances
        glBindVertexArray(texelCube.vao);
        glDrawArraysInstanced(GL_TRIANGLES, 0, texelCube.vertexCount, NUM_INSTANCES);
        glBindVertexArray(0);

        textureInstanceShader.disable();
    }

    private void renderGlobalColourInstances() {
        instanceGlobalColourShader.enable();

        // Set global uniform colour
        glUniform4f(instanceGlobalColourShader.materialHandles.colourUniformHandle, globalColour.red, globalColour.green, globalColour.blue, globalColour.alpha);

        // Set view matrices
        glUniformMatrix4fv(instanceGlobalColourShader.getProjectionMatrixUniformHandle(),1,false, camera.getPerspectiveMatrix(), 0);
        glUniformMatrix4fv(instanceGlobalColourShader.getViewMatrixUniformHandle(), 1, false, camera.getViewMatrix(), 0);

        // Draw instances
        glBindVertexArray(plainCube.vao);
        glDrawArraysInstanced(GL_TRIANGLES, 0, plainCube.vertexCount, NUM_INSTANCES);
        glBindVertexArray(0);

        instanceGlobalColourShader.disable();
    }

    private void renderUI() {
        toggleTextureInstances.draw(uiCamera);
        toggleGlobalColourInstances.draw(uiCamera);
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}