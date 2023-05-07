package com.crispin.crispinmobile.Geometry;

import android.graphics.Point;
import android.opengl.Matrix;

import com.crispin.crispinmobile.Rendering.Utilities.Camera;

import java.util.Vector;

/**
 * Geometry class provides some public static functions for useful calculations and operations based
 * on vectors and vectors.
 *
 * @author Christian Benner
 * @version %I%, %G%
 * @see Plane
 * @see Ray
 * @see Sphere
 * @see Vec2
 * @see Vec3
 * @since 1.0
 */
public class Geometry {
    // 2 dimensional direction values
    public enum Direction2D {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    /**
     * Invert a Vec2 object. This flips the x and y co-ordinate polarity
     *
     * @param vector2D The vector to invert
     * @return Inverted Vector
     * @since 1.0
     */
    public static Vec2 invert(Vec2 vector2D) {
        return new Vec2(-vector2D.x, -vector2D.y);
    }

    /**
     * Get the cross product of two vectors
     *
     * @param lhs Left hand side vector
     * @param rhs Right hand side vector
     * @return The cross product direction
     * @since 1.0
     */
    public static Vec3 crossProduct(Vec3 lhs, Vec3 rhs) {
        return new Vec3((lhs.y * rhs.z) - (lhs.z * rhs.y), (lhs.z * rhs.x) - (lhs.x * rhs.z),
                (lhs.x * rhs.y) - (lhs.y * rhs.x));
    }

    /**
     * Invert a Vec3 object. This flips the x, y and z co-ordinate polarity
     *
     * @param vector3D The vector to invert
     * @return Inverted Vector
     * @since 1.0
     */
    public static Vec3 invert(Vec3 vector3D) {
        return new Vec3(-vector3D.x, -vector3D.y, -vector3D.z);
    }

