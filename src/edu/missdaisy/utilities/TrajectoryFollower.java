package edu.missdaisy.utilities;

import edu.missdaisy.utilities.Trajectory.Segment;

/**
 *
 * @author Jared
 */
public class TrajectoryFollower 
{
    private double mKp;
    private double mKi;
    private double mKd;
    private double mKv;
    private double mKa;
    private double mLastError;
    private double mErrorSum;
    private double mDistance;
    private int mCurrentSegment;
    private Trajectory mProfile;
    
    public TrajectoryFollower()
    {
        
    }
    
    public void configure(double kp, double ki, double kd, double kv, double ka)
    {
        mKp = kp;
        mKi = ki;
        mKd = kd;
        mKv = kv;
        mKa = ka;
    }
    
    public void reset()
    {
        mLastError = 0.0;
        mCurrentSegment = 0;
        mErrorSum = 0.0;
        mDistance = 0.0;
    }
    
    public void setTrajectory(Trajectory profile)
    {
        mProfile = profile;
    }
    
    public double calculate(double distance)
    {
        //System.out.println(mCurrentSegment + " " + mProfile.getNumSegments());
        mDistance = distance;
        if( mCurrentSegment < mProfile.getNumSegments() )
        {
            Segment segment = mProfile.getSegment(mCurrentSegment);
            double error = segment.position - distance;

            double output = mKp*error + mKd*((error - mLastError)/mProfile.getDt() - segment.velocity) + mKv*segment.velocity + mKa*segment.acceleration;

            mLastError = error;

            mCurrentSegment++;
            
            return output;
        }
        else
        {
            //System.out.println("Switching to PID");
            double error = mProfile.getGoalDistance() - distance;
            mErrorSum += error;
            
            return mKp*error + mKi*mErrorSum;
        }        
    }
    
    public boolean isFinishedTrajectory()
    {
        if( mCurrentSegment >= mProfile.getNumSegments() )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public boolean onTarget(double threshold)
    {
        if( Math.abs(mDistance - mProfile.getGoalDistance()) < threshold )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
