package com.games.crispin.crispinmobile.UserInterface;

import android.graphics.Point;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

public class Button implements UIObject
{
    private ArrayList<TouchListener> buttonListeners = new ArrayList<>();

    private Text text;
    private Plane plane;

    private Point3D position;
    private Scale2D size;
    private boolean clicked = false;

    public Button(Font font, String text)
    {
        this.size = new Scale2D(200.0f, 200.0f);
        this.position = new Point3D();
        this.text = new Text(font, text, true, true, 200.0f);
        this.text.showBounds();
        plane = new Plane(size);
        plane.setColour(Colour.CYAN);
        plane.setBorderColour(Colour.BLUE);

        updatePosition();
    }

    public Button(Texture texture)
    {
        this.size = new Scale2D(200.0f, 200.0f);
        this.position = new Point3D();
        plane = new Plane(size);

        setImage(texture);
        updatePosition();
    }

    public Button(int resourceId)
    {
        this.size = new Scale2D(200.0f, 200.0f);
        this.position = new Point3D();
        plane = new Plane(size);

        setImage(resourceId);
        updatePosition();
    }

    public void addButtonListener(TouchListener listener)
    {
        buttonListeners.add(listener);
    }

    public void removeButtonListener(TouchListener listener)
    {
        buttonListeners.remove(listener);
    }

    public boolean isClicked()
    {
        return clicked;
    }

    public void sendClickEvent(Point2D position)
    {
        clicked = true;
        final TouchEvent CLICK_EVENT = new TouchEvent(this, TouchEvent.Event.CLICK, position);
        for(final TouchListener buttonListener : buttonListeners)
        {
            buttonListener.touchEvent(CLICK_EVENT);
        }
    }

    public void sendReleaseEvent(Point2D position)
    {
        clicked = false;
        final TouchEvent RELEASE_EVENT = new TouchEvent(this, TouchEvent.Event.RELEASE, position);
        for(final TouchListener buttonListener : buttonListeners)
        {
            buttonListener.touchEvent(RELEASE_EVENT);
        }
    }

    public void sendDownEvent(Point2D position)
    {
        // Send the drag touch event
        final TouchEvent DOWN_EVENT = new TouchEvent(this, TouchEvent.Event.DOWN, position);
        for(final TouchListener buttonListener : buttonListeners)
        {
            buttonListener.touchEvent(DOWN_EVENT);
        }
    }

    public boolean interacts(Point2D pointer)
    {
        Point2D pos = new Point2D();
        pos.x = pointer.x;
        pos.y = Crispin.getSurfaceHeight() - pointer.y;

        // Check if the pointer is inside the button
        if(pos.x > position.x && pos.x < position.x + getWidth() &&
                pos.y < position.y + getHeight() && pos.y > position.y)
        {
            System.out.println("INTERSECTS");
            return true;
        }

        System.out.println("Point: " + pointer + ", Pos: " + position + ", Size: " + size);
        System.out.println("DOES NOT INTERSECT");
        return false;
    }

    public void setBorder(Border border)
    {
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

        if(text != null)
        {
            final float TEXT_POS_Y = plane.getPosition().y + (plane.getHeight() / 2.0f) - (text.getHeight() / 2.0f);
            this.text.setPosition(position.x, TEXT_POS_Y);
        }
    }

    @Override
    public void setPosition(Point3D position) {
        this.position = position;
        updatePosition();
    }

    @Override
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        updatePosition();
    }

    @Override
    public void setPosition(Point2D position) {
        this.position.x = position.x;
        this.position.y = position.y;
        updatePosition();
    }

    @Override
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        updatePosition();
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setWidth(float width) {
        this.size.x = width;
        updatePosition();
    }

    @Override
    public float getWidth() {
        return size.x;
    }

    @Override
    public void setHeight(float height) {
        this.size.y = height;
        updatePosition();
    }

    @Override
    public float getHeight() {
        return size.y;
    }

    @Override
    public void setSize(Scale2D size)
    {
        this.size = size;
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
    public void setColour(Colour colour) {
        if(text != null)
        {
            this.text.setColour(colour);
        }

        this.plane.setColour(colour);
    }

    public void setAlpha(float alpha)
    {
        this.setOpacity(alpha);
    }

    public void setBackgroundColour(Colour colour)
    {
        this.plane.setColour(colour);
    }

    public void setBorderColour(Colour colour)
    {
        this.plane.setBorderColour(colour);
    }

    @Override
    public Colour getColour() {
        return this.text.getColour();
    }

    @Override
    public void setOpacity(float alpha) {
        if(this.text != null)
        {
            this.text.setOpacity(alpha);
        }

        this.plane.setOpacity(alpha);
    }

    @Override
    public float getOpacity() {
        return this.plane.getOpacity();
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
    public void draw(Camera2D camera) {
        glDisable(GL_DEPTH_TEST);
        this.plane.draw(camera);

        if(text != null)
        {
            text.draw(camera);
        }

        glEnable(GL_DEPTH_TEST);
    }
}