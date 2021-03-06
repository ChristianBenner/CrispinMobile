package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Rotation2D;
import com.games.crispin.crispinmobile.Geometry.Rotation3D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;

public class Model extends RenderObject
{
    // The model matrix
    private ModelMatrix modelMatrix;

    // Position of the object
    private Point3D position;

    // Rotation of the object
    private Rotation3D rotation;

    // Scale of the object
    private Scale3D scale;

    // Rotation around a point
    private Point3D rotationPoint;

    // Point rotation
    private Rotation3D rotationPointAngle;

    public Model(float[] positionBuffer,
                 float[] texelBuffer,
                 float[] colourBuffer,
                 float[] normalBuffer,
                 RenderMethod renderMethod,
                 int numVerticesPerGroup,
                 byte elementsPerPosition,
                 byte elementsPerTexel,
                 byte elementsPerColour,
                 byte elementsPerNormal,
                 Material material)
    {
        super(positionBuffer, texelBuffer, colourBuffer, normalBuffer, renderMethod,
                numVerticesPerGroup, elementsPerPosition, elementsPerTexel, elementsPerColour,
                elementsPerNormal, material);

        // Initialise the class variables
        modelMatrix = new ModelMatrix();
        position = new Point3D();
        rotation = new Rotation3D();
        scale = new Scale3D();
        rotationPoint = new Point3D();
        rotationPointAngle = new Rotation3D();
    }

    public Model(float[] positionBuffer,
                 float[] texelBuffer,
                 float[] colourBuffer,
                 float[] normalBuffer,
                 RenderMethod renderMethod,
                 int numVerticesPerGroup,
                 byte elementsPerPosition,
                 byte elementsPerTexel,
                 byte elementsPerColour,
                 byte elementsPerNormal)
    {
        this(positionBuffer, texelBuffer, colourBuffer, normalBuffer, renderMethod,
                numVerticesPerGroup, elementsPerPosition, elementsPerTexel, elementsPerColour,
                elementsPerNormal, new Material());
    }

    public Model(float[] vertexData,
                 RenderMethod renderMethod,
                 AttributeOrder_t attributeOrder,
                 int numVerticesPerGroup,
                 byte elementsPerPosition,
                 byte elementsPerTexel,
                 byte elementsPerColour,
                 byte elementsPerNormal,
                 Material material)
    {
        super(vertexData, renderMethod, attributeOrder, numVerticesPerGroup, elementsPerPosition,
                elementsPerTexel, elementsPerColour, elementsPerNormal, material);

        // Initialise the class variables
        modelMatrix = new ModelMatrix();
        position = new Point3D();
        rotation = new Rotation3D();
        scale = new Scale3D();
        rotationPoint = new Point3D();
        rotationPointAngle = new Rotation3D();
    }

    public Model(float[] vertexData, RenderMethod renderMethod, AttributeOrder_t attributeOrder,
                 int numVerticesPerGroup, byte elementsPerPosition, byte elementsPerTexel,
                 byte elementsPerColour, byte elementsPerNormal)
    {
        this(vertexData, renderMethod, attributeOrder, numVerticesPerGroup, elementsPerPosition,
                elementsPerTexel, elementsPerColour, elementsPerNormal, new Material());
    }

    /**
     * Set the position
     *
     * @param position  The new position
     * @since 1.0
     */
    public void setPosition(Point3D position)
    {
        setPosition(position.x, position.y, position.z);
    }

