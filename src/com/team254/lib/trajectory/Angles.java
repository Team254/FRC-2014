package com.team254.lib.trajectory;

/**
 * Math utilities for dealing with angles.
 * 
 * @author Jared341
 */
public class Angles
{
    /**
     * Get the difference in angle between two angles.
     *
     * @param from The first angle
     * @param to The second angle
     * @return The change in angle from the first argument necessary to line up
     * with the second.  Always between -Pi and Pi
     */
    public static double getDifferenceInAngleRadians(double from, double to)
    {
        return boundAngleNegPiToPiRadians(to-from);
    }

    /**
     * Get the difference in angle between two angles.
     *
     * @param from The first angle
     * @param to The second angle
     * @return The change in angle from the first argument necessary to line up
     * with the second.  Always between -180 and 180
     */
    public static double getDifferenceInAngleDegrees(double from, double to)
    {
        return boundAngleNeg180to180Degrees(to-from);
    }

    public static double boundAngle0to360Degrees(double angle)
    {
        // Naive algorithm
        while(angle >= 360.0)
        {
            angle -= 360.0;
        }
        while(angle < 0.0)
        {
            angle += 360.0;
        }
        return angle;
    }

    public static double boundAngleNeg180to180Degrees(double angle)
    {
        // Naive algorithm
        while(angle >= 180.0)
        {
            angle -= 360.0;
        }
        while(angle < -180.0)
        {
            angle += 360.0;
        }
        return angle;
    }

    public static double boundAngle0to2PiRadians(double angle)
    {
        // Naive algorithm
        while(angle >= 2.0*Math.PI)
        {
            angle -= 2.0*Math.PI;
        }
        while(angle < 0.0)
        {
            angle += 2.0*Math.PI;
        }
        return angle;
    }

    public static double boundAngleNegPiToPiRadians(double angle)
    {
        // Naive algorithm
        while(angle >= Math.PI)
        {
            angle -= 2.0*Math.PI;
        }
        while(angle < Math.PI)
        {
            angle += 2.0*Math.PI;
        }
        return angle;
    }
}