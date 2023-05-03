package com.crispin.crispinmobile.Geometry;

/**
 * Vec3 provides a singular object that can represent a three dimensional (x, y and z) direction.
 * It is a subclass of the Vec2 class.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Vec2
 * @since 1.0
 */
public class Vec3 {
    // Tag used in logging output
    private static final String TAG = "Vec3";

    // The x dimension value
    public float x;

    // The y dimension value
    public float y;

    // The z dimension value
    public float z;

    /**
     * Construct a 3D vector object
     *
     * @param x The x dimension value
     * @param y The y dimension value
     * @param z The z dimension value
     * @since 1.0
     */
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct a 3D vector object from a 2D vector and a z value
     *
     * @param v Vector to copy from
     * @param z Z value
     * @since 1.0
     */
    public Vec3(Vec2 v, float z) {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }

    /**
     * Construct a 3D vector object copying values from another
     *
     * @param v Vector to copy from
     * @since 1.0
     */
    public Vec3(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Construct a 3D direction object with default values (x: 0.0, y: 0.0 and z: 0.0)
     *
     * @since 1.0
     */
    public Vec3() {
        this(0.0f, 0.0f, 0.0f);
    }

    /**
     * Set xyz of a 3D vector object
     *
     * @param x The x dimension value
     * @param y The y dimension value
     * @param z The z dimension value
     * @since 1.0
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Set xyz of a 3D vector object
     *
     * @param v Vec3 containing new position
     * @since 1.0
     */
    public void set(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /**
     * Get the length (magnitude) of the direction
     *
     * @return The length (magnitude) of the direction as a float
     * @since 1.0
     */
    public float length() {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * Get the cross product of two vectors
     *
     * @param other The other direction
     * @return The cross product direction
     * @since 1.0
     */
    public Vec3 crossProduct(Vec3 other) {
        return new Vec3((y * other.z) - (z * other.y), (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }

    /**
     * Invert the vector
     *
     * @since 1.0
     */
    public void invert() {
        x = -x;
        y = -y;
        z = -y;
    }

    /**
     * Get the dot product of two vectors
     *
     * @param other The other direction
     * @return The dot product direction
     * @since 1.0
     */
    public float dotProduct(Vec3 other) {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    /**
     * Divide each component of the vector
     *
     * @param divisor
     * @since 1.0
     */
    public void divide(float divisor) {
        this.x /= divisor;
        this.y /= divisor;
        this.z /= divisor;
    }

    /**
     * Scale the direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x, y and z)
     * @since 1.0
     */
    public void scale(float scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
    }

    /**
     * Scale each dimension of the direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @param z Scale multiplier for the z dimension
     * @since 1.0
     */
    public void scale(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
    }

    /**
     * Subtract a Vec3 from this vector
     *
     * @param vector Vector to subtract from this vector
     * @since 1.0
     */
    public void minus(Vec3 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        this.z -= vector.z;
    }

    /**
     * Add a Vec3 to this vector
     *
     * @param vector Vector to add to this vector
     * @since 1.0
     */
    public void plus(Vec3 vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
    }

    /**
     * Translate the vector
     *
     * @param x The x co-ordinate
     * @param y The y co-ordinate
     * @param z The z co-ordinate
     * @since 1.0
     */
    public void translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Translate the vector
     *
     * @param vector A Vec3 object containing x, y and z translation
     * @since 1.0
     */
    public void translate(Vec3 vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
    }

    /**
     * Get a string that contains the direction data that can be used in a log
     *
     * @return String in the format 'Vec2[x:X,y:Y]'
     * @since 1.0
     */
    @Override
    public String toString() {
        return TAG + "[x: " + x + ", y: " + y + ", z: " + z + "]";
    }
}
