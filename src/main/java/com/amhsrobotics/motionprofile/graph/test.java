package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;

public class test {
    public static void main(String... args){
        new Graph("graph", new TrapezoidalMotionProfile(3,12,0,40, 0,0, true));
    }
}