    /**
     * Get the vector between two vectors.
     *
     * @param from The first 3D vector
     * @param to   The second 3D vector
     * @return A vector with a direction from the first vector to the second.
     * @since 1.0
     */
    public static Vec3 getVectorBetween(Vec3 from, Vec3 to) {
        return new Vec3(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    /**
     * Get the vector between two 2D vectors.
     *
     * @param from The first 2D vector
     * @param to   The second 2D vector
     * @return A vector with a direction from the first vector to the second.
     * @since 1.0
     */
    public static Vec2 getVectorBetween(Vec2 from, Vec2 to) {
        return new Vec2(to.x - from.x, to.y - from.y);
    }

    /**
     * Get the ray between two vectors
     *
     * @param from The first 3D vector (and also the origin of the ray)
     * @param to   The second 3D vector
     * @return A ray originating from the first vector that is facing the second vector
     * @since 1.0
     */
    public static Ray getRayBetween(Vec3 from, Vec3 to) {
        return new Ray(from, getVectorBetween(from, to));
    }

    /**
     * Check if a ray intersects a sphere. The method checks if the distance between a ray and a
     * spheres center vector is less than the spheres radius.
     *
     * @param sphere The virtual sphere
     * @param ray    The virtual ray
     * @return True if the ray intersects the sphere, else false
     * @since 1.0
     */
    public static boolean intersects(Sphere sphere, Ray ray) {
        return getDistanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * Get the distance between a vector and a ray
     *
     * @param vector3D The 3D vector
     * @param ray      The virtual ray
     * @return Distance between the given vector and ray as a float
     * @since 1.0
     */
    public static float getDistanceBetween(Vec3 vector3D, Ray ray) {
        Vec3 p1ToVector = getVectorBetween(ray.position, vector3D);
        Vec3 p2ToVector = getVectorBetween(translate(ray.position, ray.direction), vector3D);

        float areaOfTriangleTimesTwo = p1ToVector.crossProduct(p2ToVector).length();
        float lengthOfBase = ray.direction.length();

        // area of triangle = base * height so height = triangle / base
        float distanceFromVectorToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromVectorToRay;
    }

    /**
     * Get the distance between two vectors using pythagoras theorem
     *
     * @param first  The first vector
     * @param second The second vector
     * @return Distance between the two given vectors
     * @since 1.0
     */
    public static float getDistance(Vec2 first, Vec2 second) {
        float a = first.x - second.x;
        float b = first.y - second.y;

        // c^2 = a^2 + b^2
        return (float) Math.sqrt((a * a) + (b * b));
    }

    /**
     * Divide each component of the vector
     *
     * @param divisor
     * @since 1.0
     */
    public static Vec3 divide(Vec3 vector, float divisor) {
        return new Vec3(vector.x / divisor, vector.y / divisor, vector.z / divisor);
    }

    /**
     * Divide each component of the vector
     *
     * @param divisor
     * @since 1.0
     */
    public static Vec2 divide(Vec2 vector, float divisor) {
        return new Vec2(vector.x / divisor, vector.y / divisor);
    }

    /**
     * Translate a vector by a vector
     *
     * @param vector3D The vector to translate
     * @param vector   The vector to translate the vector by
     * @return The translated vector
     * @since 1.0
     */
    public static Vec3 translate(Vec3 vector3D, Vec3 vector) {
        return new Vec3(vector3D.x + vector.x, vector3D.y + vector.y,
                vector3D.z + vector.z);
    }

    /**
     * Translate a vector by floats
     *
     * @param vector3D The vector to translate
     * @param x        Amount to translate the x co-ordinate
     * @param y        Amount to translate the y co-ordinate
     * @param z        Amount to translate the z co-ordinate
     * @return The translated vector
     * @since 1.0
     */
    public static Vec3 translate(Vec3 vector3D, float x, float y, float z) {
        return new Vec3(vector3D.x + x, vector3D.y + y, vector3D.z + z);
    }

    /**
     * Translate a vector by floats
     *
     * @param vector2D The vector to translate
     * @param x        Amount to translate the x co-ordinate
     * @param y        Amount to translate the y co-ordinate
     * @return The translated vector
     * @since 1.0
     */
    public static Vec2 translate(Vec2 vector2D, float x, float y) {
        return new Vec2(vector2D.x + x, vector2D.y + y);
    }

    /**
     * Translate a vector by another vector
     *
     * @param vectorOne The first vector
     * @param vectorTwo The second vector
     * @return The translated vector
     * @since 1.0
     */
    public static Vec2 translate(Vec2 vectorOne, Vec2 vectorTwo) {
        return new Vec2(vectorOne.x + vectorTwo.x, vectorOne.y + vectorTwo.y);
    }

    /**
     * Scale a given direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x, y and z)
     * @return The scaled vector
     * @since 1.0
     */
    public static Vec3 scaleVector(Vec3 vector, float scale) {
        return new Vec3(vector.x * scale, vector.y * scale, vector.z * scale);
    }

    /**
     * Scale each dimension of a given direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @param z Scale multiplier for the z dimension
     * @return The scaled vector
     * @since 1.0
     */
    public static Vec3 scaleVector(Vec3 vector, float x, float y, float z) {
        return new Vec3(vector.x * x, vector.y * y, vector.z * z);
    }

    /**
     * Scale a given direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x and y)
     * @return The scaled vector
     * @since 1.0
     */
    public static Vec2 scaleVector(Vec2 vector, float scale) {
        return new Vec2(vector.x * scale, vector.y * scale);
    }

    /**
     * Scale each dimension of a given direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @return The scaled vector
     * @since 1.0
     */
    public static Vec2 scaleVector(Vec2 vector, float x, float y) {
        return new Vec2(vector.x * x, vector.y * y);
    }

    /**
     * Get the intersection vector between a ray and a plane
     *
     * @param ray The virtual ray
     * @param ray The virtual plane
     * @return The vector in which the ray intersects the plane
     * @since 1.0
     */
    public static Vec3 getIntersectionVector(Ray ray, Plane plane) {
        Vec3 rayToPlaneVector = getVectorBetween(ray.position, plane.position);

        float scaleFactor = rayToPlaneVector.dotProduct(plane.direction)
                / ray.direction.dotProduct(plane.direction);

        Vec3 intersectionVec3 = translate(ray.position, scaleVector(ray.direction, scaleFactor));
        return intersectionVec3;
    }

    /**
     * Subtract two 3D vectors (lhs - rhs)
     *
     * @param lhs Left hand side vector (being subtracted from)
     * @param rhs Right hand side vector
     * @return The subtracted vector (lhs - rhs)
     * @since 1.0
     */
    public static Vec3 minus(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
    }

    /**
     * Add two 3D vectors
     *
     * @param lhs Vector to add
     * @param rhs Vector to add
     * @since 1.0
     */
    public static Vec3 plus(Vec3 lhs, Vec3 rhs) {
        return new Vec3(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
    }

    /**
     * Subtract two 2D vectors (lhs - rhs)
     *
     * @param lhs Left hand side vector (being subtracted from)
     * @param rhs Right hand side vector
     * @return The subtracted vector (lhs - rhs)
     * @since 1.0
     */
    public static Vec2 minus(Vec2 lhs, Vec2 rhs) {
        return new Vec2(lhs.x - rhs.x, lhs.y - rhs.y);
    }

    /**
     * Add two 2D vectors
     *
     * @param lhs Vector to add
     * @param rhs Vector to add
     * @since 1.0
     */
    public static Vec2 plus(Vec2 lhs, Vec2 rhs) {
        return new Vec2(lhs.x + rhs.x, lhs.y + rhs.y);
    }

    /**
     * Normalize a 3D vector. Returns the vector / length
     *
     * @param v Vec3
     * @return Normalized vector. Vector / Length
     * @since 1.0
     */
    public static Vec3 normalize(Vec3 v) {
        final float length = (float) Math.sqrt(Math.abs((v.x * v.x) + (v.y * v.y) + (v.z + v.z)));
        return new Vec3(v.x / length, v.y / length, v.z / length);
    }

    /**
     * Normalize a 2D vector. Returns the vector / length
     *
     * @param v Vec2
     * @return Normalized vector. Vector / Length
     * @since 1.0
     */
    public static Vec2 normalize(Vec2 v) {
        final float length = (float) Math.sqrt(Math.abs((v.x * v.x) + (v.y * v.y)));
        return new Vec2(v.x / length, v.y / length);
    }

    /**
     * Get a direction from a vector
     *
     * @param v Vec2
     * @return Direction2D of the vector
     * @since 1.0
     */
    public static Direction2D getDirection(Vec2 v) {
        Vec2 norm = normalize(v);

        // See what component is larger (x or y) and return it
        if(Math.abs(norm.x) > Math.abs(norm.y)) {
            if(v.x > 0f) {
                return Direction2D.RIGHT;
            }
            return Direction2D.LEFT;
        } else if(Math.abs(norm.y) > Math.abs(norm.x)) {
            if(v.y > 0f) {
                return Direction2D.UP;
            }
            return Direction2D.DOWN;
        } else {
            return Direction2D.NONE;
        }
    }
}