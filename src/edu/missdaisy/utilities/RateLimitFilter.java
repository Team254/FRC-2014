package edu.missdaisy.utilities;

/**
 *
 * @author jrussell
 */
public class RateLimitFilter implements Filter
{
    private double maxRate = 1.0; // Per LOOP
    private double lastVal = 0.0;
    private double desired = 0.0;

    public RateLimitFilter(double maxRate)
    {
        this.maxRate = maxRate;
    }

    public void setMaxRate(double rate)
    {
        maxRate = rate;
    }

    public double getMaxRate()
    {
        return maxRate;
    }

    public void reset()
    {
        lastVal = 0.0;
        desired = 0.0;
    }

    public void setDesired(double desired)
    {
        this.desired = desired;
    }

    public double run()
    {
        double difference = desired - lastVal;
        if( difference > maxRate )
        {
            lastVal += maxRate;
        }
        else if( difference < -maxRate )
        {
            lastVal -= maxRate;
        }
        else
        {
            lastVal = desired;
        }
        return lastVal;
    }

}
