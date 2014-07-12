Team 254's 2014 Robot Code
========
This repository contains the Team 254 2013 FRC codebase. This code is released under the BSD 2-clause license. A copy of this license is included in COPYING.


Contents
--------
Like last year, we used the Netbeans IDE to write the Java codebase for 2014's Aerial Assist. This repository can be conveniently opened in Netbeans, otherwise all of our code lives in src/ inside of packages that are organized by unctionality.

Looking for a good starting point? Take a look at [com.team254.ChezyCompetition](https://github.com/Team254/FRC-2014/blob/master/src/com/team254/frc2014/ChezyCompetition.java).

### com.team254.frc2014
 * **Constants.java** contains constants correspondong to ports, PID values, and shooter speed. These can be accessed, but not modified, by the rest of the codebase.
 * **ChezyCompetition.java** subclasses the IterativeRobot template in order to inherit periodic and disabled states. It handles logic for disabled, autonomous, and tele-operated states.
 * **Automode.java** is a abstract class 'template' for each autonoumos mode stored in package [com.team254.auto](https://github.com/Team254/FRC-2014/blob/master/src/com/team254/auto).
 
### com.team254.frc2014.hotgoal
* **CheesyVisionHotgoalDetector.java** is the code on the robot that communicates with [CheesyVision](https://github.com/Team254/CheesyVision), a webcam based hotgoal detection system.

### com.team254.frc2014.paths
* **AutoPaths.java** is a class that reads paths for autonomous from files using com.team254.lib.TextFileReader

### com.team254.frc2014.subsystems
Representations of aspects of the robot with methods for retrieving source values and checking / setting the state of the subsystem.

### com.team254.frc2014.controllers
State machines and state space controllers that control the flywheel and drivebase.

### com.team254.frc2014.auto
Holds all of the autonomous routines. Can be selected at drivers station usinc com.team254.frc2014.AutoModeSelector

### com.team254.frc204.actions
Holds actions that the robot is supposed to do in a specific time
* **Action.java** is an abstract calss 'template' for all actions
