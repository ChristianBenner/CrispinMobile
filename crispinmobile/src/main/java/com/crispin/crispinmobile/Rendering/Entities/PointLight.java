package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Rendering.Utilities.RenderObject;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

/**
 * The light class provides an interface to lighting elements in a graphical scene. The class allows
 * control over the lights properties so that you can fully customise a lit scene.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         RenderObject
 * @since       1.0
 */
public class PointLight
{
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
    public float attenuationConstant;
    public float attenuationLinear;
    public float attenuationQuadratic;

    public PointLight(float x,
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
        this.diffuseStrength = DEFAULT_INTENSITY;
        this.ambientStrength = DEFAULT_AMBIENCE_STRENGTH;
        this.specularStrength = DEFAULT_SPECULAR_STRENGTH;
        this.attenuationConstant = DEFAULT_ATTENUATION_CONSTANT;
        this.attenuationLinear = DEFAULT_ATTENUATION_LINEAR;
        this.attenuationQuadratic = DEFAULT_ATTENUATION_QUADRATIC;
    }

    public PointLight(float x,
                      float y,
                      float z) {
        this(x, y, z, 1.0f, 1.0f, 1.0f);
    }

    public PointLight(float x,
                      float y,
                      float red,
                      float green,
                      float blue) {
        this(x, y, 0.0f, red, green, blue);
    }

    public PointLight(float x,
                      float y) {
        this(x, y, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public PointLight(final Vec3 position, final Vec4 colour) {
        this(position.x, position.y, position.z, colour.x, colour.y,
                colour.z);
    }

    public PointLight(final Vec2 position, final Vec4 colour) {
        this(position.x, position.y, colour.x, colour.y, colour.z);
    }

    public PointLight(final Vec3 position) {
        this(position.x, position.y, position.z);
    }

    public PointLight(final Vec2 position) {
        this(position.x, position.y);
    }

    public PointLight() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setPosition(Vec3 position) {
        x = position.x;
        y = position.y;
        z = position.z;
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPosition(Vec2 position) {
        x = position.x;
        y = position.y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec3 getPosition() {
        return new Vec3(x, y, z);
    }

    public Vec2 getPosition2D() {
        return new Vec2(x, y);
    }

    public void setColour(final Vec4 colour) {
        red = colour.x;
        green = colour.y;
        blue = colour.z;
    }

    public void setColour(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Vec4 getColour() {
        return new Vec4(red, green, blue, 1.0f);
    }

    public void setDiffuseStrength(float diffuseStrength) {
        this.diffuseStrength = diffuseStrength;
    }

    public float getDiffuseStrength() {
        return diffuseStrength;
    }

    public void setAmbientStrength(float ambientStrength) {
        this.ambientStrength = ambientStrength;
    }

    public float getAmbientStrength() {
        return ambientStrength;
    }

    public void setSpecularStrength(float specularStrength) {
        this.specularStrength = specularStrength;
    }

    public float getSpecularStrength() {
        return specularStrength;
    }
}
