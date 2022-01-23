package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.R;
import com.crispin.crispinmobile.Rendering.Data.Colour;
import com.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.crispin.crispinmobile.Rendering.Utilities.Texture;

import java.util.ArrayList;

public class Dropdown extends InteractableUIObject
{
    class Item
    {
        int id;
        Text text;
        Plane plane;
    }

    public static final int NO_ITEM_SELECTED = -1;

    private static final Scale2D DEFAULT_SIZE = new Scale2D(600.0f, 100.0f);

    private ArrayList<Item> items;
    private Plane baseBox;
    private Text baseText;
    private Image dropdownStatusImage;

    private int id;
    private Font font;
    private final float PADDING = 20.0f;
    private boolean expanded;



    public static final int ALL_BORDERS = 16;
    public static final int NO_BORDERS = 0;
    public static final int INNER_BORDERS = 1;

    private int disabledBorderFlags;

    private Texture expandIcon;
    private Texture collapseIcon;

    private int selectedId = NO_ITEM_SELECTED;

    private Vec2 position;
    private Scale2D size;
    private Scale2D itemSize;
    private Colour colour;
    private Colour textColour;
    private Colour borderColour;

    private Vec2 overallPosition;

    public Dropdown(String text)
    {
        position = new Vec2();
        size = DEFAULT_SIZE;
        itemSize = DEFAULT_SIZE;
        colour = new Colour();
        textColour = new Colour();
        borderColour = new Colour();
        overallPosition = new Vec2(position);

        items = new ArrayList<>();
        id = 0;
        font = new Font(R.raw.aileron_regular, 64);
        expanded = false;
        disabledBorderFlags = NO_BORDERS;

        // Load textures for the expanding and collapsing of the dropdown
        expandIcon = new Texture(R.drawable.expandicon);
        collapseIcon = new Texture(R.drawable.collapseicon);

        // Create the base box (this is not a selectable item in the list)
        baseBox = new Plane(position, itemSize);
        baseBox.setBorder(new Border(Colour.BLACK, 4));

        // Create text to size on-top of the base box
        baseText = new Text(font, text);

        // Create the image that represents whether or not the dropdown is currently expanded or
        // collapsed
        dropdownStatusImage = new Image(expandIcon, 100, 100);

        setPosition(position);
        setSize(size);
        setColour(Colour.LIGHT_GREY);
        setTextColour(Colour.BLACK);
        setBorderColour(Colour.BLACK);
    }

    public Dropdown()
    {
        this("Select an item");
    }

    protected void enabled()
    {

    }

    protected void disabled()
    {

    }

    public void setStateIcons(Texture expandIcon, Texture collapseIcon)
    {
        this.expandIcon = expandIcon;
        this.collapseIcon = collapseIcon;

        if(expanded)
        {
            dropdownStatusImage.setImage(collapseIcon);
        }
        else
        {
            dropdownStatusImage.setImage(expandIcon);
        }
    }

    public void setStateIcons(int expandIcon, int collapseIcon)
    {
        setStateIcons(new Texture(expandIcon), new Texture(collapseIcon));
    }

    private void updatePosition()
    {
        // Set the position of the base box here (not the position
        baseBox.setPosition(position);
        baseBox.setSize(size);

        // Position the text to be padded to the right and centered on the y-axis
        baseText.setPosition(PADDING + position.x, position.y +
                (itemSize.y / 2.0f) - (baseText.getHeight() / 2.0f));

        // Position the dropdown status image to hug the right side of the base box
        dropdownStatusImage.setPosition(position.x + baseBox.getSize().x -
                dropdownStatusImage.getSize().x, position.y);
        dropdownStatusImage.setSize(dropdownStatusImage.size.x, size.y);

        for(int i = 0; i < items.size(); i++)
        {
            Item item = items.get(i);

            item.text.setPosition(position.x + PADDING, position.y - ((i+1) * itemSize.y) +
                    (itemSize.y / 2.0f) - (item.text.getHeight() / 2.0f));
            item.plane.setPosition(baseBox.getPosition().x, position.y - ((i+1) * itemSize.y));
        }

        applyBorderFlags();

        // Update the overall position (the position including the expanded items)
        if(expanded)
        {
            overallPosition.x = position.x;
            overallPosition.y = position.y - (items.size() * itemSize.y);
        }
        else
        {
            overallPosition.x = position.x;
            overallPosition.y = position.y;
        }
    }

    public void expand()
    {
        this.expanded = true;
        dropdownStatusImage.setImage(collapseIcon);
        overallPosition.y = position.y - (items.size() * itemSize.y);
    }

    public void collapse()
    {
        this.expanded = false;
        dropdownStatusImage.setImage(expandIcon);
        overallPosition.y = position.y;
    }

