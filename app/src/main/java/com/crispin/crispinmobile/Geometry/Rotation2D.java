package com.crispin.crispinmobile.Geometry;

/**
 * Rotation2D provides a singular object that can represent two dimensional (x and y) rotations. It
 * is the base class for Rotation3D.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @see         Rotation3D
 * @since       1.0
 */
public class Rotation2D
{
    // Tag used in logging output
    private static final String TAG = "Rotation2D";

    // The angle to rotate
    public float angle;

    // The x axis multiplier
    public float x;

    // The y axis multiplier
    public float y;

    /**
     * Construct a 2D rotation object
     *
     * @param angle The angle to rotate
     * @param x The x axis multiplier
     * @param y The y axis multiplier
     * @since 1.0
     */
    public Rotation2D(float angle,
                      float x,
                      float y)
    {
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a 2D rotation object with default rotation multipliers (x: 0.0 and y: 0.0)
     *
     * @since 1.0
     */
    public Rotation2D()
    {
        this(0.0f, 0.0f, 0.0f);
    }

    /**
     * Get a string that contains the rotation data that can be used in a log
     *
     * @return  String in the format 'Rotation2D[x:X,y:Y]'
     * @since   1.0
     */
    @Override
    public String toString()
    {
        return TAG + "[angle " + angle + ",x:" + x + ",y:" + y + "]";
    }
}
