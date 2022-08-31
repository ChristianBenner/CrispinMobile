package com.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Vec3;

/**
 * Camera provides a simple interface to view matrix operations and control. The class integrates
 * with other objects in the engine such as RenderObjects, providing a unified way to render with
 * 3-dimensional views. The public methods give the user full control over things such as the
 * position, rotation, field of view and direction of the virtual camera.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Matrix
 * @see RenderObject
 * @since 1.0
 */
public class Camera {
    // Default pitch (x axis rotation)
    private static final float DEFAULT_PITCH = 0.0f;

    // Default yaw (y axis rotation)
    private static final float DEFAULT_YAW = -90.0f;

    // Default vertical field of view
    private static final float DEFAULT_FOV_V = 59.0f;

    // Default z-near
    private static final float DEFAULT_Z_NEAR = 0.1f;

    // Default z-far
    private static final float DEFAULT_Z_FAR = 100.0f;

    // The number of floats in a 4x4 Matrix
    private static final int NUM_FLOATS_4X4_MATRIX = 16;

    // View matrix
    private float[] viewMatrix;

    // Perspective matrix
    private float[] perspectiveMatrix;

    // Position
    private Vec3 position;

    // Up direction of the camera
    private Vec3 up;

    // Up direction of the world
    private Vec3 worldUp;

    // Direction the camera is pointing
    private Vec3 direction;

    // Right direction from the camera
    private Vec3 right;

    // Pitch angle in degrees (rotation around the x-axis)
    private float pitch;

    // Yaw angle in degrees (rotation around the y-axis)
    private float yaw;

    // Vertical zoom/field of view
    private float verticalFov;

    // Minimum distance before clipping
    private float zNear;

    // Maximum distance before clipping
    private float zFar;

    /**
     * Construct the camera object with the default values.
     *
     * @since 1.0
     */
    public Camera() {
        viewMatrix = new float[NUM_FLOATS_4X4_MATRIX];
        perspectiveMatrix = new float[NUM_FLOATS_4X4_MATRIX];

        position = new Vec3(0.0f, 0.0f, 0.0f);
        up = new Vec3(0.0f, 1.0f, 0.0f);
        worldUp = new Vec3(up);
        direction = new Vec3(0.0f, 0.f, -1.0f);

        pitch = DEFAULT_PITCH;
        yaw = DEFAULT_YAW;
        verticalFov = DEFAULT_FOV_V;
        zNear = DEFAULT_Z_NEAR;
        zFar = DEFAULT_Z_FAR;

        updateVectors();
        updatePerspective();
    }