    public int getSelectedId()
    {
        return selectedId;
    }

    @Override
    protected void clickEvent(Vec2 position)
    {
        selectedId = NO_ITEM_SELECTED;
    }

    @Override
    protected void dragEvent(Vec2 position)
    {

    }

    // Force the selected item to be a specified id
    public void selectItemInList(int id)
    {
        boolean foundItem = false;
        for(int i = 0; i < items.size() && !foundItem; i++)
        {
            if(items.get(i).id == id)
            {
                selectedId = id;
                baseText.setText(items.get(i).text.getTextString());
                foundItem = true;
            }
        }
    }

    @Override
    protected void releaseEvent(Vec2 position)
    {
        // Only check if an item has been selected if the dropdown is already expanded
        if(expanded)
        {
            // Clear the selected item
            selectedId = NO_ITEM_SELECTED;

            // Check if any items interact with the pointer position
            for(Item item : items)
            {
                if(InteractableUIObject.interacts(item.plane, position))
                {
                    selectedId = item.id;

                    baseText.setText(item.text.getTextString());
                }
            }
        }

        // Check if the object still has focus from the pointer
        if(InteractableUIObject.interacts(this, position))
        {
            if(expanded)
            {
                collapse();
            }
            else
            {
                expand();
            }
        }
    }

    // returns id of item
    public int addItem(String text)
    {
        Item item = new Item();
        item.id = id;
        item.text = new Text(font, text);
        item.plane = new Plane();
        item.plane.setSize(itemSize);
        item.plane.setColour(colour);
        item.text.setColour(textColour);
        item.plane.setBorder(new Border(borderColour, 4));
        items.add(item);
        id++;

        updatePosition();

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

        // Recalculate the position of items because one has been removed from the list
        updatePosition();

        return found;
    }

    @Override
    public void setPosition(Vec2 position)
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

    // Return the position of the combo box itself (and not the items)
    public Vec2 getBasePosition()
    {
        return position;
    }

    // Return the height of the combo box itself (and not the items)
    public float getBaseHeight()
    {
        return this.size.y;
    }

    @Override
    public Vec2 getPosition()
    {
        return overallPosition;
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
    public float getHeight()
    {
        return expanded ? itemSize.y * (items.size() + 1) : itemSize.y;
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
     * Set the size of the UI object
     *
     * @param size  The new size of the UI object
     * @since 1.0
     */
    @Override
    public void setSize(Scale2D size)
    {
        setSize(size.x, size.y);
    }

    @Override
    public Scale2D getSize()
    {
        return new Scale2D(size.x, getHeight());
    }

    public void setTextColour(Colour textColour)
    {
        this.textColour = textColour;

        baseText.setColour(textColour);

        for(int i = 0; i < items.size(); i++)
        {
            items.get(i).text.setColour(textColour);
        }
    }

    public Colour getTextColour()
    {
        return textColour;
    }

    public void setBorderColour(Colour borderColour)
    {
        this.borderColour = borderColour;

        baseBox.setBorderColour(borderColour);

        // Set all of the items to have the same border colour
        for(int i = 0; i < items.size(); i++)
        {
            items.get(i).plane.setBorderColour(colour);
        }
    }

    public Colour getBorderColour()
    {
        return borderColour;
    }

    @Override
    public void setColour(Colour colour)
    {
        this.colour = colour;

        baseBox.setColour(colour);

        // Set all of the items to be the same colour
        for(int i = 0; i < items.size(); i++)
        {
            items.get(i).plane.setColour(colour);
        }
    }

    @Override
    public Colour getColour()
    {
        return colour;
    }

    @Override
    public void setOpacity(float alpha)
    {
        baseBox.setAlpha(alpha);

        // Set all of the items to be the same alpha value
        for(int i = 0; i < items.size(); i++)
        {
            items.get(i).plane.setAlpha(alpha);
        }
    }

    @Override
    public float getOpacity()
    {
        return baseBox.getOpacity();
    }

    private void applyBorderFlags()
    {
        for(int i = 0; i < items.size(); i++)
        {
            if((disabledBorderFlags & INNER_BORDERS) == INNER_BORDERS)
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
                baseBox.setDisabledBorders(Border.ALL);
                items.get(i).plane.setDisabledBorders(Border.ALL);
            }
            else
            {
                items.get(i).plane.setDisabledBorders(Border.NONE);
            }
        }
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
        applyBorderFlags();
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    @Override
    public void draw(Camera2D camera)
    {
        if(expanded)
        {
            for(int i = 0; i < items.size(); i++)
            {
                items.get(i).plane.draw(camera);
                items.get(i).text.draw(camera);
            }
        }

        baseBox.draw(camera);
        baseText.draw(camera);
        dropdownStatusImage.draw(camera);
    }
}
