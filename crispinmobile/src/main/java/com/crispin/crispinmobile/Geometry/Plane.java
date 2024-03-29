package com.crispin.crispinmobile.Geometry;

/**
 * Plane represents a face position and the direction it is facing. It is used in geometry
 * calculations.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Vec3
 * @see Geometry
 * @since 1.0
 */
public class Plane {
    // Tag used in logging output
    private static final String TAG = "Plane";

    // Position of the plane
    public final Vec3 position;

    // Direction the plane is facing (the direction)
    public final Vec3 direction;

    /**
     * Construct a plane object
     *
     * @param position  The position of the plane
     * @param direction The direction of the plane (direction the plane is facing as a direction)
     * @since 1.0
     */
    public Plane(Vec3 position, Vec3 direction) {
        this.position = position;
        this.direction = direction;
    }

    /**
     * Get a string that contains the plane data that can be used in a log
     *
     * @return String in the format 'Plane{Vec3[x:X,y:Y,z:Z], Vec3[x:X,y:Y,z:z]}'
     * @since 1.0
     */
    @Override
    public String toString() {
        return TAG + "{" + position.toString() + ", " + direction.toString() + "}";
    }
}