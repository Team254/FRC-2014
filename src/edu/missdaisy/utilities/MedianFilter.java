package edu.missdaisy.utilities;

import com.sun.squawk.util.Arrays;

/**
 *
 * @author jrussell
 */
public class MedianFilter implements Filter
{
    private double[] vals;
    private int ptr = 0;
    private double median = 0.0;

    public MedianFilter(int nSamples)
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
        median = 0.0;
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

    public double getMedian()
    {
        return median;
    }

    public double run()
    {
        median = 0.0;
        
        double[] sorted = Arrays.copy(vals, 0, vals.length, 0, vals.length);
        Arrays.sort(sorted);

        median = sorted[vals.length/2];
        
        return median;
    }

}
