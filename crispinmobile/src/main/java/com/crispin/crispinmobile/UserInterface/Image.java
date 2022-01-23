package com.crispin.crispinmobile.UserInterface;

import com.crispin.crispinmobile.Geometry.Scale2D;
import com.crispin.crispinmobile.Geometry.Vec2;
import com.crispin.crispinmobile.Rendering.Utilities.Texture;

public class Image extends Plane
{
    public Image(Texture texture,
                 int uiWidth,
                 int uiHeight)
    {
        super(new Vec2(), new Scale2D());
        super.setImage(texture);
        super.setWidth(uiWidth);
        super.setHeight(uiHeight);
    }

    public Image(Texture texture)
    {
        super(new Vec2(), new Scale2D());
        super.setImage(texture);
        super.setWidth(super.plane.getMaterial().getTexture().getWidth());
        super.setHeight(super.plane.getMaterial().getTexture().getWidth());
    }

    public Image(int resourceId,
                 int uiWidth,
                 int uiHeight)
    {
        super(new Vec2(), new Scale2D());
        super.setImage(resourceId);
        super.setWidth(uiWidth);
        super.setHeight(uiHeight);
    }

    public Image(int resourceId)
    {
        super(new Vec2(), new Scale2D());
        super.setImage(resourceId);
        super.setWidth(super.plane.getMaterial().getTexture().getWidth());
        super.setHeight(super.plane.getMaterial().getTexture().getWidth());
    }
}