    /**
     * Set the camera position
     *
     * @param x New camera x position
     * @param y New camera y position
     * @param z New camera z position
     * @since 1.0
     */
    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Set the camera position
     *
     * @param position  A vec3 containing the new camera position
     * @since 1.0
     */
    public void setPosition(Vec3 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    /**
     * Translate the camera
     *
     * @param x Camera x translation
     * @param y Camera y translation
     * @param z Camera z translation
     * @since 1.0
     */
    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    /**
     * Translate the camera
     *
     * @param translation   A vec3 containing the vector translation
     * @since 1.0
     */
    public void translate(Vec3 translation) {
        this.position.x += translation.x;
        this.position.y += translation.y;
        this.position.z += translation.z;
    }

    /**
     * Retrieve the camera position
     *
     * @return The camera position
     * @since 1.0
     */
    public Vec3 getPosition() {
        return position;
    }

    /**
     * Set the camera pitch in degrees (rotation around the x-axis)
     *
     * @param degrees   New pitch value in degrees
     * @since 1.0
     */
    public void setPitch(float degrees) {
        this.pitch = degrees;
        updateVectors();
    }

    /**
     * Set the camera yaw in degrees (rotation around the y-axis)
     *
     * @param degrees   New yaw value in degrees
     * @since 1.0
     */
    public void setYaw(float degrees) {
        this.yaw = degrees;
        updateVectors();
    }

    /**
     * Set the camera rotation
     *
     * @param pitchDegrees  Pitch in degrees (rotation around x-axis)
     * @param yawDegrees    Yaw in degrees (rotation around y-axis)
     * @since 1.0
     */
    public void setRotation(float pitchDegrees, float yawDegrees) {
        this.pitch = pitchDegrees;
        this.yaw = yawDegrees;
        updateVectors();
    }

    /**
     * Rotate the camera by both pitch and yaw
     *
     * @param pitchDegrees  Pitch in degrees (rotation around x-axis)
     * @param yawDegrees    Yaw in degrees (rotation around y-axis)
     * @since 1.0
     */
    public void rotate(float pitchDegrees, float yawDegrees) {
        this.pitch += pitchDegrees;
        this.yaw += yawDegrees;
        updateVectors();
    }

    /**
     * Get the z-near value of the perspective
     *
     * @return The z-near value of the perspective
     * @since 1.0
     */
    public float getNear() {
        return zNear;
    }

    /**
     * Set the z-near value of the perspective
     *
     * @param zNear The new z-near value of the perspective
     * @since 1.0
     */
    public void setNear(float zNear) {
        this.zNear = zNear;
        updatePerspective();
    }

    /**
     * Get the z-far value of the perspective
     *
     * @return The z-far value of the perspective
     * @since 1.0
     */
    public float getFar() {
        return zFar;
    }

    /**
     * Set the z-far value of the perspective
     *
     * @param zFar  The new z-far value of the perspective
     * @since 1.0
     */
    public void setFar(float zFar) {
        this.zFar = zFar;
        updatePerspective();
    }

    /**
     * Get the perspective field of view
     *
     * @return The perspective field of view
     * @since 1.0
     */
    public float getFieldOfView() {
        return verticalFov;
    }

    /**
     * Set the perspective field of view
     *
     * @param fieldOfView The new field of view for the perspective
     * @since 1.0
     */
    public void setFieldOfView(float fieldOfView) {
        this.verticalFov = fieldOfView;
        updatePerspective();
    }

    /**
     * Get the perspective matrix.
     *
     * @return A Mat4x4 perspective (frustum) matrix
     * @see Matrix
     * @since 1.0
     */
    public float[] getPerspectiveMatrix() {
        return perspectiveMatrix;
    }

    /**
     * Get the view matrix. Before the matrix is fetched it is updated to contain the latest
     * position and direction changes.
     *
     * @return A Mat4x4 view matrix
     * @see Matrix
     * @since 1.0
     */
    public float[] getViewMatrix() {
        Matrix.setLookAtM(viewMatrix, 0, position.x, position.y, position.z,
                position.x + direction.x, position.y + direction.y,
                position.z + direction.z, up.x, up.y, up.z);
        return viewMatrix;
    }

    /**
     * Update the view matrix vectors
     *
     * @since 1.0
     */
    public void updateVectors() {
        final double yawRad = Math.toRadians(yaw);
        final double pitchRad = Math.toRadians(pitch);

        Vec3 tempDirection = new Vec3();
        tempDirection.x = (float)(Math.cos(yawRad) * Math.cos(pitchRad));
        tempDirection.y = (float)Math.sin(pitchRad);
        tempDirection.z = (float)(Math.sin(yawRad) * Math.cos(pitchRad));
        direction = Geometry.normalize(tempDirection);
        right = Geometry.normalize(Geometry.crossProduct(direction, worldUp));
        up = Geometry.normalize(Geometry.crossProduct(right, direction));
    }

    /**
     * Update the perspective matrix. Uses the Matrix perspectiveM function to produce a new
     * perspective/frustrum matrix.
     *
     * @see Matrix
     * @since 1.0
     */
    public void updatePerspective() {
        final float aspectRatio = (float)Crispin.getSurfaceWidth() / Crispin.getSurfaceHeight();
        Matrix.perspectiveM(perspectiveMatrix, 0, verticalFov, aspectRatio, zNear, zFar);
    }
}
