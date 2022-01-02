package com.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.crispin.crispinmobile.Geometry.Geometry;
import com.crispin.crispinmobile.Geometry.Point3D;
import com.crispin.crispinmobile.Geometry.Rotation2D;
import com.crispin.crispinmobile.Geometry.Rotation3D;
import com.crispin.crispinmobile.Geometry.Scale3D;

public class ModelMatrix
{
    private float[] modelMatrix;

    public ModelMatrix()
    {
        modelMatrix = new float[16];
        reset();
    }

    public ModelMatrix(float modelMatrix[])
    {
        this.modelMatrix = modelMatrix;
       // reset();
    }

    // transformations

    public void reset()
    {
        Matrix.setIdentityM(modelMatrix, 0);
    }

    public void rotate(float angle, float x, float y, float z)
    {
        Matrix.rotateM(modelMatrix, 0, angle, x, y, z);
    }

    public void rotate(Rotation3D rotation3D)
    {
        rotate(rotation3D.angle, rotation3D.x, rotation3D.y, rotation3D.z);
    }

    public void rotate(float angle, float x, float y)
    {
        Matrix.rotateM(modelMatrix, 0, angle, x, y, 0.0f);
    }

    public void rotate(Rotation2D rotation2D)
    {
        rotate(rotation2D.angle, rotation2D.x, rotation2D.y);
    }

    public void translate(Point3D point3D)
    {
        Matrix.translateM(modelMatrix, 0, point3D.x, point3D.y, point3D.z);
    }

    public void translate(float x, float y, float z)
    {
        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    public void rotateAroundPoint(float x, float y, float z, float angle, float rx, float ry,
                                  float rz)
    {
        translate(x, y, z);
        rotate(angle, rx, ry, rz);
        translate(-x, -y, -z);
    }

    public void rotateAroundPoint(Point3D point3D, float angle, float x, float y, float z)
    {
        rotateAroundPoint(point3D.x, point3D.y, point3D.z, angle, x, y, z);
    }

    public void rotateAroundPoint(Point3D point3D, Rotation3D rotation3D)
    {
        rotateAroundPoint(point3D, rotation3D.angle, rotation3D.x, rotation3D.y, rotation3D.z);
    }

    public void scale(Scale3D scale3D)
    {
        Matrix.scaleM(modelMatrix, 0, scale3D.x, scale3D.y, scale3D.z);
    }

    public void scale(float scale)
    {
        Matrix.scaleM(modelMatrix, 0, scale, scale, scale);
    }

    public float[] getModelMatrix()
    {
        return modelMatrix;
    }

    public Point3D getPosition()
    {
        // m00 m10 m20 m30 m01 m11 m21 m31 m02 m12 m22 m32 m03 m13 m23 m33
        return new Point3D(modelMatrix[12], modelMatrix[13], modelMatrix[14]);
    }

    // normal render call (no matrix in param)
    // - resetidentity
    // - transform
    // - render with model matrix


}
