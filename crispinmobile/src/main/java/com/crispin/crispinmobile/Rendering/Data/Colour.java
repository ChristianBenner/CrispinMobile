package com.crispin.crispinmobile.Rendering.Data;

/**
 * Colour class allows you to store RGBA colour data. The class allows you to create your own
 * colours but also contains some pre-defined ones. Colour channels are represented as floats with a
 * range of 0.0 to 1.0.
 *
 * @author      Christian Benner
 * @version     %I%, %G%
 * @since       1.0
 */
public class Colour
{
    // Pre-defined red colour
    public static final Colour RED = new Colour(1.0f, 0.0f, 0.0f);

    // Pre-defined green colour
    public static final Colour GREEN = new Colour(0.0f, 1.0f, 0.0f);

    // Pre-defined blue colour
    public static final Colour BLUE = new Colour(0.0f, 0.0f, 1.0f);

    // Pre-defined black colour
    public static final Colour BLACK = new Colour(0.0f, 0.0f, 0.0f);

    // Pre-defined white colour
    public static final Colour WHITE = new Colour(1.0f, 1.0f, 1.0f);

    // Pre-defined grey colour
    public static final Colour GREY = new Colour(0.5f, 0.5f, 0.5f);

    // Pre-defined light-grey colour
    public static final Colour LIGHT_GREY = new Colour(0.75f, 0.75f, 0.75f);

    // Pre-defined dark-grey colour
    public static final Colour DARK_GREY = new Colour(0.25f, 0.25f, 0.25f);

    // Pre-defined yellow colour
    public static final Colour YELLOW = new Colour(1.0f, 1.0f, 0.0f);

    // Pre-defined cyan colour
    public static final Colour CYAN = new Colour(0.0f, 1.0f, 1.0f);

    // Pre-defined magenta colour
    public static final Colour MAGENTA = new Colour(1.0f, 0.0f, 1.0f);

    // Pre-defined orange colour
    public static final Colour ORANGE = new Colour(1.0f, 0.415f, 0.0f);

    // Default alpha value if one is not provided
    private static final float DEFAULT_ALPHA_VALUE = 1.0f;

    // Default red value if one is not provided
    private static final float DEFAULT_RED_VALUE = 1.0f;

    // Default green value if one is not provided
    private static final float DEFAULT_GREEN_VALUE = 1.0f;

    // Default blue value if one is not provided
    private static final float DEFAULT_BLUE_VALUE = 1.0f;

    // Maximum channel intensity integers
    private static final int MAXIMUM_INTEGER_CHANNEL_INTENSITY = 255;

    // Red channel value
    public float red;

    // Green channel value
    public float green;

    // Blue channel value
    public float blue;

    // Alpha channel value
    public float alpha;

    /**
     * Construct a colour object with RGBA values
     *
     * @param red   The red colour value (0.0 for no red, 1.0 for maximum red)
     * @param green The green colour value (0.0 for no green, 1.0 for maximum green)
     * @param blue  The blue colour value (0.0 for no blue, 1.0 for maximum blue)
     * @param alpha The alpha colour value (0.0 for no alpha, 1.0 for maximum alpha)
     * @since 1.0
     */
    public Colour(float red, float green, float blue, float alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Construct a colour object with RGB values. Uses default alpha value of 1.0
     *
     * @param red   The red colour value (0.0 for no red, 1.0 for maximum red)
     * @param green The green colour value (0.0 for no green, 1.0 for maximum green)
     * @param blue  The blue colour value (0.0 for no blue, 1.0 for maximum blue)
     * @since   1.0
     */
    public Colour(float red, float green, float blue)
    {
        this(red, green, blue, DEFAULT_ALPHA_VALUE);
    }

    /**
     * Construct a colour object using an existing colour object
     * 1.0)
     *
     * @param   colour  An existing colour object to copy
     * @since   1.0
     */
    public Colour(final Colour colour)
    {
        this(colour.red, colour.green, colour.blue, colour.alpha);
    }

    /**
     * Construct a colour object with default RGBA values (Red: 1.0, Green: 1.0, Blue: 1.0, Alpha:
     * 1.0)
     *
     * @since   1.0
     */
    public Colour()
    {
        this(DEFAULT_RED_VALUE, DEFAULT_GREEN_VALUE, DEFAULT_BLUE_VALUE, DEFAULT_ALPHA_VALUE);
    }

    /**
     * Construct a colour object with RGBA values. Provide integers 0-255 for channel intensities.
     *
     * @param red   The red colour value (0 for no red, 255 for maximum red)
     * @param green The green colour value (0 for no green, 255 for maximum green)
     * @param blue  The blue colour value (0 for no blue, 255 for maximum blue)
     * @param alpha The alpha colour value (0 for no alpha, 255 for maximum alpha)
     * @since   1.0
     */
    public Colour(int red, int green, int blue, int alpha)
    {
        this((float)red / MAXIMUM_INTEGER_CHANNEL_INTENSITY,
                (float)green / MAXIMUM_INTEGER_CHANNEL_INTENSITY,
                (float)blue / MAXIMUM_INTEGER_CHANNEL_INTENSITY,
                (float)alpha / MAXIMUM_INTEGER_CHANNEL_INTENSITY);
    }

    /**
     * Construct a colour object with RGB values. Provide integers 0-255 for channel intensities.
     *
     * @param red   The red colour value (0 for no red, 255 for maximum red)
     * @param green The green colour value (0 for no green, 255 for maximum green)
     * @param blue  The blue colour value (0 for no blue, 255 for maximum blue)
     * @since   1.0
     */
    public Colour(int red, int green, int blue)
    {
        this(red, green, blue, MAXIMUM_INTEGER_CHANNEL_INTENSITY);
    }
}
