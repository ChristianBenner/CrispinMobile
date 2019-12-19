package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class LinearLayout implements UIObject
{
    private static float DEFAULT_PADDING_X = 10.0f;
    private static float DEFAULT_PADDING_Y = 10.0f;

    private boolean vertical;
    private ArrayList<UIObject> uiObjects;
    private float cursorX;
    private float cursorY;
    private float endX;
    private float endY;
    private Scale2D padding;

    private boolean automaticWidth;
    private boolean automaticHeight;

    private Plane background;
    private boolean showBackground;

    private Point2D position;
    private Scale2D size;

    public LinearLayout(Point2D position, Scale2D size, boolean vertical)
    {
        this.position = new Point2D();
        this.size = new Scale2D();
        this.padding = new Scale2D();

        this.vertical = vertical;
        this.showBackground = false;
        this.background = new Plane(position, size);
        background.setBorderColour(Colour.BLACK);

        this.automaticWidth = size.x == 0.0f;
        this.automaticHeight = size.y == 0.0f;

        uiObjects = new ArrayList<>();
        endX = 0.0f;
        endY = 0.0f;

        cursorX = 0.0f;
        cursorY = 0.0f;

        setSize(size);
        setPosition(position);
        setPadding(new Scale2D(DEFAULT_PADDING_X, DEFAULT_PADDING_Y));
    }

    public LinearLayout(Point2D position, Scale2D size)
    {
        this(position, size, false);
    }

    public LinearLayout(Point2D position)
    {
        this(position, new Scale2D(0.0f, 0.0f), false);
    }

    public LinearLayout(Scale2D size)
    {
        this(new Point2D(), size, false);
    }

    public LinearLayout(boolean vertical)
    {
        this(new Point2D(), new Scale2D(0.0f, 0.0f), vertical);
    }

    public LinearLayout()
    {
        this(new Point2D(), new Scale2D(0.0f, 0.0f), false);
    }

    public void addUI(UIObject uiObject)
    {
        uiObjects.add(uiObject);
    }

    public void setPadding(Scale2D padding)
    {
        this.padding.x = padding.x;
        this.padding.y = padding.y;

        updateUIElementPositions();
    }

    private void updatePosition()
    {
        // Resize plane
        background.setPosition(position);

        if(!automaticHeight)
        {
            background.setSize(size);
        }

        if(automaticWidth)
        {
            size.x = Crispin.getSurfaceWidth() - position.x;
        }

        updateUIElementPositions();
    }

    @Override
    public void setPosition(Point2D position)
    {
        this.setPosition(position.x, position.y);
    }

    @Override
    public void setPosition(float x, float y)
    {
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
    public float getHeight() {
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
        return new Scale2D();
    }

    public void setColour(Colour colour)
    {
        this.showBackground = true;
        this.background.setColour(colour);
    }

    @Override
    public Colour getColour() {
        return null;
    }

    private void updateUIElementPositions()
    {
        endX = 0.0f;
        endY = 0.0f;
        cursorX = 0.0f;
        cursorY = 0.0f;

        for(UIObject uiObject : uiObjects)
        {
            positionUIObject(uiObject);
        }
    }

    private void positionUIObject(UIObject uiObject)
    {
        if(vertical)
        {
            // UI Start position
            final float POS_X = position.x + cursorX;
            final float POS_Y = position.y + cursorY + padding.y;

            cursorY += padding.y + uiObject.getHeight();
        }
        else
        {
            // Check if the object will fit on the current line
            if(size.x != 0 && cursorX + uiObject.getWidth() > size.x)
            {
                cursorY += endY;
                cursorX = 0.0f;
                endY = 0.0f;
            }

            // UI start position
            final float POS_X = position.x + cursorX;
            final float POS_Y = position.y + cursorY;

            // Set the end of the UI object as the new cursorX
            cursorX += padding.x + uiObject.getWidth();

            // If the object and width exceeds the current end X, set the new end X
            if(POS_X + uiObject.getWidth() > endX)
            {
                endX = POS_X + uiObject.getWidth();
            }

            // If the object and height exceeds the current end Y, set the new end Y
            if(uiObject.getHeight() + padding.y > endY)
            {
                endY = uiObject.getHeight() + padding.y;

                final float NEW_HEIGHT = POS_Y + endY - position.y;
                if(automaticHeight && NEW_HEIGHT > size.y)
                {
                    size.y = NEW_HEIGHT;
                    background.setHeight(size.y);
                }
            }

            uiObject.setPosition(POS_X, POS_Y);
        }
    }

    public void add(UIObject uiObject)
    {
        uiObject.setOpacity(background.getOpacity());
        this.uiObjects.add(uiObject);
        positionUIObject(uiObject);
    }

    public void setOpacity(float alpha)
    {
        background.setOpacity(alpha);

        for(int i = 0; i < uiObjects.size(); i++)
        {
            uiObjects.get(i).setOpacity(alpha);
        }
    }

    @Override
    public float getOpacity() {
        return 0;
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
    public void draw(Camera2D camera)
    {
        glDisable(GL_DEPTH_TEST);

        if(showBackground)
        {
            background.draw(camera);
        }

        // Iterate through the ui objects drawing them
        for(int i = 0; i < uiObjects.size(); i++)
        {
            uiObjects.get(i).draw(camera);
        }

        glEnable(GL_DEPTH_TEST);
    }
}
