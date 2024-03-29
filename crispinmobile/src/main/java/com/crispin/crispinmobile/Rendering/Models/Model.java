package com.crispin.crispinmobile.Rendering.Models;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glLineWidth;
import static android.opengl.GLES30.glUniform2f;
import static android.opengl.GLES30.glUniform4f;
import static android.opengl.GLES30.GL_LINES;
import static android.opengl.GLES30.GL_POINTS;
import static android.opengl.GLES30.GL_TEXTURE_2D;
import static android.opengl.GLES30.GL_TRIANGLES;
import static android.opengl.GLES30.glBindTexture;
import static android.opengl.GLES30.glDrawArrays;
import static android.opengl.GLES30.glUniform1i;
import static android.opengl.GLES30.glUniform3f;
import static android.opengl.GLES30.glUniformMatrix4fv;

import static com.crispin.crispinmobile.Rendering.Shaders.Shader.UNDEFINED_HANDLE;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Rotation2D;
import com.crispin.crispinmobile.Geometry.Rotation3D;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Physics.BoundBox2D;
import com.crispin.crispinmobile.Physics.Hitbox;
import com.crispin.crispinmobile.Physics.HitboxCircle;
import com.crispin.crispinmobile.Physics.HitboxPolygon;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Texture;
import com.crispin.crispinmobile.Rendering.Entities.DirectionalLight;
import com.crispin.crispinmobile.Rendering.Entities.EmissiveEdge;
import com.crispin.crispinmobile.Rendering.Entities.PointLight;
import com.crispin.crispinmobile.Rendering.Entities.SpotLight;
import com.crispin.crispinmobile.Rendering.Shaders.LightingShader;
import com.crispin.crispinmobile.Rendering.Shaders.LightingTextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.Shader;
import com.crispin.crispinmobile.Rendering.Shaders.TextureShader;
import com.crispin.crispinmobile.Rendering.Shaders.UniformColourShader;
import com.crispin.crispinmobile.Rendering.Utilities.Camera;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.LightGroup;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;
import com.crispin.crispinmobile.Utilities.Logger;
import com.crispin.crispinmobile.Utilities.ShaderCache;

import java.util.ArrayList;

public class Model {
    // Tag used in logging output
    private static final String TAG = "Model";

    // The number of elements in a 4x4 view matrix
    protected static final int NUM_VALUES_PER_VIEW_MATRIX = 16;

    // Number of uniform elements to upload in a single GLSL uniform upload
    protected static final int UNIFORM_UPLOAD_COUNT_SINGLE = 1;

    // The default width of lines when wireframes are being rendered
    private static final float DEFAULT_WIREFRAME_LINE_WIDTH = 5f;

    // The mesh
    public Mesh mesh;

    // The model matrix
    protected final ModelMatrix modelMatrix;

    // Position of the object
    private final Vec3 position;

    // Rotation of the object
    private final Rotation3D rotation;

    // Scale of the object
    private final Scale3D scale;

    // Rotation around a point
    private final Vec3 rotationPoint;

    // Point rotation
    private final Rotation3D rotationPointAngle;

    // Should we update model matrix i.e. position, scale or rotation has changed since last update
    private boolean updateModelMatrix;

    // Material to apply to the object
    protected Material material;

    // Shader applied to the object
    protected Shader shader;

    // If the model has a custom shader
    private boolean hasCustomShader;

    // Should we render the wireframe (useful for debugging)
    private boolean renderWireframe;

    // Width of the wireframe lines
    private float wireframeLineWidth;

    // Hitbox
    private Hitbox hitbox;

    public Model(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.modelMatrix = new ModelMatrix();
        this.position = new Vec3();
        this.rotation = new Rotation3D();
        this.scale = new Scale3D();
        this.rotationPoint = new Vec3();
        this.rotationPointAngle = new Rotation3D();
        this.material = material;
        this.hasCustomShader = false;
        this.renderWireframe = false;
        this.wireframeLineWidth = DEFAULT_WIREFRAME_LINE_WIDTH;
        this.updateModelMatrix = true;
    }

    public Model(Mesh mesh) {
        this(mesh, new Material());
    }

