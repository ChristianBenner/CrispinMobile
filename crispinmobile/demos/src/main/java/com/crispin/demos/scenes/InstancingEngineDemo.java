package com.crispin.demos.scenes;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1i;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.Models.Cube;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.InstanceTextureShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.InstanceRenderer;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.crispinmobile.Utilities.TextureCache;
import com.crispin.demos.R;

import java.util.Random;

public class InstancingEngineDemo extends Scene {
    private final int NUM_INSTANCES = 200000;
    private final Scale3D GENERATION_AREA_SIZE = new Scale3D(30.0f, 30.0f, 100.0f);

    private InstanceRenderer instanceRenderer;
    private final Camera camera;
    private float cameraZCount = 0.0f;

    public InstancingEngineDemo() {
        Crispin.setBackgroundColour(Colour.GREY);

        camera = new Camera();
        camera.setPosition(new Vec3(0.0f, 0f, GENERATION_AREA_SIZE.z));

        Random r = new Random();
        ModelMatrix[] matrices = new ModelMatrix[NUM_INSTANCES];
        for(int i = 0; i < NUM_INSTANCES; i++) {
            float x = (r.nextFloat() * GENERATION_AREA_SIZE.x) - (GENERATION_AREA_SIZE.x/2.0f);
            float y = (r.nextFloat() * GENERATION_AREA_SIZE.y) - (GENERATION_AREA_SIZE.y/2.0f);
            float z = (r.nextFloat() * GENERATION_AREA_SIZE.z) - (GENERATION_AREA_SIZE.z/2.0f);
            float rotateAngle = r.nextFloat() * 360.0f;
            float scale = (1.0f + r.nextFloat()) / 32f;

            ModelMatrix modelMatrix = new ModelMatrix();
            modelMatrix.translate(x, y, z);
            modelMatrix.rotate(rotateAngle, 0.4f, 0.6f, 0.8f);
            modelMatrix.scale(scale);
            matrices[i] = modelMatrix;
        }

        instanceRenderer = new InstanceRenderer(new CubeMesh(true, false));
        instanceRenderer.setTexture(TextureCache.loadTexture(R.drawable.crate_texture));
        instanceRenderer.add(matrices);
    }

    @Override
    public void update(float deltaTime) {
        // Update camera position to move in out
        cameraZCount += 0.005f * deltaTime;
        float cameraZ = (((float)Math.sin(cameraZCount)) * (GENERATION_AREA_SIZE.z/2f)) + 10.0f;
        camera.setPosition(0.0f, 0.0f, cameraZ);
    }

    @Override
    public void render() {
        instanceRenderer.render(camera);
    }

    @Override
    public void touch(int type, Vec2 position) {

    }
}