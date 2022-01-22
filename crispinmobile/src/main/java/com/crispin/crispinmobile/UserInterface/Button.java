package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.Texture;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

import glm_.vec2.Vec2;
import glm_.vec4.Vec4;

public class Button extends InteractableUIObject
{
    private static final Vec4 DEFAULT_COLOUR_BORDER = Colour.BLUE;
    private static final Vec4 DEFAULT_COLOUR_BACKGROUND = Colour.WHITE;
    private static final Vec4 DEFAULT_COLOUR_TEXT = Colour.BLACK;
    private static final Vec4 DEFAULT_DISABLED_COLOUR_BORDER = Colour.GREY;
    private static final Vec4 DEFAULT_DISABLED_COLOUR_BACKGROUND = Colour.LIGHT_GREY;
    private static final Vec4 DEFAULT_DISABLED_COLOUR_TEXT = Colour.GREY;

    private Font font;
    private String textString;

    private Text text;
    private Plane plane;

    private Vec2 position;
    private Vec2 size;

    private Vec4 colourBorder;
    private Vec4 colourBackground;
    private Vec4 colourText;
    private Vec4 disabledColourBorder;
    private Vec4 disabledColourBackground;
    private Vec4 disabledColourText;

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

        this.position = new Vec2();
        this.size = new Vec2();
        this.text = new Text(font, textString, true, true, size.x);

        plane = new Plane(size);
        plane.setColour(colourBackground);
        plane.setBorderColour(colourBorder);
        text.setColour(colourText);

        setSize(new Vec2(200.0f, 200.0f));
        setPosition(new Vec2());
    }

    public Button(Texture texture)
    {
        this.position = new Vec2();
        this.size = new Vec2();
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        plane = new Plane(size);

        setSize(new Vec2(200.0f, 200.0f));
        setPosition(new Vec2());
        setImage(texture);
    }

    public Button(int resourceId)
    {
        this.position = new Vec2();
        this.size = new Vec2();
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        plane = new Plane(size);

        setSize(new Vec2(200.0f, 200.0f));
        setPosition(new Vec2());
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

    public void setDisabledBorderColour(Vec4 colour)
    {
        disabledColourBorder = new Vec4(colour);
    }

    public void setDisabledTextColour(Vec4 colour)
    {
        disabledColourText = new Vec4(colour);
    }

    public void setDisabledColour(Vec4 colour)
    {
        disabledColourBackground = new Vec4(colour);
    }

    public Vec4 getDisabledBorderColour()
    {
        return disabledColourBorder;
    }

    public Vec4 getDisabledTextColour()
    {
        return disabledColourText;
    }

    public Vec4 getDisabledColour()
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
    public void setPosition(Vec2 position)
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
    public Vec2 getPosition()
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
    public void setSize(Vec2 size)
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
    public Vec2 getSize()
    {
        return size;
    }

    @Override
    public void setColour(Vec4 colour)
    {
        colourBackground = colour;
        this.plane.setColour(colourBackground);
    }

    public void setAlpha(float alpha)
    {
        this.setOpacity(alpha);
    }

    public void setTextColour(Vec4 colour)
    {
        colourText = colour;

        if(text != null)
        {
            text.setColour(colourText);
        }
    }

    public Vec4 getTextColour()
    {
        return colourText;
    }

    public void setBorderColour(Vec4 colour)
    {
        colourBorder = colour;
        plane.setBorderColour(colourBorder);
    }

    @Override
    public Vec4 getColour()
    {
        return colourBackground;
    }

    @Override
    public void setOpacity(float alpha)
    {
        colourBackground.w = alpha;
        colourText.w = alpha;

        if(text != null)
        {
            text.setColour(colourText);
        }

        plane.setColour(colourBackground);
    }

    @Override
    public float getOpacity()
    {
        return colourBackground.w;
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
    protected void clickEvent(Vec2 position)
    {
        // Play click sound
    }

    @Override
    protected void dragEvent(Vec2 position)
    {

    }

    @Override
    protected void releaseEvent(Vec2 position)
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
