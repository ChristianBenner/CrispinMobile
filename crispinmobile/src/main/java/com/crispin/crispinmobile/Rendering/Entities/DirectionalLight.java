package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

/**
 * Text
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
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

    /**
     * Construct a directional light with a given direction and colour
     *
     * @param dx    Direction x
     * @param dy    Direction y
     * @param dz    Direction z
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
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

    /**
     * Construct a directional light with a given direction
     *
     * @param dx Direction x
     * @param dy Direction y
     * @param dz Direction z
     * @since 1.0
     */
    public DirectionalLight(float dx, float dy, float dz) {
        this(dx, dy, dz, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct a directional light with a given 2D direction and colour
     *
     * @param dx    Direction x
     * @param dy    Direction y
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
    public DirectionalLight(float dx, float dy, float red, float green, float blue) {
        this(dx, dy, 0.0f, red, green, blue);
    }

    /**
     * Construct a directional light with a given 2D direction
     *
     * @param dx Direction x
     * @param dy Direction y
     * @since 1.0
     */
    public DirectionalLight(float dx, float dy) {
        this(dx, dy, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct a directional light with a given direction and colour
     *
     * @param direction Direction vector
     * @param colour    Colour
     * @since 1.0
     */
    public DirectionalLight(final Vec3 direction, final Colour colour) {
        this(direction.x, direction.y, direction.z, colour.red, colour.green, colour.blue);
    }

    /**
     * Construct a directional light with a given 2D direction and colour
     *
     * @param direction Direction vector
     * @param colour    Colour
     * @since 1.0
     */
    public DirectionalLight(final Vec2 direction, final Colour colour) {
        this(direction.x, direction.y, colour.red, colour.green, colour.blue);
    }

    /**
     * Construct a directional light with a given direction
     *
     * @param direction Direction vector
     * @since 1.0
     */
    public DirectionalLight(final Vec3 direction) {
        this(direction.x, direction.y, direction.z);
    }

    /**
     * Construct a directional light with a given 2D direction
     *
     * @param direction Direction vector
     * @since 1.0
     */
    public DirectionalLight(final Vec2 direction) {
        this(direction.x, direction.y);
    }

    /**
     * Construct a directional light with no direction. Note that no light will be produced until
     * a direction is provided
     *
     * @since 1.0
     */
    public DirectionalLight() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Set the light direction
     *
     * @param dx Direction x
     * @param dy Direction y
     * @param dz Direction z
     * @since 1.0
     */
    public void setDirection(float dx, float dy, float dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    /**
     * Set the light direction
     *
     * @param dx Direction x
     * @param dy Direction y
     * @since 1.0
     */
    public void setDirection(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the light direction
     *
     * @return The light direction as a vector
     * @since 1.0
     */
    public Vec3 getDirection() {
        return new Vec3(dx, dy, dz);
    }

    /**
     * Set the light direction
     *
     * @param direction Direction vector
     * @since 1.0
     */
    public void setDirection(Vec3 direction) {
        dx = direction.x;
        dy = direction.y;
        dz = direction.z;
    }

    /**
     * Set the light direction
     *
     * @param direction Direction vector
     * @since 1.0
     */
    public void setDirection(Vec2 direction) {
        dx = direction.x;
        dy = direction.y;
    }

    /**
     * Get the light direction as a 2D vector
     *
     * @return The light direction as a 2D vector
     * @since 1.0
     */
    public Vec2 getDirection2D() {
        return new Vec2(dx, dy);
    }

    /**
     * Set the light colour
     *
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
    public void setColour(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Get the light colour
     *
     * @return Colour of the light
     * @since 1.0
     */
    public Colour getColour() {
        return new Colour(red, green, blue);
    }

    /**
     * Set the light colour
     *
     * @param colour Colour
     * @since 1.0
     */
    public void setColour(final Colour colour) {
        red = colour.red;
        green = colour.green;
        blue = colour.blue;
    }

    /**
     * Get the diffuse strength (strength of simulated light spread)
     *
     * @return The diffuse strength from 0.0-1.0 (where 1.0 is the highest strength)
     * @since 1.0
     */
    public float getDiffuseStrength() {
        return diffuseStrength;
    }

    /**
     * Set the diffuse strength (strength of simulated light spread)
     *
     * @param diffuseStrength   Strength for 0.0-1.0
     * @since 1.0
     */
    public void setDiffuseStrength(float diffuseStrength) {
        this.diffuseStrength = diffuseStrength;
    }

    /**
     * Get the ambient strength
     *
     * @return The ambient strength from 0.0-1.0 (where 1.0 is the highest strength)
     * @since 1.0
     */
    public float getAmbientStrength() {
        return ambientStrength;
    }

    /**
     * Set the ambient strength
     *
     * @param ambientStrength   Strength for 0.0-1.0
     * @since 1.0
     */
    public void setAmbientStrength(float ambientStrength) {
        this.ambientStrength = ambientStrength;
    }

    /**
     * Get the specular strength (reflective glint/shine)
     *
     * @return The specular strength from 0.0-1.0 (where 1.0 is the highest strength)
     * @since 1.0
     */
    public float getSpecularStrength() {
        return specularStrength;
    }

    /**
     * Set the specular strength (reflective glint/shine)
     *
     * @param specularStrength  Strength for 0.0-1.0
     * @since 1.0
     */
    public void setSpecularStrength(float specularStrength) {
        this.specularStrength = specularStrength;
    }
}
