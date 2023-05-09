package com.crispin.crispinmobile.Geometry;

/**
 * Scale2D provides a singular object that can represent a two dimensional (w and h) scale
 * multiplier. It is the base class for Scale3D.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Scale3D
 * @since 1.0
 */
public class Scale2D {
    // Tag used in logging output
    private static final String TAG = "Scale2D";

    // The default scale multiplier. It is 1.0 to maintain the scale of the model as it is provided
    protected static final float DEFAULT_SCALE_MULTIPLIER = 1.0f;

    // The width
    public float w;

    // The height
    public float h;

    /**
     * Construct a 2D scale object from another
     *
     * @param scale The other scale object to copy from
     * @since 1.0
     */
    public Scale2D(Scale2D scale) {
        this.w = scale.w;
        this.h = scale.h;
    }

    /**
     * Construct a 2D scale object
     *
     * @param w The w scale multiplier
     * @param h The h scale multiplier
     * @since 1.0
     */
    public Scale2D(float w, float h) {
        this.w = w;
        this.h = h;
    }

    /**
     * Construct a 2D scale object with the default values (w: 1.0 and h: 1.0)
     *
     * @since 1.0
     */
    public Scale2D() {
        this.w = DEFAULT_SCALE_MULTIPLIER;
        this.h = DEFAULT_SCALE_MULTIPLIER;
    }

    /**
     * Get a string that contains the scale data that can be used in a log
     *
     * @return String in the format 'Scale2D[w:W,h:H]'
     * @since 1.0
     */
    @Override
    public String toString() {
        return TAG + "[w:" + w + ",h:" + h + "]";
    }
}
