package edu.missdaisy.utilities;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * This class encapsulates the functionality of an MA3 analog-output absolute encoder.
 *
 * @author jrussell
 */
public final class MA3Encoder extends AnalogChannel
{   
    private double mOffset = 0.;
    private int mRevolutions = 0;
    private double mCurrentVoltage = -1.0;
    private double mLastVoltage = -1.0;
    private double mScalingFactor = 1;

    public MA3Encoder(int channel)
    {
        super(channel);
    }

    public MA3Encoder(int slot, int channel)
    {
        super(slot, channel);
    }

    public synchronized void setVoltageOffset( double aOffset )
    {
        mOffset = aOffset;
        mCurrentVoltage = mLastVoltage = getVoltage();
    }

    public synchronized double getVoltageOffset()
    {
        return mOffset;
    }
    
    public synchronized void zero()
    {
        setVoltageOffset(getAverageVoltage());
        mRevolutions = 0;
        mCurrentVoltage = mLastVoltage = getVoltage();
    }

    /***
     * Get the angle of the sensor.
     *
     * @return the current angle (in degrees, 0-360)
     */
    public synchronized double getRawAngle()
    {
        mCurrentVoltage = getVoltage();
        double lAngle = (mCurrentVoltage)*360.0/5.0;
        DaisyMath.boundAngle0to360Degrees(lAngle);

        return lAngle;
    }

    public double getContinuousAngle()
    {
        double lAngle = getRawAngle();
        
        if( mLastVoltage >= 0.0 )
        {
            if( Math.abs(mCurrentVoltage-mLastVoltage) > 2.5 )
            {
                if( mCurrentVoltage > mLastVoltage )
                {
                    mRevolutions--;
                }
                else
                {
                    mRevolutions++;
                }
            }
        }
        mLastVoltage = mCurrentVoltage;
        return lAngle - mOffset*360.0/5.0 + 360.0 * (double)mRevolutions;
    }
    
    public synchronized void setScalingFactor(double aUnitsPerDegree)
    {
        mScalingFactor = aUnitsPerDegree;
    }

    public synchronized double getScaledAngle()
    {
        return getContinuousAngle()*mScalingFactor;
    }
    
    public synchronized double getScaledRawAngle()
    {
        return (getRawAngle()- mOffset*360.0/5.0)*mScalingFactor;
    }
    
    public int getNumRevolutions()
    {
        return this.mRevolutions;
    }

}