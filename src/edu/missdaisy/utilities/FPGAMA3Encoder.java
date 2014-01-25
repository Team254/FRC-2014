package edu.missdaisy.utilities;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.AnalogTriggerOutput;
import edu.wpi.first.wpilibj.Counter;

/**
 * This class encapsulates the functionality of an MA3 analog-output absolute encoder.
 *
 * @author jrussell
 */
public final class FPGAMA3Encoder extends AnalogChannel
{   
    private AnalogTrigger mTrigger;
    private Counter mCounter;
    
    private double mOffset = 0.;
    private double mScalingFactor = 1;
    
    public synchronized void init()
    {
        mTrigger = new AnalogTrigger(this);
        mTrigger.setLimitsVoltage(1.67, 3.33);
        mCounter = new Counter(mTrigger);
        mCounter.setUpDownCounterMode();
        mCounter.setUpSource(mTrigger, AnalogTriggerOutput.Type.kRisingPulse);
        mCounter.setUpSourceEdge(true, false);
        mCounter.setMaxPeriod(10.0);
        mCounter.setDownSource(mTrigger, AnalogTriggerOutput.Type.kFallingPulse);
        mCounter.setDownSourceEdge(true, false);
        mCounter.reset();
        mCounter.start();
    }

    public FPGAMA3Encoder(int channel)
    {
        super(channel);
        //init();
    }

    public FPGAMA3Encoder(int slot, int channel)
    {
        super(slot, channel);
        //init();
    }

    public synchronized void setVoltageOffset( double aOffset )
    {
        mOffset = aOffset;
    }

    public synchronized double getVoltageOffset()
    {
        return mOffset;
    }
    
    public synchronized void zero()
    {
        setVoltageOffset(getAverageVoltage());
        mCounter.reset();
        mCounter.start();
    }

    /***
     * Get the angle of the sensor.
     *
     * @return the current angle (in degrees, 0-360)
     */
    public synchronized double getRawAngle()
    {
        double lAngle = (getVoltage()-mOffset)*360.0/5.0;
        DaisyMath.boundAngle0to360Degrees(lAngle);

        return lAngle;
    }

    public double getContinuousAngle()
    {
        double lAngle = getRawAngle();
        return lAngle + 360.0 * (double)mCounter.get();
    }
    
    public synchronized void setScalingFactor(double aUnitsPerDegree)
    {
        mScalingFactor = aUnitsPerDegree;
    }

    public synchronized double getScaledAngle()
    {
        return getContinuousAngle()*mScalingFactor;
    }

}