    public Model(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                 Mesh.RenderMethod renderMethod, int elementsPerPosition, int elementsPerTexel,
                 int elementsPerNormal, Material material) {
        this(new Mesh(positionBuffer, texelBuffer, normalBuffer, renderMethod,
                elementsPerPosition, elementsPerTexel, elementsPerNormal), material);
    }

    public Model(float[] positionBuffer, float[] texelBuffer, float[] normalBuffer,
                 Mesh.RenderMethod renderMethod, int elementsPerPosition, int elementsPerTexel,
                 int elementsPerNormal) {
        this(positionBuffer, texelBuffer, normalBuffer, renderMethod, elementsPerPosition,
                elementsPerTexel, elementsPerNormal, new Material());
    }

    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isColliding(Model model) {
        Hitbox other = model.getHitbox();
        if (other instanceof HitboxPolygon) {
            return hitbox.isColliding((HitboxPolygon)other);
        } else if (other instanceof HitboxCircle) {
            return hitbox.isColliding((HitboxCircle) other);
        }

        return false;
    }

    public Vec2 isCollidingMTV(Model model) {
        Hitbox other = model.getHitbox();
        if (other instanceof HitboxPolygon) {
            return hitbox.isCollidingMTV((HitboxPolygon)other);
        } else if (other instanceof HitboxCircle) {
            return hitbox.isCollidingMTV((HitboxCircle) other);
        }

        return null;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
        if(shader != null) {
            mesh.setAttributePointers(shader.positionAttributeHandle, shader.textureAttributeHandle,
                    shader.normalAttributeHandle, shader.tangentAttributeHandle,
                    shader.bitangentAttributeHandle);
        }
    }

    public ModelMatrix getModelMatrix() {
        updateModelMatrix();
        return modelMatrix;
    }

    /**
     * If enabled, a wireframe outlining the mesh will be rendered next render call. This is useful
     * for debugging purposes to see how a mesh is being rendered. You can also set the width of the
     * wireframe lines.
     *
     * @param state True to render wireframes, else false
     * @see @setWireframeLineWidth
     * @since 1.0
     */
    public void setRenderWireframe(boolean state) {
        this.renderWireframe = state;
    }

    /**
     * Width of the wireframe lines. Wireframe needs to be enabled seperately
     *
     * @param width Width of the wireframe lines
     * @see @setRenderWireframe
     * @since 1.0
     */
    public void setWireframeLineWidth(int width) {
        this.wireframeLineWidth = width;
    }

    /**
     * Set the x co-ordinate
     *
     * @param x The new x-coordinate
     * @since 1.0
     */
    public void setX(float x) {
        this.position.x = x;
        this.updateModelMatrix = true;
    }

    /**
     * Set the y co-ordinate
     *
     * @param y The new y-coordinate
     * @since 1.0
     */
    public void setY(float y) {
        this.position.y = y;
        this.updateModelMatrix = true;
    }

    /**
     * Set the z co-ordinate
     *
     * @param z The new z-coordinate
     * @since 1.0
     */
    public void setZ(float z) {
        this.position.z = z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the position
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @param z The new z-coordinate
     * @since 1.0
     */
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the position
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @since 1.0
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.updateModelMatrix = true;
    }

    /**
     * Get the position
     *
     * @return The position of the object
     * @since 1.0
     */
    public Vec3 getPosition() {
        return position;
    }

