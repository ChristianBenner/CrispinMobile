package com.crispin.crispinmobile.Rendering.Models;

import com.crispin.crispinmobile.Geometry.Rotation2D;
import com.crispin.crispinmobile.Geometry.Rotation3D;
import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Scale3D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Data.Material;
import com.crispin.crispinmobile.Rendering.Utilities.ModelMatrix;

public class ModelProperties {
    // Material to apply to the object
    public Material material;

    // The model matrix
    private final ModelMatrix modelMatrix;

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

    // Model matrix requires update
    private boolean modelMatrixUpdate;

    public ModelProperties(Material material) {
        this.material = material;
        modelMatrix = new ModelMatrix();
        position = new Vec3();
        rotation = new Rotation3D();
        scale = new Scale3D();
        rotationPoint = new Vec3();
        rotationPointAngle = new Rotation3D();
        modelMatrixUpdate = true;
    }

    public ModelProperties() {
        this.material = new Material();
        modelMatrix = new ModelMatrix();
        position = new Vec3();
        rotation = new Rotation3D();
        scale = new Scale3D();
        rotationPoint = new Vec3();
        rotationPointAngle = new Rotation3D();
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
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
        setPosition(position.x, position.y, position.z);
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
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
    }

    /**
     * Set x-axis scale multiplier
     *
     * @param x Multiplier for the x-axis
     * @since 1.0
     */
    public void setScaleX(float x) {
        this.scale.w = x;
        modelMatrixUpdate = true;
    }

    /**
     * Set y-axis scale multiplier
     *
     * @param y Multiplier for the y-axis
     * @since 1.0
     */
    public void setScaleY(float y) {
        this.scale.h = y;
        modelMatrixUpdate = true;
    }

    /**
     * Set z-axis scale multiplier
     *
     * @param z Multiplier for the z-axis
     * @since 1.0
     */
    public void setScaleZ(float z) {
        this.scale.l = z;
        modelMatrixUpdate = true;
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
     * Set the scale
     *
     * @param scale The scale to apply to the object in all dimensions (xyz)
     * @since 1.0
     */
    public void setScale(float scale) {
        setScale(scale, scale, scale);
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale3D scale) {
        setScale(scale.w, scale.h, scale.l);
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale2D scale) {
        setScale(scale.w, scale.h);
    }

    /**
     * Set the rotation
     *
     * @param rotation The new rotation
     * @since 1.0
     */
    public void setRotation(Rotation3D rotation) {
        setRotation(rotation.angle, rotation.x, rotation.y, rotation.z);
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
        modelMatrixUpdate = true;
    }

    /**
     * Set the rotation
     *
     * @param rotation The new rotation
     * @since 1.0
     */
    public void setRotation(Rotation2D rotation) {
        setRotation(rotation.angle, rotation.x, rotation.y);
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
        modelMatrixUpdate = true;
    }

    /**
     * Get the rotation
     *
     * @return Rotation
     * @since 1.0
     */
    public Rotation3D getRotation() {
        return rotation;
    }

    /**
     * Set the rotation around point
     *
     * @param point    The point to rotate around
     * @param rotation The rotation to make
     * @since 1.0
     */
    public void setRotationAroundPoint(Vec3 point, Rotation3D rotation) {
        setRotationAroundPoint(point.x, point.y, point.z, rotation.angle, rotation.x, rotation.y,
                rotation.z);
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
        setRotationAroundPoint(point.x, point.y, point.z, angle, rotationX, rotationY, rotationZ);
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
        setRotationAroundPoint(x, y, z, rotation.angle, rotation.x, rotation.y, rotation.z);
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
        modelMatrixUpdate = true;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Vec3 point) {
        this.position.translate(point);
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
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
        modelMatrixUpdate = true;
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
     * Get the model matrix. If a model property has changed since, it will be updated first
     *
     * @since 1.0
     */
    public ModelMatrix getModelMatrix() {
        if(modelMatrixUpdate) {
            updateModelMatrix();
        }
        return modelMatrix;
    }

    /**
     * Update the model matrix. This can update the model matrix if a transformation property of the
     * object such as a position, scale or rotation has changed.
     *
     * @since 1.0
     */
    private void updateModelMatrix() {
        modelMatrix.reset();
        modelMatrix.translate(position);

        if (rotation.angle != 0.0f) {
            modelMatrix.rotate(rotation);
        }

        if (rotationPointAngle.angle != 0.0f) {
            modelMatrix.rotateAroundPoint(rotationPoint, rotationPointAngle);
        }

        modelMatrix.scale(scale);
        modelMatrixUpdate = false;
    }
}