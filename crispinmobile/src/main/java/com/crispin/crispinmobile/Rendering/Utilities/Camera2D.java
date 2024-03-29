package com.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Crispin;
import com.crispin.crispinmobile.Geometry.Vec2;

/**
 * Camera2D provides a simple interface to view matrix operations and control. The class integrates
 * with other objects in the engine such as RenderObjects, providing a unified way to render with
 * 2-dimensional views. The public methods give the user full control over things such as the
 * position, field of view and direction of the virtual camera.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Matrix
 * @see Mesh
 * @since 1.0
 */
public class Camera2D {
    // Tag used in logging output
    private static final String TAG = "Camera2D";

    // This is the default near range of the orthographic matrix
    private static final float DEFAULT_NEAR = -5.0f;

    // This is the default far range of the orthographic matrix
    private static final float DEFAULT_FAR = 5.0f;

    // This is the default left range of the orthographic matrix
    private static final float DEFAULT_LEFT = 0.0f;

    // This is the default bottom range of the orthographic matrix
    private static final float DEFAULT_BOTTOM = 0.0f;

    // Left position of the orthographic matrix
    private float left;

    // Right position of the orthographic matrix
    private float right;

    // Bottom position of the orthographic matrix
    private float bottom;

    // Top position of the orthographic matrix
    private float top;

    // Near position of the orthographic matrix
    private float near;

    // Far position of the orthographic matrix
    private float far;

    // The orthographic matrix
    private float[] orthoMatrix;

    // Position of the view
    private Vec2 position;

    // Update required (if a change has been made should we update the ortho matrix)
    private boolean update;

    // Zoom amount
    private float zoom;

    /**
     * Construct the camera object with the custom values. Then produce the orthographic matrix
     * matrix. The parameters are all in real world co-ordinates. If you use the surface width and
     * height as dimensions, you can place objects using pixels within that surface dimension.
     *
     * @param x         The x position of the camera
     * @param y         The y position of the camera
     * @param width     The width of the camera view
     * @param height    The height of the camera view
     * @param nearBound The near bound of the orthographic matrix. Anything that is not within the
     *                  near and far bounds (on the Z plane) will be clipped
     * @param farBound  The far bound of the orthographic matrix. Anything that is not within the
     *                  near and far bounds (on the Z plane) will be clipped
     * @since 1.0
     */
    public Camera2D(float x, float y, float width, float height, float nearBound, float farBound) {
        this.left = x;
        this.bottom = y;
        this.right = width;
        this.top = height;
        this.near = nearBound;
        this.far = farBound;
        this.orthoMatrix = new float[16];
        this.position = new Vec2();
        this.update = true;
        this.zoom = 1.0f;
    }

    /**
     * Construct the camera object with the custom values. Then produce the orthographic matrix
     * matrix. The parameters are all in real world co-ordinates. If you use the surface width and
     * height as dimensions, you can place objects using pixels within that surface dimension. This
     * constructor uses default near and far values, if you find 3D objects in your 2D view being
     * clipped on the Z plane, specify different near and far values.
     *
     * @param x      The x position of the camera
     * @param y      The y position of the camera
     * @param width  The width of the camera view
     * @param height The height of the camera view
     * @since 1.0
     */
    public Camera2D(float x, float y, float width, float height) {
        this(x, y, width, height, DEFAULT_NEAR, DEFAULT_FAR);
    }

    /**
     * Construct the camera object with the custom values. Then produce the orthographic matrix
     * matrix. The parameters are all in real world co-ordinates. If you use the surface width and
     * height as dimensions, you can place objects using pixels within that surface dimension. This
     * constructor uses default values for the creation of an orthographic matrix (2D view). The
     * camera has a width equal to the surface width, a height equal to the surface height, and is
     * located at x: 0.0, y: 0.0 in world co-ordinates. This constructor also uses default near and
     * far values, if you find 3D objects in your 2D view being clipped on the Z plane, specify
     * different near and far values.
     *
     * @since 1.0
     */
    public Camera2D() {
        this(DEFAULT_LEFT, DEFAULT_BOTTOM, Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight());
    }

    public void translate(float x, float y) {
        position.x += x;
        position.y += y;
        update = true;
    }

    public void translate(Vec2 position) {
        this.position.x += position.x;
        this.position.y += position.y;
        update = true;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        update = true;
    }

    public void setPosition(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        update = true;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        update = true;
    }

    public float getZoom() {
        return zoom;
    }

    /**
     * Get the left bound of the cameras orthographic matrix
     *
     * @return Return the left bound of the orthographic matrix
     * @since 1.0
     */
    public float getLeft() {
        return this.left;
    }

    /**
     * Set the left bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param left The new left bound of the orthographic matrix
     * @since 1.0
     */
    public void setLeft(float left) {
        this.left = left;
        update = true;
    }

    /**
     * Get the right bound of the cameras orthographic matrix
     *
     * @return Return the right bound of the orthographic matrix
     * @since 1.0
     */
    public float getRight() {
        return right;
    }

    /**
     * Set the right bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param right The new right bound of the orthographic matrix
     * @since 1.0
     */
    public void setRight(float right) {
        this.right = right;
        update = true;
    }

    /**
     * Get the bottom bound of the cameras orthographic matrix
     *
     * @return Return the bottom bound of the orthographic matrix
     * @since 1.0
     */
    public float getBottom() {
        return bottom;
    }

    /**
     * Set the bottom bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param bottom The new bottom bound of the orthographic matrix
     * @since 1.0
     */
    public void setBottom(float bottom) {
        this.bottom = bottom;
        update = true;
    }

    /**
     * Get the top bound of the cameras orthographic matrix
     *
     * @return Return the top bound of the orthographic matrix
     * @since 1.0
     */
    public float getTop() {
        return top;
    }

    /**
     * Set the top bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param top The new top bound of the orthographic matrix
     * @since 1.0
     */
    public void setTop(float top) {
        this.top = top;
        update = true;
    }

    /**
     * Get the near bound of the cameras orthographic matrix
     *
     * @return Return the near bound of the orthographic matrix
     * @since 1.0
     */
    public float getNear() {
        return near;
    }

    /**
     * Set the near bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param near The new near bound of the orthographic matrix
     * @since 1.0
     */
    public void setNear(float near) {
        this.near = near;
        update = true;
    }

    /**
     * Get the far bound of the cameras orthographic matrix
     *
     * @return Return the far bound of the orthographic matrix
     * @since 1.0
     */
    public float getFar() {
        return far;
    }

    /**
     * Set the far bound of the cameras orthographic matrix. Causes a view matrix update.
     *
     * @param far The new far bound of the orthographic matrix
     * @since 1.0
     */
    public void setFar(float far) {
        this.far = far;
        update = true;
    }

    /**
     * Get the far bound of the cameras orthographic matrix
     *
     * @return Return the orthographic matrix
     * @since 1.0
     */
    public float[] getOrthoMatrix() {
        if(update) {
            Matrix.orthoM(orthoMatrix, 0, left, right, bottom, top, near, far);
            Matrix.translateM(orthoMatrix, 0, -position.x, -position.y, 0.0f);
            Matrix.scaleM(orthoMatrix, 0, zoom, zoom, 1.0f);
            update = false;
        }
        return orthoMatrix;
    }
}
