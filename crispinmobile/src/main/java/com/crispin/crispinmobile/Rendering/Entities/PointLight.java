package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

/**
 * The light class provides an interface to lighting elements in a graphical scene. The class allows
 * control over the lights properties so that you can fully customise a lit scene.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class PointLight {
    // The intensity of the light. This effects specular and diffuse calculations (not ambience)
    private final float DEFAULT_INTENSITY = 1.0f;

    // The default strength of the lights ambience effect. This means that the object will always
    // be lit with a minimal brightness (e.g. 0.4 means the object is of 40% its brightness prior to
    // any lighting calculations)
    private final float DEFAULT_AMBIENCE_STRENGTH = 0.4f;

    // The default strength of the specular glint
    private final float DEFAULT_SPECULAR_STRENGTH = 0.5f;

    // Default attenuation variables have been set to cover a distance of about 50m
    private final float DEFAULT_ATTENUATION_CONSTANT = 1.0f;
    private final float DEFAULT_ATTENUATION_LINEAR = 0.09f;
    private final float DEFAULT_ATTENUATION_QUADRATIC = 0.032f;

    // The position of the light source
    public float x;
    public float y;
    public float z;

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

    // Attenuation variables
    public float constantAttenuation;
    public float linearAttenuation;
    public float quadraticAttenuation;

    /**
     * Construct a point light with a given position and colour
     *
     * @param x     Position x co-ordinate
     * @param y     Position y co-ordinate
     * @param z     Position z co-ordinate
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
    public PointLight(float x, float y, float z, float red, float green, float blue) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.diffuseStrength = DEFAULT_INTENSITY;
        this.ambientStrength = DEFAULT_AMBIENCE_STRENGTH;
        this.specularStrength = DEFAULT_SPECULAR_STRENGTH;
        this.constantAttenuation = DEFAULT_ATTENUATION_CONSTANT;
        this.linearAttenuation = DEFAULT_ATTENUATION_LINEAR;
        this.quadraticAttenuation = DEFAULT_ATTENUATION_QUADRATIC;
    }

    /**
     * Construct a point light with a given position
     *
     * @param x     Position x co-ordinate
     * @param y     Position y co-ordinate
     * @param z     Position z co-ordinate
     * @since 1.0
     */
    public PointLight(float x, float y, float z) {
        this(x, y, z, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct a point light with a given position and colour
     *
     * @param x     Position x co-ordinate
     * @param y     Position y co-ordinate
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
    public PointLight(float x, float y, float red, float green, float blue) {
        this(x, y, 0.0f, red, green, blue);
    }

    /**
     * Construct a point light with a given position and colour
     *
     * @param x     Position x co-ordinate
     * @param y     Position y co-ordinate
     * @since 1.0
     */
    public PointLight(float x, float y) {
        this(x, y, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct a point light with a given position and colour
     *
     * @param position  Position vector
     * @param colour    Colour
     * @since 1.0
     */
    public PointLight(final Vec3 position, final Colour colour) {
        this(position.x, position.y, position.z, colour.red, colour.green,
                colour.blue);
    }

    /**
     * Construct a point light with a given position and colour
     *
     * @param position  Position vector
     * @param colour    Colour
     * @since 1.0
     */
    public PointLight(final Vec2 position, final Colour colour) {
        this(position.x, position.y, colour.red, colour.green, colour.blue);
    }

    /**
     * Construct a point light with a given position
     *
     * @param position  Position vector
     * @since 1.0
     */
    public PointLight(final Vec3 position) {
        this(position.x, position.y, position.z);
    }

    /**
     * Construct a point light with a given 2D position
     *
     * @param position  Position vector
     * @since 1.0
     */
    public PointLight(final Vec2 position) {
        this(position.x, position.y);
    }

    /**
     * Construct a point light. As no position is provided this light will be located at the origin.
     *
     * @since 1.0
     */
    public PointLight() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Set the light position
     *
     * @param x Position x co-ordinate
     * @param y Position y co-ordinate
     * @param z Position z co-ordinate
     * @since 1.0
     */
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Set the light position
     *
     * @param x Position x co-ordinate
     * @param y Position y co-ordinate
     * @since 1.0
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the light position
     *
     * @return Position as a 3D vector
     * @since 1.0
     */
    public Vec3 getPosition() {
        return new Vec3(x, y, z);
    }

    /**
     * Set the light position
     *
     * @param position  Position vector
     * @since 1.0
     */
    public void setPosition(Vec3 position) {
        x = position.x;
        y = position.y;
        z = position.z;
    }

    /**
     * Set the light position
     *
     * @param position  Position vector
     * @since 1.0
     */
    public void setPosition(Vec2 position) {
        x = position.x;
        y = position.y;
    }

    /**
     * Get the light position
     *
     * @return Position as a 2D vector
     * @since 1.0
     */
    public Vec2 getPosition2D() {
        return new Vec2(x, y);
    }

    /**
     * Translate the light position
     *
     * @param x x co-ordinate translation
     * @param y y co-ordinate translation
     * @param z z co-ordinate translation
     * @since 1.0
     */
    public void translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Translate the light position
     *
     * @param x x co-ordinate translation
     * @param y y co-ordinate translation
     * @since 1.0
     */
    public void translate(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Translate the light position
     *
     * @param translateVector Vec3 containing translation
     * @since 1.0
     */
    public void translate(Vec3 translateVector) {
        this.x += translateVector.x;
        this.y += translateVector.y;
        this.z += translateVector.z;
    }

    /**
     * Translate the light position
     *
     * @param translateVector Vec2 containing translation
     * @since 1.0
     */
    public void translate(Vec2 translateVector) {
        this.x += translateVector.x;
        this.y += translateVector.y;
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
     * @param diffuseStrength Strength for 0.0-1.0
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
     * @param ambientStrength Strength for 0.0-1.0
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
     * @param specularStrength Strength for 0.0-1.0
     * @since 1.0
     */
    public void setSpecularStrength(float specularStrength) {
        this.specularStrength = specularStrength;
    }

    /**
     * Get the constant attenuation
     *
     * @return Constant attenuation
     * @since 1.0
     */
    public float getConstantAttenuation() {
        return constantAttenuation;
    }

    /**
     * Set the constant attenuation
     *
     * @param constantAttenuation   Value for constant attenuation
     * @since 1.0
     */
    public void setConstantAttenuation(float constantAttenuation) {
        this.constantAttenuation = this.constantAttenuation;
    }

    /**
     * Get the linear attenuation
     *
     * @return Linear attenuation
     * @since 1.0
     */
    public float getLinearAttenuation() {
        return linearAttenuation;
    }

    /**
     * Set the linear attenuation
     *
     * @param linearAttenuation Value for linear attenuation
     * @since 1.0
     */
    public void setLinearAttenuation(float linearAttenuation) {
        this.linearAttenuation = linearAttenuation;
    }

    /**
     * Get the quadratic attenuation
     *
     * @return Quadratic attenuation
     * @since 1.0
     */
    public float getQuadraticAttenuation() {
        return quadraticAttenuation;
    }

    /**
     * Set the quadratic attenuation
     *
     * @param quadraticAttenuation  Value for quadratic attenuation
     * @since 1.0
     */
    public void setQuadraticAttenuation(float quadraticAttenuation) {
        this.quadraticAttenuation = quadraticAttenuation;
    }
}
