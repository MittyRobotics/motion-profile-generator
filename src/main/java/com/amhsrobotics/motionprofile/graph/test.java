package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;

public class test {
    public static void main(String... args){
        new Graph("graph", new TrapezoidalMotionProfile(6,12,0,40, -5,-5,0.06, true));
    }
}
