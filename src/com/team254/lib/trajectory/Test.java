package com.team254.lib.trajectory;

/**
 * Unit tests for trajectorylib.
 * 
 * @author Jared341
 */
public class Test {
    
    static void test(double start_vel, double goal_vel, double goal_distance,
            TrajectoryGenerator.Strategy strategy, String test_case)
            throws Exception {
        TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
        config.dt = .01;
        config.max_acc = 250.0;
        config.max_jerk = 1250.0;
        config.max_vel = 100.0;
        
        Trajectory traj = TrajectoryGenerator.generate(
                config,
                strategy,
                start_vel,
                -75.0,
                goal_distance,
                goal_vel,
                75.0);
        
        System.out.print(traj.toString());
        
        Trajectory.Segment last = traj.getSegment(traj.getNumSegments()-1);
        if (Math.abs(last.pos - goal_distance) > 1.0) {
            throw new Exception("Bad position in case " + test_case);
        }
        if (Math.abs(last.vel - goal_vel) > 1.0) {
            throw new Exception("Bad velocity in case " + test_case);
        }
        if (Math.abs(last.heading - 75.0) > 1.0) {
            throw new Exception("Bad heading in case " + test_case);
        }
    }
    
    public static void main(String[] args) throws Exception {
        try {
            ///// POINT TO POINT MOVES /////
            test(100, 100, 120, TrajectoryGenerator.StepStrategy, "P2P Step");
            test(100, 100, 12, TrajectoryGenerator.StepStrategy, "P2P Short step");
            test(0, 0, 120, TrajectoryGenerator.TrapezoidalStrategy, "P2P Trap");
            test(0, 0, 12, TrajectoryGenerator.TrapezoidalStrategy, "P2P Short trap");
            test(0, 0, 120, TrajectoryGenerator.SCurvesStrategy, "P2P SCurve");
            test(0, 0, 12, TrajectoryGenerator.SCurvesStrategy, "P2P Short SCurve");
            
            ///// NON-ZERO VELOCITY ENDPOINTS /////
            test(0, 100, 120, TrajectoryGenerator.TrapezoidalStrategy, "Ramp Up");
            test(0, 50, 120, TrajectoryGenerator.TrapezoidalStrategy, "Slow Ramp Up");
            test(100, 0, 120, TrajectoryGenerator.TrapezoidalStrategy, "Ramp Down");
            test(50, 0, 120, TrajectoryGenerator.TrapezoidalStrategy, "Slow Ramp Down");
            test(50, 50, 120, TrajectoryGenerator.TrapezoidalStrategy, "Ramp Up Down");
            test(100, 100, 120, TrajectoryGenerator.TrapezoidalStrategy, "Constant Vel Trap");
            
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}
