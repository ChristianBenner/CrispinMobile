package com.crispin.crispinmobile.Geometry;

import glm_.vec2.Vec2;
import glm_.vec3.Vec3;

/**
 * Geometry class provides some public static functions for useful calculations and operations based
 * on vectors and points.
 *
 * @author  Christian Benner
 * @version %I%, %G%
 * @see     Plane
 * @see     Ray
 * @see     Sphere
 * @see     Vec2
 * @see     Vec3
 * @since   1.0
 */
public class Geometry
{
    /**
     * Invert a Vec2 object. This flips the x and y co-ordinate polarity
     *
     * @param point2D   The point to invert
     * @return  Inverted Point
     * @since   1.0
     */
    public static Vec2 invert(Vec2 point2D)
    {
        return new Vec2(-point2D.x, -point2D.y);
    }

    /**
     * Invert a Vec3 object. This flips the x, y and z co-ordinate polarity
     *
     * @param point3D   The point to invert
     * @return  Inverted Point
     * @since   1.0
     */
    public static Vec3 invert(Vec3 point3D)
    {
        return new Vec3(-point3D.x, -point3D.y, -point3D.z);
    }

    /**
     * Get the vector between two points.
     *
     * @param from  The first 3D point
     * @param to    The second 3D point
     * @return      A vector with a direction from the first point to the second.
     * @since 1.0
     */
    public static Vec3 getVectorBetween(Vec3 from, Vec3 to)
    {
        return new Vec3(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }

    /**
     * Get the vector between two 2D points.
     *
     * @param from  The first 2D point
     * @param to    The second 2D point
     * @return      A vector with a direction from the first point to the second.
     * @since 1.0
     */
    public static Vec2 getVectorBetween(Vec2 from, Vec2 to)
    {
        return new Vec2(
                to.x - from.x,
                to.y - from.y);
    }

    /**
     * Get the ray between two points
     *
     * @param from  The first 3D point (and also the origin of the ray)
     * @param to    The second 3D point
     * @return      A ray originating from the first point that is facing the second point
     * @since 1.0
     */
    public static Ray getRayBetween(Vec3 from, Vec3 to)
    {
        return new Ray(from,
                getVectorBetween(from, to));
    }

    /**
     * Check if a ray intersects a sphere. The method checks if the distance between a ray and a
     * spheres center point is less than the spheres radius.
     *
     * @param sphere    The virtual sphere
     * @param ray       The virtual ray
     * @return          True if the ray intersects the sphere, else false
     * @since 1.0
     */
    public static boolean intersects(Sphere sphere, Ray ray)
    {
        return getDistanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * Get the distance between a point and a ray
     *
     * @param point3D   The 3D point
     * @param ray       The virtual ray
     * @return          Distance between the given point and ray as a float
     * @since 1.0
     */
    public static float getDistanceBetween(Vec3 point3D, Ray ray)
    {
        Vec3 p1ToPoint = getVectorBetween(ray.position, point3D);
        Vec3 p2ToPoint = getVectorBetween(translate(ray.position, ray.direction), point3D);

        float areaOfTriangleTimesTwo = p1ToPoint.cross(p2ToPoint).length();
        float lengthOfBase = ray.direction.length();

        // area of triangle = base * height so height = triangle / base
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
        return distanceFromPointToRay;
    }

    /**
     * Get the distance between two points using pythagoras theorem
     *
     * @param first     The first point
     * @param second    The second point
     * @return          Distance between the two given points
     * @since 1.0
     */
    public static float getDistance(Vec2 first, Vec2 second)
    {
        float a = first.x - second.x;
        float b = first.y - second.y;

        // c^2 = a^2 + b^2
        return (float)Math.sqrt((a * a) + (b * b));
    }

    /**
     * Translate a point by a vector
     *
     * @param point3D   The point to translate
     * @param vector    The vector to translate the point by
     * @return          The translated point
     * @since 1.0
     */
    public static Vec3 translate(Vec3 point3D, Vec3 vector)
    {
        return new Vec3(
                point3D.x + vector.x,
                point3D.y + vector.y,
                point3D.z + vector.z);
    }

    /**
     * Translate a point by floats
     *
     * @param point3D   The point to translate
     * @param x         Amount to translate the x co-ordinate
     * @param y         Amount to translate the y co-ordinate
     * @param z         Amount to translate the z co-ordinate
     * @return          The translated point
     * @since 1.0
     */
    public static Vec3 translate(Vec3 point3D,
                                    float x,
                                    float y,
                                    float z)
    {
        return new Vec3(point3D.x + x, point3D.y + y, point3D.z + z);
    }

    /**
     * Translate a 2D point by a vector
     *
     * @param point2D   The point to translate
     * @param vector    The vector to translate the point by
     * @return          The translated point
     * @since 1.0
     */
    public static Vec2 translate(Vec2 point2D, Vec2 vector)
    {
        return new Vec2(point2D.x + vector.x, point2D.y + vector.y);
    }

    /**
     * Translate a point by floats
     *
     * @param point2D   The point to translate
     * @param x         Amount to translate the x co-ordinate
     * @param y         Amount to translate the y co-ordinate
     * @return          The translated point
     * @since 1.0
     */
    public static Vec2 translate(Vec2 point2D,
                                    float x,
                                    float y)
    {
        return new Vec2(point2D.x + x, point2D.y + y);
    }

    /**
     * Scale a given direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x, y and z)
     * @return      The scaled vector
     * @since 1.0
     */
    public static Vec3 scaleVector(Vec3 vector, float scale)
    {
        return new Vec3(
                vector.x * scale,
                vector.y * scale,
                vector.z * scale);
    }

    /**
     * Scale each dimension of a given direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @param z Scale multiplier for the z dimension
     * @return  The scaled vector
     * @since 1.0
     */
    public static Vec3 scaleVector(Vec3 vector,
                                float x,
                                float y,
                                float z)
    {
        return new Vec3(
                vector.x * x,
                vector.y * y,
                vector.z * z);
    }

    /**
     * Scale a given direction by a multiplier
     *
     * @param scale Scale multiplier (multiplies all dimensions x and y)
     * @return      The scaled vector
     * @since 1.0
     */
    public static Vec2 scaleVector(Vec2 vector, float scale)
    {
        return new Vec2(
                vector.x * scale,
                vector.y * scale);
    }

    /**
     * Scale each dimension of a given direction by a specific multiplier
     *
     * @param x Scale multiplier for the x dimension
     * @param y Scale multiplier for the y dimension
     * @return  The scaled vector
     * @since 1.0
     */
    public static Vec2 scaleVector(Vec2 vector,
                                       float x,
                                       float y)
    {
        return new Vec2(
                vector.x * x,
                vector.y * y);
    }

    /**
     * Get the intersection point between a ray and a plane
     *
     * @param ray   The virtual ray
     * @param ray   The virtual plane
     * @return      The point in which the ray intersects the plane
     * @since 1.0
     */
    public static Vec3 getIntersectionPoint(Ray ray, Plane plane)
    {
        Vec3 rayToPlaneVector = getVectorBetween(ray.position, plane.position);

        float scaleFactor = rayToPlaneVector.dot(plane.direction)
                / ray.direction.dot(plane.direction);

        Vec3 intersectionVec3 = translate(ray.position,
                scaleVector(ray.direction, scaleFactor));
        return intersectionVec3;
    }
}