package com.crispin.crispinmobile.Geometry;

/**
 * Scale3D provides a singular object that can represent a three dimensional (w, h and l) scale
 * multiplier
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Scale2D
 * @since 1.0
 */
public class Scale3D {
    // Tag used in logging output
    private static final String TAG = "Scale2D";

    // The default scale multiplier. It is 1.0 to maintain the scale of the model as it is provided
    protected static final float DEFAULT_SCALE_MULTIPLIER = 1.0f;

    // The width
    public float w;

    // The height
    public float h;

    // The length
    public float l;

    /**
     * Construct a 2D scale object
     *
     * @param w The w scale multiplier
     * @param h The h scale multiplier
     * @param l The l scale multiplier
     * @since 1.0
     */
    public Scale3D(float w, float h, float l) {
        this.w = w;
        this.h = h;
        this.l = l;
    }

    /**
     * Construct a 3D scale object with the default values (w: 1.0, h: 1.0 and l: 1.0)
     *
     * @since 1.0
     */
    public Scale3D() {
        w = DEFAULT_SCALE_MULTIPLIER;
        h = DEFAULT_SCALE_MULTIPLIER;
        l = DEFAULT_SCALE_MULTIPLIER;
    }

    /**
     * Get the 2D scale comprising of width and height components
     *
     * @return 2D scale comprising of width and height components
     * @since 1.0
     */
    public Scale2D toScale2D() {
        return new Scale2D(w, h);
    }

    /**
     * Get a string that contains the scale data that can be used in a log
     *
     * @return String in the format 'Scale3D[w:W,h:H,l:L]'
     * @since 1.0
     */
    @Override
    public String toString() {
        return TAG + "[w:" + w + ",h:" + h + ",l:" + l + "]";
    }
}
