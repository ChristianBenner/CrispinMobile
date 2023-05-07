package com.crispin.demos.Scenes;

import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glUniform3f;

import android.view.MotionEvent;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.DefaultMesh.CubeMesh;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Models.Model;
import com.crispin.crispinmobile.Rendering.Shaders.Handles.MaterialHandles;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.UserInterface.Pointer;
import com.crispin.crispinmobile.Utilities.Scene;
import com.crispin.demos.R;

public class NormalMapDemo extends Scene {
    class NormalMapShader extends Shader {
        private int lightPositionUniformHandle = UNDEFINED_HANDLE;

        protected NormalMapShader() {
            super("NormalMapShader", R.raw.test_vert, R.raw.texture_frag); // todo: shader

            // Attributes
            positionAttributeHandle = getAttribute("aPosition");
            normalAttributeHandle = getAttribute("aNormal");
            tangentAttributeHandle = getAttribute("aTangent");
            textureAttributeHandle = getAttribute("aTextureCoordinates");

            // Vertex Uniforms
            projectionMatrixUniformHandle = getUniform("uProjection");
            viewMatrixUniformHandle = getUniform("uView");
            modelMatrixUniformHandle = getUniform("uModel");
            viewPositionUniformHandle = getUniform("uViewPosition");
            lightPositionUniformHandle = getUniform("uLightPos");

            // Fragment Uniforms
            materialHandles = new MaterialHandles();
            materialHandles.textureUniformHandle = getUniform("theTexture");
            materialHandles.diffuseMapUniformHandle = getUniform("diffuseMap");
            materialHandles.normalMapUniformHandle = getUniform("normalMap");
        }

        public void setLightPosition(float x, float y, float z) {
            enable();
            glUniform3f(lightPositionUniformHandle, x, y, z);
            disable();
        }
    }

    private NormalMapShader normalMapShader;
    private Model cube;
    private Model light;
    private Camera camera;
    private Vec2 touchDownPos;
    float angle = 45.0f;

    float lx;
    float ly;
    float lz;
    float lt;

    LightGroup lightGroup;
    PointLight pointLight;


    public NormalMapDemo() {
        normalMapShader = new NormalMapShader();
        normalMapShader.setLightPosition(1f, 0f, 0f);

        int[] tex = new int[1];
        glGenTextures(1, tex, 0);
        System.out.println("TEX:" + tex[0]);

//        Material material = new Material(new Texture(R.drawable.substance_graph_basecolor));
//        material.setNormalMap(new Texture(R.drawable.substance_graph_normal));
//        material.setDiffuseMap(new Texture(R.drawable.substance_graph_basecolor));
// todo: texture        Material material = new Material(new Texture(R.drawable.stacked_rock_cliff_albedo));
// todo: texture        material.setNormalMap(new Texture(R.drawable.stacked_rock_cliff_ao));
// todo: texture        material.setDiffuseMap(new Texture(R.drawable.stacked_rock_cliff_albedo));
//        Material material = new Material(new Texture(R.drawable.floor_base));
//        material.setNormalMap(new Texture(R.drawable.floor_normal));
//        material.setDiffuseMap(new Texture(R.drawable.floor_base));
//        Material material = new Material(new Texture(R.drawable.metal_plate));
//        material.setNormalMap(new Texture(R.drawable.metal_plate_normal));
//        material.setDiffuseMap(new Texture(R.drawable.metal_plate));


// todo: texture        cube = new Model(OBJModelLoader.readObjFile(R.raw.highpoly_sphere), material);

//        cube = new Model(new CubeMesh(true, true), material);
        cube.setPosition(0f, 0f, -8f);
        cube.setRotation(45f, 1f, 1f, 1f);
        cube.useCustomShader(normalMapShader);

        light = new Model(new CubeMesh(false, false));
        light.setColour(Colour.YELLOW);
        light.setScale(0.02f);

        lightGroup = new LightGroup();
        pointLight = new PointLight();
        lightGroup.addLight(pointLight);

        camera = new Camera();
    }


    @Override
    public void update(float deltaTime) {
        cube.setRotation(angle, 1f, 1f, 1f);
        angle += 0.2f * deltaTime;

//        lt += 0.01f * deltaTime;
        lx = (float)Math.sin(lt) * 2f;
        lz = ((float)Math.cos(lt) * 2f) - 8f;

      //  lx = 1.0f;
      //  ly = 1.0f;
      //  lz = 1.0f;
        normalMapShader.setLightPosition(lx, ly, lz);
        light.setPosition(lx, ly, lz);
        pointLight.setPosition(lx, ly, lz);
    }

    @Override
    public void render() {
        cube.render(camera);
        light.render(camera);
    }

    @Override
    public void touch(int eventType, Pointer pointer) {
        Vec2 position = new Vec2(pointer.getPosition());

        switch (eventType) {
            case MotionEvent.ACTION_DOWN:
                touchDownPos = position;
                break;
            case MotionEvent.ACTION_MOVE:
                if(touchDownPos != null) {
                    camera.translate(0.0f, 0.0f, Geometry.getVectorBetween(touchDownPos, position).y / 50.0f);
                    touchDownPos = position;
                }
                break;
            case MotionEvent.ACTION_UP:
                touchDownPos = null;
                break;
        }
    }
}
