package com.games.crispin.crispinmobile.Rendering.Utilities;

import android.opengl.Matrix;

import com.games.crispin.crispinmobile.Geometry.Geometry;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Geometry.Scale3D;

public class ModelMatrix
{
    private float[] modelMatrix;

    public ModelMatrix()
    {
        modelMatrix = new float[16];
        reset();
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

    public void translate(Point3D point3D)
    {
        Matrix.translateM(modelMatrix, 0, point3D.x, point3D.y, point3D.z);
    }

    public void rotateAroundPoint(Point3D point3D, float angle, float x, float y, float z)
    {
        translate(point3D);
        rotate(angle, x, y, z);
        translate(Geometry.invert(point3D));
    }

    public void scale(Scale3D scale3D)
    {
        Matrix.scaleM(modelMatrix, 0, scale3D.x, scale3D.y, scale3D.z);
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