    /**
     * Set the position
     *
     * @param position The new position
     * @since 1.0
     */
    public void setPosition(Vec3 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the position
     *
     * @param position The new position
     * @since 1.0
     */
    public void setPosition(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.updateModelMatrix = true;
    }

    /**
     * Set the scale
     *
     * @param w Multiplier for the x-axis
     * @param h Multiplier for the y-axis
     * @param l Multiplier for the z-axis
     * @since 1.0
     */
    public void setScale(float w, float h, float l) {
        this.scale.w = w;
        this.scale.h = h;
        this.scale.l = l;
        this.updateModelMatrix = true;
    }

    /**
     * Set the scale
     *
     * @param w Multiplier for the x-axis
     * @param h Multiplier for the y-axis
     * @since 1.0
     */
    public void setScale(float w, float h) {
        this.scale.w = w;
        this.scale.h = h;
        this.updateModelMatrix = true;
    }

    /**
     * Set x-axis scale multiplier
     *
     * @param x Multiplier for the x-axis
     * @since 1.0
     */
    public void setScaleX(float x) {
        this.scale.w = x;
        this.updateModelMatrix = true;
    }

    /**
     * Set y-axis scale multiplier
     *
     * @param y Multiplier for the y-axis
     * @since 1.0
     */
    public void setScaleY(float y) {
        this.scale.h = y;
        this.updateModelMatrix = true;
    }

    /**
     * Set z-axis scale multiplier
     *
     * @param z Multiplier for the z-axis
     * @since 1.0
     */
    public void setScaleZ(float z) {
        this.scale.l = z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object in all dimensions (xyz)
     * @since 1.0
     */
    public void setScale(float scale) {
        this.scale.w = scale;
        this.scale.h = scale;
        this.scale.l = scale;
        this.updateModelMatrix = true;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale3D scale) {
        this.scale.w = scale.w;
        this.scale.h = scale.h;
        this.scale.l = scale.l;
        this.updateModelMatrix = true;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale2D scale) {
        this.scale.w = scale.w;
        this.scale.h = scale.h;
        this.updateModelMatrix = true;
    }

    /**
     * Get the scale
     *
     * @return The scale multiplier of the object
     * @since 1.0
     */
    public Scale3D getScale() {
        return this.scale;
    }

    /**
     * Set the rotation in z axis (for 2D sprites)
     *
     * @param angle The new rotation angle
     * @since 1.0
     */
    public void setRotation(float angle) {
        this.rotation.angle = angle;
        this.rotation.x = 0f;
        this.rotation.y = 0f;
        this.rotation.z = 1f;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation
     *
     * @param angle The new rotation angle
     * @param x     The x axis multiplier
     * @param y     The y axis multiplier
     * @param z     The z axis multiplier
     * @since 1.0
     */
    public void setRotation(float angle, float x, float y, float z) {
        this.rotation.angle = angle;
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation
     *
     * @param rotation The new rotation
     * @since 1.0
     */
    public void setRotation(Rotation3D rotation) {
        this.rotation.angle = rotation.angle;
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation
     *
     * @param rotation The new rotation
     * @since 1.0
     */
    public void setRotation(Rotation2D rotation) {
        this.rotation.angle = rotation.angle;
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation
     *
     * @param angle The new rotation angle
     * @param x     The x axis multiplier
     * @param y     The y axis multiplier
     * @since 1.0
     */
    public void setRotation(float angle, float x, float y) {
        this.rotation.angle = angle;
        this.rotation.x = x;
        this.rotation.y = y;
        this.updateModelMatrix = true;
    }

    /**
     * Get the rotation
     *
     * @return The 3D rotation of the object
     * @since 1.0
     */
    public Rotation3D getRotation() {
        return this.rotation;
    }

    /**
     * Set the rotation around point
     *
     * @param x         The x position to rotate around
     * @param y         The x position to rotate around
     * @param z         The x position to rotate around
     * @param angle     The angle to rotate
     * @param rotationX The x axis multiplier
     * @param rotationY The y axis multiplier
     * @param rotationZ The z axis multiplier
     * @since 1.0
     */
    public void setRotationAroundPoint(float x, float y, float z, float angle, float rotationX,
                                       float rotationY, float rotationZ) {
        this.rotationPoint.x = x;
        this.rotationPoint.y = y;
        this.rotationPoint.z = z;
        this.rotationPointAngle.angle = angle;
        this.rotationPointAngle.x = rotationX;
        this.rotationPointAngle.y = rotationY;
        this.rotationPointAngle.z = rotationZ;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation around point in 2D space
     *
     * @param x         The x position to rotate around
     * @param y         The x position to rotate around
     * @param angle     The angle to rotate
     * @since 1.0
     */
    public void setRotationAroundPoint(float x, float y, float angle) {
        this.rotationPoint.x = x;
        this.rotationPoint.y = y;
        this.rotationPoint.z = 0f;
        this.rotationPointAngle.angle = angle;
        this.rotationPointAngle.x = 0f;
        this.rotationPointAngle.y = 0f;
        this.rotationPointAngle.z = 1f;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation around point in 2D space
     *
     * @param point Vec2
     * @param angle The angle to rotate
     * @since 1.0
     */
    public void setRotationAroundPoint(Vec2 point, float angle) {
        this.rotationPoint.x = point.x;
        this.rotationPoint.y = point.y;
        this.rotationPoint.z = 0f;
        this.rotationPointAngle.angle = angle;
        this.rotationPointAngle.x = 0f;
        this.rotationPointAngle.y = 0f;
        this.rotationPointAngle.z = 1f;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation around point
     *
     * @param x        The x position to rotate around
     * @param y        The x position to rotate around
     * @param z        The x position to rotate around
     * @param rotation The rotation to make
     * @since 1.0
     */
    public void setRotationAroundPoint(float x, float y, float z, Rotation3D rotation) {
        this.rotationPoint.x = x;
        this.rotationPoint.y = y;
        this.rotationPoint.z = z;
        this.rotationPointAngle.angle = rotation.angle;
        this.rotationPointAngle.x = rotation.x;
        this.rotationPointAngle.y = rotation.y;
        this.rotationPointAngle.z = rotation.z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation around point
     *
     * @param point    The point to rotate around
     * @param rotation The rotation to make
     * @since 1.0
     */
    public void setRotationAroundPoint(Vec3 point, Rotation3D rotation) {
        this.rotationPoint.x = point.x;
        this.rotationPoint.y = point.y;
        this.rotationPoint.z = point.z;
        this.rotationPointAngle.angle = rotation.angle;
        this.rotationPointAngle.x = rotation.x;
        this.rotationPointAngle.y = rotation.y;
        this.rotationPointAngle.z = rotation.z;
        this.updateModelMatrix = true;
    }

    /**
     * Set the rotation around point
     *
     * @param point     The point to rotate around
     * @param angle     The angle to rotate
     * @param rotationX The x axis multiplier
     * @param rotationY The y axis multiplier
     * @param rotationZ The z axis multiplier
     * @since 1.0
     */
    public void setRotationAroundPoint(Vec3 point, float angle, float rotationX, float rotationY,
                                       float rotationZ) {
        this.rotationPoint.x = point.x;
        this.rotationPoint.y = point.y;
        this.rotationPoint.z = point.z;
        this.rotationPointAngle.angle = angle;
        this.rotationPointAngle.x = rotationX;
        this.rotationPointAngle.y = rotationY;
        this.rotationPointAngle.z = rotationZ;
        this.updateModelMatrix = true;
    }

    /**
     * Translate the render objects position
     *
     * @param x The x-coordinate to translate by
     * @param y The y-coordinate to translate by
     * @param z The z-coordinate to translate by
     * @since 1.0
     */
    public void translate(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
        this.updateModelMatrix = true;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Vec3 point) {
        this.position.x += point.x;
        this.position.y += point.y;
        this.position.z += point.z;
        this.updateModelMatrix = true;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Vec2 point) {
        this.position.x += point.x;
        this.position.y += point.y;
        this.updateModelMatrix = true;
    }

    /**
     * Translate the render objects position
     *
     * @param x The x-coordinate to translate by
     * @param y The y-coordinate to translate by
     * @since 1.0
     */
    public void translate(float x, float y) {
        this.position.x += x;
        this.position.y += y;
        this.updateModelMatrix = true;
    }

    /**
     * Get the material applied to the render object
     *
     * @return The material attached to the render object
     * @see Material
     * @since 1.0
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Set the material. Materials can contain multiple pieces of information such as texture and
     * colour. Your model must contain texel data in order to support material textures.
     *
     * @param material The material to apply to the render object
     * @see Material
     * @since 1.0
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Set the texture. The existing material will be updated to use the provided texture
     *
     * @param texture The texture to apply to the render object
     * @see Texture
     * @since 1.0
     */
    public void setTexture(Texture texture) {
        this.material.setTexture(texture);
    }

    /**
     * Set the colour of the object
     *
     * @param r The intensity of the red channel (0.0-1.0)
     * @param g The intensity of the green channel (0.0-1.0)
     * @param b The intensity of the blue channel (0.0-1.0)
     * @param a The intensity of the alpha channel (0.0-1.0)
     * @see Colour
     * @since 1.0
     */
    public void setColour(float r, float g, float b, float a) {
        this.material.setColour(new Colour(r, g, b, a));
    }

    /**
     * Set the colour of the object
     *
     * @param r The intensity of the red channel (0.0-1.0)
     * @param g The intensity of the green channel (0.0-1.0)
     * @param b The intensity of the blue channel (0.0-1.0)
     * @see Colour
     * @since 1.0
     */
    public void setColour(float r, float g, float b) {
        this.material.setColour(new Colour(r, g, b));
    }

    /**
     * Get the alpha channel intensity of the object
     *
     * @return The intensity of the alpha channel (0.0-1.0)
     * @see Colour
     * @since 1.0
     */
    public float getAlpha() {
        return this.material.colour.alpha;
    }

    /**
     * Set the alpha channel intensity of the object
     *
     * @param alpha The intensity of the alpha channel (0.0-1.0)
     * @see Colour
     * @since 1.0
     */
    public void setAlpha(float alpha) {
        this.material.colour.alpha = alpha;
    }

    /**
     * Get the colour of the object
     *
     * @return The colour of the object
     * @see Colour
     * @since 1.0
     */
    public Colour getColour() {
        return this.material.colour;
    }

    /**
     * Set the colour of the object
     *
     * @param colour The colour to set the object
     * @see Colour
     * @since 1.0
     */
    public void setColour(Colour colour) {
        this.material.setColour(colour);
    }

    /**
     * Update the model matrix. This can update the model matrix if a transformation property of the
     * object such as a position, scale or rotation has changed.
     *
     * @since 1.0
     */
    protected void updateModelMatrix() {
        // Only update the model matrix when there is a reason to do so i.e. position, scale or
        // rotation has changed since last time so this flag is set to true
        if(!updateModelMatrix) {
            return;
        }
        updateModelMatrix = false;

        modelMatrix.reset();
        modelMatrix.translate(position);
        if (rotationPointAngle.angle != 0.0f) {
            modelMatrix.rotateAroundPoint(rotationPoint, rotationPointAngle);
        }
        if (rotation.angle != 0.0f) {
            modelMatrix.rotate(rotation);
        }
        modelMatrix.scale(scale);
    }

    /**
     * Use a custom shader with the object. This means that the object will be rendered using your
     * own or a different built in GLSL program. You must make sure the shader you are setting
     * supports the data attributes and uniforms of the object or the object may not render
     * correctly or worse the program will crash.
     *
     * @since 1.0
     */
    public void setShader(Shader customShader) {
        // Check if the shader being assigned has been freeTypeInitialised
        if (customShader != null) {
            hasCustomShader = true;
            shader = customShader;
            mesh.setAttributePointers(shader.positionAttributeHandle, shader.textureAttributeHandle,
                    shader.normalAttributeHandle, shader.tangentAttributeHandle,
                    shader.bitangentAttributeHandle);
        } else {
            Logger.error(TAG, "Custom shader supplied is null");
        }
    }

    public Shader getShader() {
        return shader;
    }

    public void render(Camera2D camera, final LightGroup lightGroup) {
        updateModelMatrix();

        // If the shader is null, create a shader for the object
        if (shader == null) {
            updateShader();
        }

        shader.enable();
        setLightUniforms(lightGroup);

        if (shader.validHandle(shader.getMatrixUniformHandle())) {
            float[] modelViewMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
            Matrix.multiplyMM(modelViewMatrix, 0, camera.getOrthoMatrix(), 0,
                    modelMatrix.getFloats(), 0);

            glUniformMatrix4fv(shader.getMatrixUniformHandle(), UNIFORM_UPLOAD_COUNT_SINGLE, false,
                    modelViewMatrix, 0);
        }

        if (shader.validHandle(shader.getProjectionMatrixUniformHandle())) {
            float[] emptyMatrix = new float[NUM_VALUES_PER_VIEW_MATRIX];
            Matrix.setIdentityM(emptyMatrix, 0);
            glUniformMatrix4fv(shader.getProjectionMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    emptyMatrix,
                    0);
        }

        if (shader.validHandle(shader.getViewMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getViewMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    camera.getOrthoMatrix(),
                    0);
        }

        if (shader.validHandle(shader.getModelMatrixUniformHandle())) {
            glUniformMatrix4fv(shader.getModelMatrixUniformHandle(),
                    UNIFORM_UPLOAD_COUNT_SINGLE,
                    false,
                    modelMatrix.getFloats(),
                    0);
        }

        if (shader.validHandle(shader.getViewDimensionUniformHandle())) {
            glUniform2f(shader.getViewDimensionUniformHandle(), Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());
        }

        // Set all material uniforms
        shader.setMaterialUniforms(material);

        GLES30.glBindVertexArray(mesh.vao);
        // Draw the vertex data with the specified render method
        switch (mesh.renderMethod) {
            case POINTS:
                glDrawArrays(GL_POINTS, 0, mesh.vertexCount);
                break;
            case LINES:
                glDrawArrays(GL_LINES, 0, mesh.vertexCount);
                break;
            case TRIANGLES:
                glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount);
                break;
            case TRIANGLE_FAN:
                glDrawArrays(GL_TRIANGLE_FAN, 0, mesh.vertexCount);
                break;
            case TRIANGLE_STRIP:
                glDrawArrays(GL_TRIANGLE_STRIP, 0, mesh.vertexCount);
                break;
        }
        glBindTexture(GL_TEXTURE_2D, 0);

        if(renderWireframe) {
            if (shader.materialHandles.colourUniformHandle != UNDEFINED_HANDLE) {
                glUniform4f(shader.materialHandles.colourUniformHandle, 0f, 0f, 0f, material.colour.alpha);
            }
            glLineWidth(wireframeLineWidth);
            glDrawArrays(GL_LINES, 0, mesh.vertexCount);
            glDrawArrays(GL_LINES, 1, mesh.vertexCount);
        }

        GLES30.glBindVertexArray(0);

        shader.disable();
    }

    public void render(Camera2D camera) {
        render(camera, null);
    }

    public void render(Camera camera, final LightGroup lightGroup) {
        updateModelMatrix();

        // If the shader is null, create a shader for the object
        if (shader == null) {
            updateShader();
        }

        shader.enable();
        setLightUniforms(lightGroup);

        // Set all material uniforms
        shader.setMaterialUniforms(material);

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

        if (shader.validHandle(shader.getViewDimensionUniformHandle())) {
            glUniform2f(shader.getViewDimensionUniformHandle(), Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());
        }

        GLES30.glBindVertexArray(mesh.vao);
        // Draw the vertex data with the specified render method
        switch (mesh.renderMethod) {
            case POINTS:
                glDrawArrays(GL_POINTS, 0, mesh.vertexCount);
                break;
            case LINES:
                glDrawArrays(GL_LINES, 0, mesh.vertexCount);
                break;
            case TRIANGLES:
                glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount);
                break;
        }

        glBindTexture(GL_TEXTURE_2D, 0);

        if(renderWireframe) {
            if (shader.materialHandles.colourUniformHandle != UNDEFINED_HANDLE) {
                glUniform4f(shader.materialHandles.colourUniformHandle, 0f, 0f, 0f, material.colour.alpha);
            }
            glLineWidth(wireframeLineWidth);
            glDrawArrays(GL_LINES, 0, mesh.vertexCount);
            glDrawArrays(GL_LINES, 1, mesh.vertexCount);
        }
        GLES30.glBindVertexArray(0);

        shader.disable();
    }

    public void render(Camera camera) {
        render(camera, null);
    }

    private void setLightUniforms(LightGroup lightGroup) {
        if (lightGroup != null) {
            final DirectionalLight directionalLight = lightGroup.getDirectionalLight();
            if (directionalLight != null) {
                shader.setDirectionalLightUniforms(directionalLight);
            } else {
                shader.clearDirectionalLightUniforms();
            }

            // Point lights
            final ArrayList<PointLight> pointLights = lightGroup.getPointLights();
            if (shader.validHandle(shader.getNumPointLightsUniformHandle())) {
                glUniform1i(shader.getNumPointLightsUniformHandle(), pointLights.size());
            }
            // Iterate through point lights, uploading each to the shader
            for (int i = 0; i < pointLights.size() && i < shader.getMaxPointLights(); i++) {
                final PointLight pointLight = pointLights.get(i);
                shader.setPointLightUniforms(i, pointLight);
            }

            // Emissive edges
            final ArrayList<EmissiveEdge> emissiveEdges = lightGroup.getEmissiveEdges();
            if (shader.validHandle(shader.getNumEmissiveEdgesUniformHandle())) {
                glUniform1i(shader.getNumEmissiveEdgesUniformHandle(), emissiveEdges.size());
            }
            // Iterate through emissive edges, uploading each to the shader
            for (int i = 0; i < emissiveEdges.size() && i < shader.getMaxEmissiveEdges(); i++) {
                final EmissiveEdge emissiveEdge = emissiveEdges.get(i);
                shader.setEmissiveEdgeUniforms(i, emissiveEdge);
            }

            // Spot lights
            final ArrayList<SpotLight> spotLights = lightGroup.getSpotLights();
            if (shader.validHandle(shader.getNumSpotLightsUniformHandle())) {
                glUniform1i(shader.getNumSpotLightsUniformHandle(), spotLights.size());
            }
            // Iterate through spot lights, uploading each to the shader
            for (int i = 0; i < spotLights.size() && i < shader.getMaxSpotLights(); i++) {
                final SpotLight spotLight = spotLights.get(i);
                shader.setSpotLightUniforms(i, spotLight);
            }
        }
    }

    /**
     * Update the shader by automatically deciding what built in GLSL program to use depending on
     * the data that is present on the render object. For example, if the object has position data,
     * texel data and a texture applied, a texture shader will be assigned.
     *
     * @since 1.0
     */
    protected void updateShader() {
        if(shader != null) {
            return;
        }

        // If their has not been a custom shader allocated to the render object, automatically
        // allocate one
        if (hasCustomShader) {
            return;
        }

        // Check that the object has all of the components required to render normal data
        final boolean supportsNormals = !material.isIgnoringNormalData() &&
                mesh.elementsPerNormal != 0;

        // Check that the object has all of the components required to render a texture
        final boolean supportsTexture = material.hasTexture() &&
                (mesh.elementsPerTexel != 0) &&
                !material.isIgnoringTexelData();

        // Select a shader based on what data attributes and uniforms the object supports
        if (supportsNormals && supportsTexture) {
            if (ShaderCache.existsInCache(LightingTextureShader.VERTEX_FILE,
                    LightingTextureShader.FRAGMENT_FILE)) {
                shader = ShaderCache.getShader(LightingTextureShader.VERTEX_FILE,
                        LightingTextureShader.FRAGMENT_FILE);
            } else {
                shader = new LightingTextureShader();
            }
        } else if (supportsNormals) {
            if (ShaderCache.existsInCache(LightingShader.VERTEX_FILE, LightingShader.FRAGMENT_FILE)) {
                shader = ShaderCache.getShader(LightingShader.VERTEX_FILE,
                        LightingShader.FRAGMENT_FILE);
            } else {
                shader = new LightingShader();
            }
        } else if (supportsTexture) {
            // Just a texture shader
            if (ShaderCache.existsInCache(TextureShader.VERTEX_FILE,
                    TextureShader.FRAGMENT_FILE)) {
                shader = ShaderCache.getShader(TextureShader.VERTEX_FILE,
                        TextureShader.FRAGMENT_FILE);
            } else {
                shader = new TextureShader();
            }
        } else {
            // Just use a colour shader
            if (ShaderCache.existsInCache(UniformColourShader.VERTEX_FILE,
                    UniformColourShader.FRAGMENT_FILE)) {
                shader = ShaderCache.getShader(UniformColourShader.VERTEX_FILE,
                        UniformColourShader.FRAGMENT_FILE);
            } else {
                shader = new UniformColourShader();
            }
        }

        mesh.setAttributePointers(shader.positionAttributeHandle, shader.textureAttributeHandle,
                shader.normalAttributeHandle, shader.tangentAttributeHandle,
                shader.bitangentAttributeHandle);
    }
}
