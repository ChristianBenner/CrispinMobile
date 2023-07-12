package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Geometry.Vec3;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Mesh;

/**
 * EmissiveEdge class provides a light source in the form of a line. This means that the lighting
 * across the edge is uniform. This can help create light sources that span a distance over a line.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Mesh
 * @since 1.0
 */
public class EmissiveEdge {
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

    // The position of the two points that make up the emissive edge
    public float ax;
    public float ay;
    public float az;
    public float bx;
    public float by;
    public float bz;

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
     * Construct an emissive edge with a given position and colour
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param az    Point A z co-ordinate
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @param bz    Point B z co-ordinate
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
    public EmissiveEdge(float ax, float ay, float az, float bx, float by, float bz, float red, float green, float blue) {
        this.ax = ax;
        this.ay = ay;
        this.az = az;
        this.bx = bx;
        this.by = by;
        this.bz = bz;
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
     * Construct an emissive with a given position
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param az    Point A z co-ordinate
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @param bz    Point B z co-ordinate
     * @since 1.0
     */
    public EmissiveEdge(float ax, float ay, float az, float bx, float by, float bz) {
        this(ax, ay, az, bx, by, bz, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct an emissive edge in 2D space with a given position and colour
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @param red   Red channel amount (0.0-1.0)
     * @param green Green channel amount (0.0-1.0)
     * @param blue  Blue channel amount (0.0-1.0)
     * @since 1.0
     */
    public EmissiveEdge(float ax, float ay, float bx, float by, float red, float green, float blue) {
        this(ax, ay, 0.0f, bx, by, 0.0f, red, green, blue);
    }

    /**
     * Construct an emissive edge in 2D space with a given position
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @since 1.0
     */
    public EmissiveEdge(float ax, float ay, float bx, float by) {
        this(ax, ay, 0.0f, bx, by, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct an emissive edge with a given position and colour
     *
     * @param pointA Position vector for point A
     * @param pointB Position vector for point B
     * @param colour   Colour
     * @since 1.0
     */
    public EmissiveEdge(final Vec3 pointA, final Vec3 pointB, final Colour colour) {
        this(pointA.x, pointA.y, pointA.z, pointB.x, pointB.y, pointB.z, colour.red, colour.green,
                colour.blue);
    }

    /**
     * Construct an emissive edge with a given position and colour
     *
     * @param pointA Position vector for point A
     * @param pointB Position vector for point B
     * @param colour   Colour
     * @since 1.0
     */
    public EmissiveEdge(final Vec2 pointA, final Vec2 pointB, final Colour colour) {
        this(pointA.x, pointB.y, 0.0f, pointB.x, pointB.y, 0.0f, colour.red, colour.green,
                colour.blue);
    }

    /**
     * Construct an emissive edge with a given position
     *
     * @param pointA Position vector for point A
     * @param pointB Position vector for point B
     * @since 1.0
     */
    public EmissiveEdge(final Vec3 pointA, final Vec3 pointB) {
        this(pointA.x, pointA.y, pointA.z, pointB.x, pointB.y, pointB.z, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct an emissive edge with a given position
     *
     * @param pointA Position vector for point A
     * @param pointB Position vector for point B
     * @since 1.0
     */
    public EmissiveEdge(final Vec2 pointA, final Vec2 pointB) {
        this(pointA.x, pointA.y, 0.0f, pointB.x, pointB.y, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Construct an emissive edge. As no position is provided this light will be located at the origin.
     *
     * @since 1.0
     */
    public EmissiveEdge() {
        this(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Set the edge position
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param az    Point A z co-ordinate
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @param bz    Point B z co-ordinate
     * @since 1.0
     */
    public void setPosition(float ax, float ay, float az, float bx, float by, float bz) {
        this.ax = ax;
        this.ay = ay;
        this.az = az;
        this.bx = bx;
        this.by = by;
        this.bz = bz;
    }

    /**
     * Set the edge position
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @since 1.0
     */
    public void setPosition(float ax, float ay, float bx, float by) {
        this.ax = ax;
        this.ay = ay;
        this.bx = bx;
        this.by = by;
    }

    /**
     * Set the edge position
     *
     * @param pointA Position vector for point A
     * @param pointB Position vector for point B
     * @since 1.0
     */
    public void setPosition(Vec3 pointA, Vec3 pointB) {
        ax = pointA.x;
        ay = pointA.y;
        az = pointA.z;
        bx = pointB.x;
        by = pointB.y;
        bz = pointB.z;
    }

    /**
     * Set the edge position
     *
     * @param pointA Position vector for point A
     * @param pointB Position vector for point B
     * @since 1.0
     */
    public void setPosition(Vec2 pointA, Vec2 pointB) {
        ax = pointA.x;
        ay = pointA.y;
        bx = pointB.x;
        by = pointB.y;
    }

    /**
     * Set the position of point A
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @param az    Point A z co-ordinate
     * @since 1.0
     */
    public void setPositionA(float ax, float ay, float az) {
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    /**
     * Set the position of point A
     *
     * @param ax    Point A x co-ordinate
     * @param ay    Point A y co-ordinate
     * @since 1.0
     */
    public void setPositionA(float ax, float ay) {
        this.ax = ax;
        this.ay = ay;
    }

    /**
     * Set the position of point A
     *
     * @param pointA Position vector for point A
     * @since 1.0
     */
    public void setPositionA(Vec3 pointA) {
        ax = pointA.x;
        ay = pointA.y;
        az = pointA.z;
    }

    /**
     * Set the position of point A
     *
     * @param pointA Position vector for point A
     * @since 1.0
     */
    public void setPositionA(Vec2 pointA) {
        ax = pointA.x;
        ay = pointA.y;
    }

    /**
     * Set the position of point B
     *
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @param bz    Point B z co-ordinate
     * @since 1.0
     */
    public void setPositionB(float bx, float by, float bz) {
        this.bx = bx;
        this.by = by;
        this.bz = bz;
    }

    /**
     * Set the position of point B
     *
     * @param bx    Point B x co-ordinate
     * @param by    Point B y co-ordinate
     * @since 1.0
     */
    public void setPositionB(float bx, float by) {
        this.bx = bx;
        this.by = by;
    }

    /**
     * Set the position of point B
     *
     * @param pointB Position vector for point B
     * @since 1.0
     */
    public void setPositionB(Vec3 pointB) {
        bx = pointB.x;
        by = pointB.y;
        bz = pointB.z;
    }

    /**
     * Set the position of point B
     *
     * @param pointB Position vector for point B
     * @since 1.0
     */
    public void setPositionB(Vec2 pointB) {
        bx = pointB.x;
        by = pointB.y;
    }

    /**
     * Get the position of point A
     *
     * @return Position as a 3D vector
     * @since 1.0
     */
    public Vec3 getPointA() {
        return new Vec3(ax, ay, az);
    }

    /**
     * Get the position of point B
     *
     * @return Position as a 3D vector
     * @since 1.0
     */
    public Vec3 getPointB() {
        return new Vec3(ax, ay, az);
    }

    /**
     * Get the position of point A
     *
     * @return Position as a 2D vector
     * @since 1.0
     */
    public Vec2 getPointA2D() {
        return new Vec2(ax, ay);
    }

    /**
     * Get the position of point B
     *
     * @return Position as a 2D vector
     * @since 1.0
     */
    public Vec2 getPointB2D() {
        return new Vec2(ax, ay);
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
        this.ax += x;
        this.ay += y;
        this.az += z;
        this.bx += x;
        this.by += y;
        this.bz += z;
    }

    /**
     * Translate the light position
     *
     * @param x x co-ordinate translation
     * @param y y co-ordinate translation
     * @since 1.0
     */
    public void translate(float x, float y) {
        this.ax += x;
        this.ay += y;
        this.bx += x;
        this.by += y;
    }

    /**
     * Translate the light position
     *
     * @param translateVector Vec3 containing translation
     * @since 1.0
     */
    public void translate(Vec3 translateVector) {
        this.ax += translateVector.x;
        this.ay += translateVector.y;
        this.az += translateVector.z;
        this.bx += translateVector.x;
        this.by += translateVector.y;
        this.bz += translateVector.z;
    }

    /**
     * Translate the light position
     *
     * @param translateVector Vec2 containing translation
     * @since 1.0
     */
    public void translate(Vec2 translateVector) {
        this.ax += translateVector.x;
        this.ay += translateVector.y;
        this.bx += translateVector.x;
        this.by += translateVector.y;
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
     * @param constantAttenuation Value for constant attenuation
     * @since 1.0
     */
    public void setConstantAttenuation(float constantAttenuation) {
        this.constantAttenuation = constantAttenuation;
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
     * @param quadraticAttenuation Value for quadratic attenuation
     * @since 1.0
     */
    public void setQuadraticAttenuation(float quadraticAttenuation) {
        this.quadraticAttenuation = quadraticAttenuation;
    }
}
