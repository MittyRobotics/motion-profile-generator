package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.MechanismBounds;
import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;
import com.amhsrobotics.motionprofile.VelocityConstraints;

public class test {
    public static void main(String... args){
        VelocityConstraints velocityConstraints = new VelocityConstraints(20,20,10,0,0);
        MechanismBounds mechanismBounds = new MechanismBounds(30,0,200);
        new Graph("graph", new TrapezoidalMotionProfile(20,velocityConstraints,mechanismBounds));
    }
}
