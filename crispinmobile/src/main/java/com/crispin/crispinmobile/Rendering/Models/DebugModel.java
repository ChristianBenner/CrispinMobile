package com.crispin.crispinmobile.Rendering.Models;

import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_POINTS;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniformMatrix4fv;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.crispin.crispinmobile.Geometry.Rotation2D;
import com.crispin.crispinmobile.Geometry.Rotation3D;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.LightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Shaders.TextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.DebugMesh;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Utilities.Logger;
import com.crispin.crispinmobile.Utilities.ShaderCache;

import java.util.ArrayList;

public class DebugModel extends Model {
    private DebugMesh debugMesh;
    private Shader debugMeshShader;

    public DebugModel(DebugMesh debugMesh, Material material) {
        super(debugMesh, material);
        this.debugMesh = debugMesh;
        debugMeshShader = new UniformColourShader();
        debugMeshShader.setMaterialUniforms(new Material(Colour.RED));
    }

    public DebugModel(DebugMesh debugMesh) {
        this(debugMesh, new Material());
    }

    public DebugModel(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                      Mesh.RenderMethod renderMethod, int elementsPerPosition, int elementsPerTexel,
                      int elementsPerNormal, Material material) {
        this(new DebugMesh(positionBuffer, texelBuffer, normalBuffer, renderMethod,
                elementsPerPosition, elementsPerTexel, elementsPerNormal), material);
    }

    public DebugModel(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                      Mesh.RenderMethod renderMethod, int elementsPerPosition, int elementsPerTexel,
                      int elementsPerNormal) {
        this(positionBuffer, texelBuffer, normalBuffer, renderMethod, elementsPerPosition,
                elementsPerTexel, elementsPerNormal, new Material());
    }

    @Override
    public void render(Camera camera, final LightGroup lightGroup) {
        super.render(camera, lightGroup);

        // Now do the debug mesh specific rendering (i.e. render normals)
        debugMeshShader.enable();

        // Support for shaders that only take in matrix (like uniform colour shader)
        if (shader.validHandle(shader.getMatrixUniformHandle())) {
            float[] modelViewMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
            Matrix.multiplyMM(modelViewMatrix, 0, camera.getViewMatrix(), 0,
                    modelMatrix.getFloats(), 0);

            float[] modelViewProjectionMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
            Matrix.multiplyMM(modelViewProjectionMatrix, 0, camera.getPerspectiveMatrix(), 0,
                    modelViewMatrix, 0);

            glUniformMatrix4fv(shader.getMatrixUniformHandle(), UNIFORM_UPLOAD_COUNT_SINGLE, false,
                    modelViewProjectionMatrix, 0);
        }

        if (shader.validHandle(shader.getViewPositionUniformHandle())) {
            final Vec3 cameraPos = camera.getPosition();
            glUniform3f(shader.getViewPositionUniformHandle(), cameraPos.x, cameraPos.y,
                    cameraPos.z);
        }

        if (shader.validHandle(shader.getProjectionMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getProjectionMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    camera.getPerspectiveMatrix(),
                    0);
        }

        if (shader.validHandle(shader.getViewMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getViewMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    camera.getViewMatrix(),
                    0);
        }

        if (shader.validHandle(shader.getModelMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getModelMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    modelMatrix.getFloats(),
                    0);
        }

        GLES30.glBindVertexArray(debugMesh.normalLinesVao);
        glDrawArrays(GL_LINES, 0, debugMesh.normalLinesVertexCount);
        GLES30.glBindVertexArray(0);

        debugMeshShader.disable();
    }

    public void render(Camera camera) {
        render(camera, null);
    }
}
