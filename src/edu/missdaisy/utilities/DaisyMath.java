package edu.missdaisy.utilities;

import com.sun.squawk.util.MathUtils;
/**
 *
 * @author jrussell
 */
public class DaisyMath
{
    
    /**
     * Get the Euclidean distance between two points
     *
     * @param x1 X coordinate of point 1
     * @param y1 Y coordinate of point 1
     * @param x2 X coordinate of point 2
     * @param y2 Y coordinate of point 2
     * @return The distance in the same units as the inputs
     */
    public static double getDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    /**
     * Get the angle between two points
     *
     * @param x1 X coordinate of point 1
     * @param y1 Y coordinate of point 1
     * @param x2 X coordinate of point 2
     * @param y2 Y coordinate of point 2
     * @return The angle in radians using the standard coordinate frame
     */
    public static double getBearingRadians(double x1, double y1, double x2, double y2)
    {
        return MathUtils.atan2(y2-y1, x2-x1);
    }

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
    
    public static double applyDeadband(double value, double deadband)
    {
        if( value > -deadband && value < deadband ) 
        {
            return 0.0;
        }
        else
        {
            return value;
        }
    }
}