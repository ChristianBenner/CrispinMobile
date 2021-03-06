package com.games.crispin.crispinmobile.UserInterface;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;

public class Image extends Plane
{
    public Image(Texture texture,
                 int uiWidth,
                 int uiHeight)
    {
        super(new Point2D(), new Scale2D());
        super.setImage(texture);
        super.setWidth(uiWidth);
        super.setHeight(uiHeight);
    }

    public Image(Texture texture)
    {
        super(new Point2D(), new Scale2D());
        super.setImage(texture);
        super.setWidth(super.plane.getMaterial().getTexture().getWidth());
        super.setHeight(super.plane.getMaterial().getTexture().getWidth());
    }

    public Image(int resourceId,
                 int uiWidth,
                 int uiHeight)
    {
        super(new Point2D(), new Scale2D());
        super.setImage(resourceId);
        super.setWidth(uiWidth);
        super.setHeight(uiHeight);
    }

    public Image(int resourceId)
    {
        super(new Point2D(), new Scale2D());
        super.setImage(resourceId);
        super.setWidth(super.plane.getMaterial().getTexture().getWidth());
        super.setHeight(super.plane.getMaterial().getTexture().getWidth());
    }
}
