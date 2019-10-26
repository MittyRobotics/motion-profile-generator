package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.NewTrapezoidalMotionProfile;
import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;

public class test {
    public static void main(String... args){
        new Graph("graph", new NewTrapezoidalMotionProfile(20,20,20,10,-5,-5,0));

    }
}
