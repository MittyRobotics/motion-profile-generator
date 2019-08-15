package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;

public class test {
    public static void main(String... args){
        new Graph("graph", new TrapezoidalMotionProfile(6,12,9,10,0.06));
    }
}
