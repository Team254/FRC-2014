package edu.missdaisy.utilities;

/**
 *
 * @author jrussell
 */
public class MovingAverageFilter implements Filter
{
    private double[] vals;
    private int ptr = 0;
    private double average = 0.0;

    public MovingAverageFilter(int nSamples)
    {
        vals = new double[nSamples];
    }

    public void reset()
    {
        for( int i = 0; i < vals.length; i++ )
        {
            vals[i]=0.0;
        }
        ptr = 0;
        average = 0.0;
    }

    public void setInput(double val)
    {
        vals[ptr] = val;
        ++ptr;

        if( ptr >= vals.length )
        {
            ptr = 0;
        }
    }

    public double getAverage()
    {
        return average;
    }

    public double run()
    {
        average = 0.0;
        for( int i = 0; i < vals.length; i++ )
        {
            average += vals[i];
        }
        average /= (double)vals.length;
        return average;
    }

}
