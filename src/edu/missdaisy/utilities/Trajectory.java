package edu.missdaisy.utilities;

/**
 *
 * @author Jared
 */
public class Trajectory 
{
    private Trajectory.Segment[] mSegments;
    private int mNumSegments;
    private double mDt;
    private double mGoalDistance;
    
    public class Segment
    {
        protected Segment()
        {
            
        }
        
        public double position;
        public double velocity;
        public double acceleration;
        public double jerk;
        protected double f1;
        public double heading;

        public Segment(Segment segment) {
          this.position = segment.position;
          this.velocity = segment.velocity;
          this.acceleration = segment.acceleration;
          this.jerk = segment.jerk;
          this.f1 = segment.f1;
          this.heading = segment.heading;
        }
    }   
    
    private static Trajectory mInstance = null;
    
    public static Trajectory getInstance()
    {
        if( mInstance == null )
        {
            mInstance = new Trajectory();
        }
        return mInstance;
    }
    
    private Trajectory()
    {
        mNumSegments = 0;
        mSegments = new Segment[500];
        
        for( int i = 0 ; i < mSegments.length; i++ )
        {
            mSegments[i] = new Segment();
        }
    }
    
    public void generate(double aDistance, double aMaxVel, double aMaxAcc, double aMaxJerk, double startHeading, double endHeading, double aDtSeconds)
    {
        // quadratic formula
        double adjustedMaxVel = Math.min(aMaxVel, (-aMaxAcc*aMaxAcc+Math.sqrt(aMaxAcc*aMaxAcc*aMaxAcc*aMaxAcc+4*aMaxJerk*aMaxJerk*aMaxAcc*aDistance))/(2*aMaxJerk));
                
        double timeAtFullSpeedS = aDistance/adjustedMaxVel;
        
        double numTapsF1 = Math.ceil((adjustedMaxVel/aMaxAcc)/aDtSeconds);
        double numTapsF2 = Math.ceil((aMaxAcc/aMaxJerk)/aDtSeconds);
        double numImpulses = timeAtFullSpeedS/aDtSeconds;
        

        
        mNumSegments = (int)(Math.ceil(numTapsF1+numTapsF2+numImpulses));
        if( mNumSegments > 500 )
        {
            System.err.println("More than 500 segments in trajectory!");
            mNumSegments = 500;
        }
        double deltaHeadingPerUpdate = (endHeading - startHeading) / ((mNumSegments - 1)* 1.0);
        double f1 = 0;
        double lastVel = 0;
        double lastPos = 0;
        double lastAcc = 0;
        double lastHeading = startHeading;
        for( int i = 0; i < mNumSegments; ++i )
        {
            double input = (i < ( (int)Math.ceil(numImpulses) ) ? 1.0 : -1.0);

            // Segment 0 is a special case
            if( i == 0 && numImpulses - Math.floor(numImpulses) > 0 )
            {
                input = numImpulses - Math.floor(numImpulses);
            }
            
            f1 = Math.max(0.0, Math.min(numTapsF1,f1 + input));
            mSegments[i].f1 = f1;
            double f2 = 0;
            for( int j = 0; j < numTapsF2; ++j )
            {
                if( i-j < 0 )
                {
                    break;
                }
                
                f2 += mSegments[i-j].f1;
            }
            f2 = f2 / numTapsF1;
            
            mSegments[i].velocity = f2/numTapsF2*adjustedMaxVel;
            mSegments[i].position = (lastVel + mSegments[i].velocity)/2.0*aDtSeconds + lastPos;
            mSegments[i].acceleration = (mSegments[i].velocity - lastVel)/aDtSeconds;
            mSegments[i].jerk = (mSegments[i].acceleration - lastAcc)/aDtSeconds;
            mSegments[i].heading = lastHeading + deltaHeadingPerUpdate;
 
            lastVel = mSegments[i].velocity;
            lastPos = mSegments[i].position;
            lastAcc = mSegments[i].acceleration;
            lastHeading = mSegments[i].heading;
        }
        
        mDt = aDtSeconds;
        mGoalDistance = aDistance;
    }
    
    public int getNumSegments()
    {
        return mNumSegments;
    }
    
    public double getDt()
    {
        return mDt;
    }
    
    public double getGoalDistance()
    {
        return mGoalDistance;
    }
    
    public Trajectory.Segment getSegment(int aCount)
    {
        if( aCount >= mNumSegments )
        {
            return new Trajectory.Segment();
        }
        else
        {
            return mSegments[aCount];
        }
    }
    
    public String toString()
    {
        String str = "Segment\tTime\tF1\tVel\tPos\tAcc\tJerk\n";
        for( int i = 0; i < getNumSegments(); ++i )
        {
            str += i + "\t";
            str += (double)i*mDt + "\t";
            str += mSegments[i].f1 + "\t";
            str += mSegments[i].velocity + "\t";
            str += mSegments[i].position + "\t";
            str += mSegments[i].acceleration + "\t";
            str += mSegments[i].jerk;
            str += "\n";
        }
        
        return str;
    }

    public void scale(double scale) {
      for( int i = 0; i < mNumSegments; ++i )
      {
        mSegments[i].velocity = mSegments[i].velocity * scale;
        mSegments[i].position = mSegments[i].position * scale;
        mSegments[i].acceleration = mSegments[i].acceleration * scale;
      }
    }

    public Trajectory (Trajectory toClone) {
      this.mNumSegments = toClone.mNumSegments;
      this.mGoalDistance = toClone.mGoalDistance;
      Segment[] temp = new Segment[toClone.mSegments.length];
      for (int i = temp.length - 1; i >= 0; --i) {
          Segment s = toClone.mSegments[i];
          if (s != null) {
              temp[i] = new Segment(s);
          }
      }
      this.mSegments = temp;
      this.mDt = toClone.mDt;
    }
 
}
