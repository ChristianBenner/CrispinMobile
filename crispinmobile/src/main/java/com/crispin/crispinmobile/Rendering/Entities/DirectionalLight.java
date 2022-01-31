package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;

/**
 * Text
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see RenderObject
 * @since 1.0
 */
public class DirectionalLight {
    // The intensity of the light. This effects specular and diffuse calculations (not ambience)
    private final float DEFAULT_INTENSITY = 1.0f;

    // The default strength of the lights ambience effect. This means that the object will always
    // be lit with a minimal brightness (e.g. 0.4 means the object is of 40% its brightness prior to
    // any lighting calculations)
    private final float DEFAULT_AMBIENCE_STRENGTH = 0.4f;

    // The default strength of the specular glint
    private final float DEFAULT_SPECULAR_STRENGTH = 0.5f;

    // direction of the light source
    public float dx;
    public float dy;
    public float dz;

    // The colour of the light
    public float red;
    public float green;
    public float blue;

    // Lights ambient strength (when the light is present how does it effect the entire scene
    // no matter how far away it is). By default this is DEFAULT_AMBIENT_STRENGTH
    public float ambientStrength;

    // Intensity of the light
    public float diffuseStrength;

    // Strength of the specular glint. This gives the object a shiny look. Note that this can also
    // be controlled with specular texture maps on object materials (the advantage of that is that
    // an objects surface can appear with varying levels of shininess)
    public float specularStrength;

    public DirectionalLight(float dx, float dy, float dz, float red, float green, float blue) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.diffuseStrength = DEFAULT_INTENSITY;
        this.ambientStrength = DEFAULT_AMBIENCE_STRENGTH;
        this.specularStrength = DEFAULT_SPECULAR_STRENGTH;
    }

    public DirectionalLight(float dx, float dy, float dz) {
        this(dx, dy, dz, 1.0f, 1.0f, 1.0f);
    }

    public DirectionalLight(float dx, float dy, float red, float green, float blue) {
        this(dx, dy, 0.0f, red, green, blue);
    }

    public DirectionalLight(float dx, float dy) {
        this(dx, dy, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public DirectionalLight(final Vec3 direction, final Colour colour) {
        this(direction.x, direction.y, direction.z, colour.red, colour.green, colour.blue);
    }

    public DirectionalLight(final Vec2 direction, final Colour colour) {
        this(direction.x, direction.y, colour.red, colour.green, colour.blue);
    }

    public DirectionalLight(final Vec3 direction) {
        this(direction.x, direction.y, direction.z);
    }

    public DirectionalLight(final Vec2 direction) {
        this(direction.x, direction.y);
    }

    public DirectionalLight() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setDirection(float dx, float dy, float dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public void setDirection(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Vec3 getDirection() {
        return new Vec3(dx, dy, dz);
    }

    public void setDirection(Vec3 direction) {
        dx = direction.x;
        dy = direction.y;
        dz = direction.z;
    }

    public void setDirection(Vec2 direction) {
        dx = direction.x;
        dy = direction.y;
    }

    public Vec2 getDirection2D() {
        return new Vec2(dx, dy);
    }

    public void setColour(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Colour getColour() {
        return new Colour(red, green, blue);
    }

    public void setColour(final Colour colour) {
        red = colour.red;
        green = colour.green;
        blue = colour.blue;
    }

    public float getDiffuseStrength() {
        return diffuseStrength;
    }

    public void setDiffuseStrength(float diffuseStrength) {
        this.diffuseStrength = diffuseStrength;
    }

    public float getAmbientStrength() {
        return ambientStrength;
    }

    public void setAmbientStrength(float ambientStrength) {
        this.ambientStrength = ambientStrength;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }

    public void setSpecularStrength(float specularStrength) {
        this.specularStrength = specularStrength;
    }
}