    /**
     * Set the position
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @param z The new z-coordinate
     * @since 1.0
     */
    public void setPosition(float x,
                            float y,
                            float z)
    {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    /**
     * Set the position
     *
     * @param position  The new position
     * @since 1.0
     */
    public void setPosition(Point2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    /**
     * Set the position
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     * @since 1.0
     */
    public void setPosition(float x, float y)
    {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Get the position
     *
     * @return The position of the object
     * @since 1.0
     */
    public Point3D getPosition()
    {
        return position;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale3D scale)
    {
        setScale(scale.x, scale.y, scale.z);
    }

    /**
     * Set the scale
     *
     * @param w Multiplier for the x-axis
     * @param h Multiplier for the y-axis
     * @param l Multiplier for the z-axis
     * @since 1.0
     */
    public void setScale(float w,
                         float h,
                         float l)
    {
        this.scale.x = w;
        this.scale.y = h;
        this.scale.z = l;
    }

    /**
     * Set the scale
     *
     * @param scale The scale to apply to the object
     * @since 1.0
     */
    public void setScale(Scale2D scale)
    {
        setScale(scale.x, scale.y);
    }

    /**
     * Set the scale
     *
     * @param w Multiplier for the x-axis
     * @param h Multiplier for the y-axis
     * @since 1.0
     */
    public void setScale(float w, float h)
    {
        this.scale.x = w;
        this.scale.y = h;
    }

    /**
     * Set x-axis scale multiplier
     *
     * @param x Multiplier for the x-axis
     * @since 1.0
     */
    public void setScaleX(float x)
    {
        this.scale.x = x;
    }

    /**
     * Set y-axis scale multiplier
     *
     * @param y Multiplier for the y-axis
     * @since 1.0
     */
    public void setScaleY(float y)
    {
        this.scale.y = y;
    }

    /**
     * Set z-axis scale multiplier
     *
     * @param z Multiplier for the z-axis
     * @since 1.0
     */
    public void setScaleZ(float z)
    {
        this.scale.z = z;
    }

    /**
     * Get the scale
     *
     * @return The scale multiplier of the object
     * @since 1.0
     */
    public Scale3D getScale()
    {
        return this.scale;
    }

    /**
     * Set the rotation
     *
     * @param rotation  The new rotation
     * @since 1.0
     */
    public void setRotation(Rotation3D rotation)
    {
        setRotation(rotation.angle, rotation.x, rotation.y, rotation.z);
    }

    /**
     * Set the rotation
     *
     * @param angle The new rotation angle
     * @param x The x axis multiplier
     * @param y The y axis multiplier
     * @param z The z axis multiplier
     * @since 1.0
     */
    public void setRotation(float angle,
                            float x,
                            float y,
                            float z)
    {
        this.rotation.angle = angle;
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    /**
     * Set the rotation
     *
     * @param rotation  The new rotation
     * @since 1.0
     */
    public void setRotation(Rotation2D rotation)
    {
        setRotation(rotation.angle, rotation.x, rotation.y);
    }

    /**
     * Set the rotation
     *
     * @param angle The new rotation angle
     * @param x The x axis multiplier
     * @param y The y axis multiplier
     * @since 1.0
     */
    public void setRotation(float angle,
                            float x,
                            float y)
    {
        this.rotation.angle = angle;
        this.rotation.x = x;
        this.rotation.y = y;
    }

    /**
     * Set the rotation around point
     *
     * @param point The point to rotate around
     * @param rotation  The rotation to make
     * @since 1.0
     */
    public void setRotationAroundPoint(Point3D point,
                                       Rotation3D rotation)
    {
        setRotationAroundPoint(point.x, point.y, point.z, rotation.angle, rotation.x, rotation.y,
                rotation.z);
    }

    /**
     * Set the rotation around point
     *
     * @param point The point to rotate around
     * @param angle The angle to rotate
     * @param rotationX The x axis multiplier
     * @param rotationY The y axis multiplier
     * @param rotationZ The z axis multiplier
     * @since 1.0
     */
    public void setRotationAroundPoint(Point3D point,
                                       float angle,
                                       float rotationX,
                                       float rotationY,
                                       float rotationZ)
    {
        setRotationAroundPoint(point.x, point.y, point.z, angle, rotationX, rotationY, rotationZ);
    }

    /**
     * Set the rotation around point
     *
     * @param x The x position to rotate around
     * @param y The x position to rotate around
     * @param z The x position to rotate around
     * @param rotation  The rotation to make
     * @since 1.0
     */
    public void setRotationAroundPoint(float x,
                                       float y,
                                       float z,
                                       Rotation3D rotation)
    {
        setRotationAroundPoint(x, y, z, rotation.angle, rotation.x, rotation.y, rotation.z);
    }

    /**
     * Set the rotation around point
     *
     * @param x The x position to rotate around
     * @param y The x position to rotate around
     * @param z The x position to rotate around
     * @param angle The angle to rotate
     * @param rotationX The x axis multiplier
     * @param rotationY The y axis multiplier
     * @param rotationZ The z axis multiplier
     * @since 1.0
     */
    public void setRotationAroundPoint(float x,
                                       float y,
                                       float z,
                                       float angle,
                                       float rotationX,
                                       float rotationY,
                                       float rotationZ)
    {
        this.rotationPoint.x = x;
        this.rotationPoint.y = y;
        this.rotationPoint.z = z;
        this.rotationPointAngle.angle = angle;
        this.rotationPointAngle.x = rotationX;
        this.rotationPointAngle.y = rotationY;
        this.rotationPointAngle.z = rotationZ;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Point3D point)
    {
        this.position.translate(point);
    }

    /**
     * Translate the render objects position
     *
     * @param x The x-coordinate to translate by
     * @param y The y-coordinate to translate by
     * @param z The z-coordinate to translate by
     * @since 1.0
     */
    public void translate(float x,
                          float y,
                          float z)
    {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    /**
     * Translate the render objects position
     *
     * @param point The point to translate by
     * @since 1.0
     */
    public void translate(Point2D point)
    {
        this.position.translate(point);
    }

    /**
     * Translate the render objects position
     *
     * @param x The x-coordinate to translate by
     * @param y The y-coordinate to translate by
     * @since 1.0
     */
    public void translate(float x, float y)
    {
        this.position.x += x;
        this.position.y += y;
    }

    /**
     * Update the model matrix. This can update the model matrix if a transformation property of the
     * object such as a position, scale or rotation has changed.
     *
     * @since 1.0
     */
    protected void updateModelMatrix()
    {
        modelMatrix.reset();
        modelMatrix.translate(position);

        if(rotation.angle != 0.0f)
        {
            modelMatrix.rotate(rotation);
        }

        if(rotationPointAngle.angle != 0.0f)
        {
            modelMatrix.rotateAroundPoint(rotationPoint, rotationPointAngle);
        }

        modelMatrix.scale(scale);
    }

    public void render(Camera2D camera2D)
    {
        updateModelMatrix();
        super.render(camera2D, modelMatrix);
    }

    public void render(Camera3D camera3D)
    {
        updateModelMatrix();
        super.render(camera3D, modelMatrix);
    }
}
