package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Data.Texture;

public class Button extends InteractiveUI {
    private static final Colour DEFAULT_COLOUR_BORDER = Colour.BLUE;
    private static final Colour DEFAULT_COLOUR_BACKGROUND = Colour.WHITE;
    private static final Colour DEFAULT_COLOUR_TEXT = Colour.BLACK;
    private static final Colour DEFAULT_DISABLED_COLOUR_BORDER = Colour.GREY;
    private static final Colour DEFAULT_DISABLED_COLOUR_BACKGROUND = Colour.LIGHT_GREY;
    private static final Colour DEFAULT_DISABLED_COLOUR_TEXT = Colour.GREY;

    private Font font;
    private String textString;

    private Text text;
    private final Plane plane;

    private final Vec2 position;
    private final Scale2D size;

    private Colour colourBorder;
    private Colour colourBackground;
    private Colour colourText;
    private Colour disabledColourBorder;
    private Colour disabledColourBackground;
    private Colour disabledColourText;

    public Button(Font font, String textString) {
        this.font = font;
        this.textString = textString;
        this.colourBorder = new Colour(DEFAULT_COLOUR_BORDER);
        this.colourBackground = new Colour(DEFAULT_COLOUR_BACKGROUND);
        this.colourText = new Colour(DEFAULT_COLOUR_TEXT);
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        this.position = new Vec2();
        this.size = new Scale2D();
        this.text = new Text(font, textString, true, true, size.w);

        plane = new Plane(size);
        plane.setColour(colourBackground);
        plane.setBorderColour(colourBorder);
        text.setColour(colourText);

        setSize(new Scale2D(200.0f, 200.0f));
        setPosition(new Vec2());
    }

    public Button(Texture texture) {
        this.position = new Vec2();
        this.size = new Scale2D();
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        plane = new Plane(size);

        setSize(new Scale2D(200.0f, 200.0f));
        setPosition(new Vec2());
        setImage(texture);
    }

    public Button(int resourceId) {
        this.position = new Vec2();
        this.size = new Scale2D();
        this.colourBorder = DEFAULT_COLOUR_BORDER;
        this.colourBackground = DEFAULT_COLOUR_BACKGROUND;
        this.colourText = DEFAULT_COLOUR_TEXT;
        this.disabledColourBorder = DEFAULT_DISABLED_COLOUR_BORDER;
        this.disabledColourBackground = DEFAULT_DISABLED_COLOUR_BACKGROUND;
        this.disabledColourText = DEFAULT_DISABLED_COLOUR_TEXT;

        plane = new Plane(size);

        setSize(new Scale2D(200.0f, 200.0f));
        setPosition(new Vec2());
        setImage(resourceId);
    }

    protected void enabled() {
        if (text != null) {
            text.setColour(colourText);
        }

        plane.setBorderColour(colourBorder);
        plane.setColour(colourBackground);
    }

    protected void disabled() {
        if (text != null) {
            text.setColour(disabledColourText);
        }

        plane.setBorderColour(disabledColourBorder);
        plane.setColour(disabledColourBackground);
    }

    public Colour getDisabledBorderColour() {
        return disabledColourBorder;
    }

    public void setDisabledBorderColour(Colour colour) {
        disabledColourBorder = new Colour(colour);
    }

    public Colour getDisabledTextColour() {
        return disabledColourText;
    }

    public void setDisabledTextColour(Colour colour) {
        disabledColourText = new Colour(colour);
    }

    public Colour getDisabledColour() {
        return disabledColourBackground;
    }

    public void setDisabledColour(Colour colour) {
        disabledColourBackground = new Colour(colour);
    }

    public void setBorder(Border border) {
        colourBorder = border.getColour();
        this.plane.setBorder(border);
    }

    public void removeBorder() {
        this.plane.removeBorder();
    }

    public void setImage(Texture texture) {
        plane.setImage(texture);
    }

    public void setImage(int resourceId) {
        plane.setImage(resourceId);
    }

    private void updatePosition() {
        this.plane.setPosition(position);
        this.plane.setSize(size);

        if (text != null) {
            final float TEXT_POS_Y = plane.getPosition().y + (plane.getHeight() / 2.0f) - (text.getHeight() / 2.0f);
            this.text.setPosition(position.x, TEXT_POS_Y);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        updatePosition();
    }

    @Override
    public Vec2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vec2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        updatePosition();
    }

    @Override
    public float getWidth() {
        return size.w;
    }

    @Override
    public void setWidth(float width) {
        if (text != null) {
            this.text = new Text(font, textString, true, true, width);
        }

        this.size.w = width;
        updatePosition();
    }

    @Override
    public float getHeight() {
        return size.h;
    }

    @Override
    public void setHeight(float height) {
        this.size.h = height;
        updatePosition();
    }

    /**
     * Set the size of the UI object
     *
     * @param width  The new width for the object
     * @param height The new height for the object
     * @since 1.0
     */
    @Override
    public void setSize(float width, float height) {
        this.size.w = width;
        this.size.h = height;

        if (text != null) {
//            this.text = new Text(font, textString, true, true, width);
            this.text.setMaxLineWidth(width);
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
    public Scale2D getSize() {
        return size;
    }

    /**
     * Set the size of the UI object
     *
     * @param size The new size of the UI object
     * @since 1.0
     */
    @Override
    public void setSize(Scale2D size) {
        this.setSize(size.w, size.h);
    }

    public void setAlpha(float alpha) {
        this.setOpacity(alpha);
    }

    public Colour getTextColour() {
        return colourText;
    }

    public void setTextColour(Colour colour) {
        colourText = new Colour(colour);

        if (text != null) {
            text.setColour(colourText);
        }
    }

    public void setBorderColour(Colour colour) {
        colourBorder = new Colour(colour);
        plane.setBorderColour(colourBorder);
    }

    @Override
    public Colour getColour() {
        return colourBackground;
    }

    @Override
    public void setColour(Colour colour) {
        colourBackground = new Colour(colour);
        this.plane.setColour(colourBackground);
    }

    @Override
    public float getOpacity() {
        return colourBackground.alpha;
    }

    @Override
    public void setOpacity(float alpha) {
        colourBackground.alpha = alpha;
        colourText.alpha = alpha;

        if (text != null) {
            text.setColour(colourText);
        }

        plane.setColour(colourBackground);
    }

    /**
     * Disable specific borders on the object
     *
     * @param flags The border flags
     * @since 1.0
     */
    @Override
    public void setDisabledBorders(int flags) {

    }

    @Override
    protected void clickEvent(Vec2 position) {
        // Play click sound
    }

    @Override
    protected void dragEvent(Vec2 position) {

    }

    @Override
    protected void releaseEvent(Vec2 position) {

    }

    @Override
    public void draw(Camera2D camera) {
        this.plane.draw(camera);

        if (text != null) {
            text.draw(camera);
        }
    }
}
