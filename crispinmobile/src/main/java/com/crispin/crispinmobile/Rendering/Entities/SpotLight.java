package com.crispin.crispinmobile.Rendering.Entities;

import com.crispin.crispinmobile.Geometry.Point2D;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Geometry.Vector2D;
import com.crispin.crispinmobile.Geometry.Vector3D;
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
public class SpotLight
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

    // Default size of the beam
    private final float DEFAULT_SIZE = 0.22f;
    private final float DEFAULT_OUTER_SIZE = 0.26f;

    // The position of the light source
    public float x;
    public float y;
    public float z;

    // Direction of the light source
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

    // Attenuation variables
    public float attenuationConstant;
    public float attenuationLinear;
    public float attenuationQuadratic;

    // Size of the light beam (when to start fading out the radius)
    public float size;

    // Outer size of the light beam (when to finish fading out the radius)
    public float outerSize;

    public SpotLight(float x,
                     float y,
                     float z,
                     float dx,
                     float dy,
                     float dz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.diffuseStrength = DEFAULT_INTENSITY;
        this.ambientStrength = DEFAULT_AMBIENCE_STRENGTH;
        this.specularStrength = DEFAULT_SPECULAR_STRENGTH;
        this.attenuationConstant = DEFAULT_ATTENUATION_CONSTANT;
        this.attenuationLinear = DEFAULT_ATTENUATION_LINEAR;
        this.attenuationQuadratic = DEFAULT_ATTENUATION_QUADRATIC;
        this.size = DEFAULT_SIZE;
        this.outerSize = DEFAULT_OUTER_SIZE;
    }

    public SpotLight(float x, float y, float z) {
        this(x, y, z, 1.0f, 1.0f, 1.0f);
    }

    public SpotLight(float x,
                     float y,
                     float dx,
                     float dy) {
        this(x, y, 0.0f, dx, dy, 0.0f);
    }

    public SpotLight(float x, float y) {
        this(x, y, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public SpotLight(final Point3D position, final Vector3D direction) {
        this(position.x, position.y, position.z, direction.x, direction.y, direction.z);
    }

    public SpotLight(final Point2D position, final Vector2D direction) {
        this(position.x, position.y, 0.0f, direction.x, direction.y, 0.0f);
    }

    public SpotLight(final Point3D position) {
        this(position.x, position.y, position.z);
    }

    public SpotLight(final Point2D position) {
        this(position.x, position.y);
    }

    public SpotLight() {
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

    public void setPosition(Point2D position) {
        x = position.x;
        y = position.y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point3D getPosition() {
        return new Point3D(x, y, z);
    }

    public Point2D getPosition2D() {
        return new Point2D(x, y);
    }

    public void setDirection(Vector3D direction) {
        dx = direction.x;
        dy = direction.y;
        dz = direction.z;
    }

    public void setDirection(float dx, float dy, float dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public void setDirection(Vector2D direction) {
        dx = direction.x;
        dy = direction.y;
    }

    public void setDirection(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Vector3D getDirection() {
        return new Vector3D(dx, dy, dz);
    }

    public Vector2D getDirection2D() {
        return new Vector2D(dx, dy);
    }

    public void setColour(final Colour colour) {
        red = colour.red;
        green = colour.green;
        blue = colour.blue;
    }

    public void setColour(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Colour getColour() {
        return new Colour(red, green, blue);
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
