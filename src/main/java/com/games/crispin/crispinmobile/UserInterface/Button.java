package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class Button extends InteractableUIObject
{
    private static final Colour DEFAULT_COLOUR_BORDER = Colour.BLUE;
    private static final Colour DEFAULT_COLOUR_BACKGROUND = Colour.CYAN;
    private static final Colour DEFAULT_COLOUR_TEXT = Colour.BLACK;
    private static final Colour DEFAULT_DISABLED_COLOUR_BORDER = Colour.GREY;
    private static final Colour DEFAULT_DISABLED_COLOUR_BACKGROUND = Colour.LIGHT_GREY;
    private static final Colour DEFAULT_DISABLED_COLOUR_TEXT = Colour.GREY;

    private Font font;
    private String textString;

    private Text text;
    private Plane plane;

    private Point2D position;
    private Scale2D size;

    private Colour colourBorder;
    private Colour colourBackground;
    private Colour colourText;
    private Colour disabledColourBorder;
    private Colour disabledColourBackground;
    private Colour disabledColourText;

    public Button(Font font, String textString)
    {
        this.font = font;
        this.textString = textString;
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        this.position = new Point2D();
        this.size = new Scale2D();
        this.text = new Text(font, textString, true, true, size.x);

        plane = new Plane(size);
        plane.setColour(colourBackground);
        plane.setBorderColour(colourBorder);
        text.setColour(colourText);

        setSize(new Scale2D(200.0f, 200.0f));
        setPosition(new Point2D());
    }

    public Button(Texture texture)
    {
        this.position = new Point2D();
        this.size = new Scale2D();
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        plane = new Plane(size);

        setSize(new Scale2D(200.0f, 200.0f));
        setPosition(new Point2D());
        setImage(texture);
    }

    public Button(int resourceId)
    {
        this.position = new Point2D();
        this.size = new Scale2D();
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        plane = new Plane(size);

        setSize(new Scale2D(200.0f, 200.0f));
        setPosition(new Point2D());
        setImage(resourceId);
    }

    protected void enabled()
    {
        if(text != null)
        {
            text.setColour(colourText);
        }

        plane.setBorderColour(colourBorder);
        plane.setColour(colourBackground);
    }

    protected void disabled()
    {
        if(text != null)
        {
            text.setColour(disabledColourText);
        }

        plane.setBorderColour(disabledColourBorder);
        plane.setColour(disabledColourBackground);
    }

    public void setDisabledBorderColour(Colour colour)
    {
        disabledColourBorder = new Colour(colour);
    }

    public void setDisabledTextColour(Colour colour)
    {
        disabledColourText = new Colour(colour);
    }

    public void setDisabledColour(Colour colour)
    {
        disabledColourBackground = new Colour(colour);
    }

    public Colour getDisabledBorderColour()
    {
        return disabledColourBorder;
    }

    public Colour getDisabledTextColour()
    {
        return disabledColourText;
    }

    public Colour getDisabledColour()
    {
        return disabledColourBackground;
    }

    public void setBorder(Border border)
    {
        colourBorder = border.getColour();
        this.plane.setBorder(border);
    }

    public void removeBorder()
    {
        this.plane.removeBorder();
    }

    public void setImage(Texture texture)
    {
        plane.setImage(texture);
    }

    public void setImage(int resourceId)
    {
        plane.setImage(resourceId);
    }

    private void updatePosition()
    {
        this.plane.setPosition(position);
        this.plane.setSize(size);

        if(text != null)
        {
            final float TEXT_POS_Y = plane.getPosition().y + (plane.getHeight() / 2.0f) - (text.getHeight() / 2.0f);
            this.text.setPosition(position.x, TEXT_POS_Y);
        }
    }

    @Override
    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
        updatePosition();
    }

    @Override
    public void setPosition(float x,
                            float y) {
        this.position.x = x;
        this.position.y = y;
        updatePosition();
    }

    @Override
    public Point2D getPosition()
    {
        return position;
    }

    @Override
    public void setWidth(float width)
    {
        if(text != null)
        {
            this.text = new Text(font, textString, true, true, width);
        }

        this.size.x = width;
        updatePosition();
    }

    @Override
    public float getWidth()
    {
        return size.x;
    }

    @Override
    public void setHeight(float height)
    {
        this.size.y = height;
        updatePosition();
    }

    @Override
    public float getHeight()
    {
        return size.y;
    }

    /**
     * Set the size of the UI object
     *
     * @param size  The new size of the UI object
     * @since 1.0
     */
    @Override
    public void setSize(Scale2D size)
    {
        this.setSize(size.x, size.y);
    }

    /**
     * Set the size of the UI object
     *
     * @param width     The new width for the object
     * @param height    The new height for the object
     * @since 1.0
     */
    @Override
    public void setSize(float width, float height)
    {
        this.size.x = width;
        this.size.y = height;

        if(text != null)
        {
            this.text = new Text(font, textString, true, true, width);
        }

        updatePosition();
    }

    /**
     * Get the size of the UI object
     *
     * @return The size of the UI object
     * @since 1.0
     */
    @Override
    public Scale2D getSize()
    {
        return size;
    }

    @Override
    public void setColour(Colour colour)
    {
        colourBackground = colour;
        this.plane.setColour(colourBackground);
    }

    public void setAlpha(float alpha)
    {
        this.setOpacity(alpha);
    }

    public void setTextColour(Colour colour)
    {
        colourText = colour;

        if(text != null)
        {
            text.setColour(colourText);
        }
    }

    public Colour getTextColour()
    {
        return colourText;
    }

    public void setBorderColour(Colour colour)
    {
        colourBorder = colour;
        plane.setBorderColour(colourBorder);
    }

    @Override
    public Colour getColour()
    {
        return colourBackground;
    }

    @Override
    public void setOpacity(float alpha)
    {
        colourBackground.setAlpha(alpha);
        colourText.setAlpha(alpha);

        if(text != null)
        {
            text.setColour(colourText);
        }

        plane.setColour(colourBackground);
    }

    @Override
    public float getOpacity()
    {
        return colourBackground.getAlpha();
    }

    /**
     * Disable specific borders on the object
     *
     * @param flags The border flags
     * @since 1.0
     */
    @Override
    public void setDisabledBorders(int flags)
    {

    }

    @Override
    protected void clickEvent(Point2D position)
    {
        // Play click sound
    }

    @Override
    protected void dragEvent(Point2D position)
    {

    }

    @Override
    protected void releaseEvent(Point2D position)
    {

    }

    @Override
    public void draw(Camera2D camera)
    {
        glDisable(GL_DEPTH_TEST);
        this.plane.draw(camera);

        if(text != null)
        {
            text.draw(camera);
        }

        glEnable(GL_DEPTH_TEST);
    }
}
