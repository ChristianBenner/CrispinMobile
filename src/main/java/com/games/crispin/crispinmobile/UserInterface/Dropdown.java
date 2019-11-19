package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;

import java.util.ArrayList;

public class Dropdown implements UIObject
{
    class Item
    {
        int id;
        Text text;
        Plane plane;
    }

    private ArrayList<Item> items;
    private Plane collapsedBox;
    private int id;
    private Font font;
    private final float PADDING = 20.0f;
    private boolean expanded;
    private Image expandImg;

    public static final int ALL_BORDERS = 16;
    public static final int NO_BORDERS = 0;
    public static final int OUTER_BORDERS = 1;

    private int disabledBorderFlags;

    public Dropdown()
    {
        items = new ArrayList<>();
        id = 0;
        font = new Font(R.raw.aileron_regular, 64);
        expanded = false;
        disabledBorderFlags = NO_BORDERS;

        collapsedBox = new Plane(new Point2D(200.0f, 400.0f), new Scale2D(800.0f, 100.0f));
        collapsedBox.setBorder(new Border(Colour.BLACK, 4));
        collapsedBox.setColour(Colour.LIGHT_GREY);

        expandImg = new Image(R.drawable.expandicon, 100, 100);
        expandImg.setPosition(collapsedBox.getPosition().x + collapsedBox.getSize().x - expandImg.getSize().x, collapsedBox.getPosition().y);

    }

    public void expand()
    {
        this.expanded = true;
    }

    public void collapse()
    {
        this.expanded = false;
    }

    // returns id of item
    public int addItem(String text)
    {
        Item item = new Item();
        item.id = id;
        item.text = new Text(font, text);
        item.plane = new Plane();
        item.plane.setSize(collapsedBox.getSize());
        item.plane.setColour(collapsedBox.getColour());
        item.plane.setBorder(new Border(Colour.BLACK, 4));
        items.add(item);
        id++;
        return item.id;
    }

    // returns false if already removed
    public boolean removeItem(int key)
    {
        boolean found = false;
        for(int i = 0; i < items.size() && !found; i++)
        {
            if(items.get(i).id == key)
            {
                found = true;
                items.remove(i);
            }
        }

        return found;
    }

    @Override
    public void setPosition(Point3D position) {

    }

    @Override
    public void setPosition(float x, float y, float z) {

    }

    @Override
    public void setPosition(Point2D position) {

    }

    @Override
    public void setPosition(float x, float y) {

    }

    @Override
    public Point2D getPosition() {
        return null;
    }

    @Override
    public void setWidth(float width) {

    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public void setHeight(float height) {

    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public void setSize(Scale2D size)
    {

    }

    @Override
    public Scale2D getSize() {
        return null;
    }

    @Override
    public void setColour(Colour colour) {

    }

    @Override
    public Colour getColour() {
        return null;
    }

    @Override
    public void setOpacity(float alpha) {

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
        disabledBorderFlags = flags;
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    @Override
    public void draw(Camera2D camera) {
        if(expanded)
        {
            for(int i = 0; i < items.size(); i++)
            {
                items.get(i).text.setPosition(PADDING + collapsedBox.getPosition().x, collapsedBox.getPosition().y - (i * collapsedBox.getSize().y) + (collapsedBox.getSize().y / 2.0f) - (items.get(0).text.getHeight() / 2.0f));
                items.get(i).plane.setPosition(collapsedBox.getPosition().x, collapsedBox.getPosition().y - (i * collapsedBox.getSize().y));

                if((disabledBorderFlags & OUTER_BORDERS) == OUTER_BORDERS)
                {
                    if(items.size() != 1)
                    {
                        if(i == 0)
                        {
                            items.get(i).plane.setDisabledBorders(Border.BOTTOM);
                        }
                        else if(i == items.size() - 1)
                        {
                            items.get(i).plane.setDisabledBorders(Border.TOP);
                        }
                        else
                        {
                            items.get(i).plane.setDisabledBorders(Border.TOP | Border.BOTTOM);
                        }
                    }
                }
                else if((disabledBorderFlags & ALL_BORDERS) == ALL_BORDERS)
                {
                    collapsedBox.setDisabledBorders(Border.ALL);
                    items.get(i).plane.setDisabledBorders(Border.ALL);
                }
                else
                {
                    items.get(i).plane.setDisabledBorders(Border.NONE);
                }

                items.get(i).plane.draw(camera);

                if(i == 0)
                {
                    expandImg.draw(camera);
                }

                items.get(i).text.draw(camera);
            }
        }
        else
        {
            if(items.size() > 0)
            {
                if(items.get(0).text != null)
                {
                    items.get(0).text.setPosition(PADDING + collapsedBox.getPosition().x, collapsedBox.getPosition().y + (collapsedBox.getSize().y / 2.0f) - (items.get(0).text.getHeight() / 2.0f));
                    items.get(0).plane.setPosition(collapsedBox.getPosition().x, collapsedBox.getPosition().y);
                    items.get(0).plane.draw(camera);
                    items.get(0).text.draw(camera);
                }
            }

            expandImg.draw(camera);
        }
    }
}
