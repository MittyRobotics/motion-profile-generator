package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.datatypes.MechanismBounds;
import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;
import com.amhsrobotics.motionprofile.datatypes.VelocityConstraints;

/**
 * Testing motion profile class. This generates a test motion profile and graphs it.
 */
public class GraphTestMotionProfile {
    public static void main(String... args){
        //Mess around with these values and try to brake it! So far it has worked for every case I have tried. If you break it, please message Owen Leather with the values that broke it and a picture of the graph and I will try to fix.
        //Acceleration, deceleration, and max velocity must be positive. If the setpoint is negative, these will be automatically inverted. Start velocity and end velocity can be negative or positive.
        VelocityConstraints velocityConstraints = new VelocityConstraints(20,20,10,0,0);
        //Current position is the current position of the mechanism, min position is the minimum position value of the mechanism (lower bound), max position is the maximum position value of the mechanism (upper bound)
        //Leave current position at 0 if you want to use relative position. Otherwise, the motion profile will be in absolute position.
        //Leave BOTH min and max position at 0 if you don't want any minimum and maximum position bounds.
        MechanismBounds mechanismBounds = new MechanismBounds(0,0,0);
        //Creates a new graph with the bounds. If you want multiple graphs at once, you can duplicate this code to create multiple graphs.
        new Graph("graph", new TrapezoidalMotionProfile(20,velocityConstraints,mechanismBounds));
    }
}
