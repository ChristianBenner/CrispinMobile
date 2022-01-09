package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.RenderObject;

/**
 * The light class provides an interface to lighting elements in a graphical scene. The class allows
 * control over the lights properties so that you can fully customise a lit scene.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         RenderObject
 * @since       1.0
 */
public class Light
{
    // The intensity of the light. This effects specular and diffuse calculations (not ambience)
    private final float DEFAULT_INTENSITY = 1.0f;

    // The default strength of the lights ambience effect. This means that the object will always
    // be lit with a minimal brightness (e.g. 0.4 means the object is of 40% its brightness prior to
    // any lighting calculations)
    private final float DEFAULT_AMBIENCE_STRENGTH = 0.4f;

    // The default strength of the specular glint
    private final float DEFAULT_SPECULAR_STRENGTH = 0.5f;

    // The position of the light source
    private float x;
    private float y;
    private float z;

    // The colour of the light
    private float red;
    private float green;
    private float blue;

    // Intensity of the light
    private float intensity;

    // Lights ambient strength (when the light is present how does it effect the entire scene
    // no matter how far away it is). By default this is DEFAULT_AMBIENT_STRENGTH
    private float ambienceStrength;

    // Strength of the specular glint. This gives the object a shiny look. Note that this can also
    // be controlled with specular texture maps on object materials (the advantage of that is that
    // an objects surface can appear with varying levels of shininess)
    private float specularStrength;

    public Light(float x,
                 float y,
                 float z,
                 float red,
                 float green,
                 float blue) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.intensity = DEFAULT_INTENSITY;
        this.ambienceStrength = DEFAULT_AMBIENCE_STRENGTH;
        this.specularStrength = DEFAULT_SPECULAR_STRENGTH;
    }

    public Light(float x,
                 float y,
                 float z) {
        this(x, y, z, 1.0f, 1.0f, 1.0f);
    }

    public Light(float x,
                 float y,
                 float red,
                 float green,
                 float blue) {
        this(x, y, 0.0f, red, green, blue);
    }

    public Light(float x,
                 float y) {
        this(x, y, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public Light(final Point3D position, final Colour colour) {
        this(position.x, position.y, position.z, colour.getRed(), colour.getGreen(),
                colour.getBlue());
    }

    public Light(final Point2D position, final Colour colour) {
        this(position.x, position.y, colour.getRed(), colour.getGreen(), colour.getBlue());
    }

    public Light(final Point3D position) {
        this(position.x, position.y, position.z);
    }

    public Light(final Point2D position) {
        this(position.x, position.y);
    }

    public Light() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setPosition(Point3D position) {
        x = position.x;
        y = position.y;
        z = position.z;
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D getPosition() {
        return new Point3D(x, y, z);
    }

    public Point2D getPosition2D() {
        return new Point2D(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setColour(final Colour colour) {
        red = colour.getRed();
        green = colour.getGreen();
        blue = colour.getBlue();
    }

    public void setColour(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Colour getColour() {
        return new Colour(red, green, blue);
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setAmbienceStrength(float ambienceStrength) {
        this.ambienceStrength = ambienceStrength;
    }

    public float getAmbienceStrength() {
        return ambienceStrength;
    }

    public void setSpecularStrength(float specularStrength) {
        this.specularStrength = specularStrength;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }
}
