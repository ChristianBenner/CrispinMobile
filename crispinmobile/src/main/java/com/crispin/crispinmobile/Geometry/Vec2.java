package com.crispin.crispinmobile.Geometry;

/**
 * Vec2 provides a singular object that can represent a two dimensional (x and y) direction. It is
 * the base class for Vec3.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Vec3
 * @since 1.0
 */
public class Vec2 {
    // Tag used in logging output
    private static final String TAG = "Vec2";

    // The x dimension value
    public float x;

    // The y dimension value
    public float y;

    /**
     * Construct a 2D vector object
     *
     * @param x The x dimension value
     * @param y The y dimension value
     * @since 1.0
     */
    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a 2D vector object copying values from another
     *
     * @param v Vector to copy from
     * @since 1.0
     */
    public Vec2(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Construct a 2D vector object copying x,y values from a 3D vector object
     *
     * @param v Vector to copy from
     * @since 1.0
     */
    public Vec2(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Construct a 2D vector object copying values from a Scale2D object
     *
     * @param s Scale2D object to copy from
     * @since 1.0
     */
    public Vec2(Scale2D s) {
        this.x = s.w;
        this.y = s.h;
    }

    /**
     * Construct a 2D vector object copying x, y values from a Scale3D object
     *
     * @param s Scale3D object to copy from
     * @since 1.0
     */
    public Vec2(Scale3D s) {
        this.x = s.w;
        this.y = s.h;
    }

    /**
     * Set xy of a 2D vector object
     *
     * @param x The x dimension value
     * @param y The y dimension value
     * @since 1.0
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set xy of a 2D vector object
     *
     * @param v Vec2 containing new position
     * @since 1.0
     */
    public void set(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Construct a 2D direction object with default values (x: 0.0 and y: 0.0)
     *
     * @since 1.0
     */
    public Vec2() {
        this(0.0f, 0.0f);
    }

    /**
     * Get the magnitude of the direction
     *
     * @return The magnitude of the direction as a float
     * @since 1.0
     */
    public float getMagnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Get the length (magnitude) of the direction
     *
     * @return The length (magnitude) of the direction as a float
     * @since 1.0
     */
    public float length() {
        return getMagnitude();
    }

    /**
     * Get the dot product of the direction
     *
     * @return The dot product of the direction as a float
     * @since 1.0
     */
    public float getDotProduct(Vec2 other) {
        return (x * other.x) + (y * other.y);
    }

    /**
     * Scale the direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies both dimensions x and y)
     * @since 1.0
     */
    public void scale(float scale) {
        this.x *= scale;
        this.y *= scale;
    }

    /**
     * Scale each dimension of the direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @since 1.0
     */
    public void scale(float x, float y) {
        this.x *= x;
        this.y *= y;
    }

    /**
     * Subtract a Vec2 from this vector
     *
     * @param vector Vector to subtract from this vector
     * @since 1.0
     */
    public void minus(Vec2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    /**
     * Add a Vec2 to this vector
     *
     * @param vector Vector to add to this vector
     * @since 1.0
     */
    public void plus(Vec2 vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    /**
     * Invert the vector
     *
     * @since 1.0
     */
    public void invert() {
        x = -x;
        y = -y;
    }

    /**
     * Get a string that contains the direction data that can be used in a log
     *
     * @return String in the format 'Vec2[x:X,y:Y]'
     * @since 1.0
     */
    @Override
    public String toString() {
        return TAG + "[x: " + x + ", y: " + y + "]";
    }
}
