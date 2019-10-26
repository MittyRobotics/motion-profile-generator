package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.NewTrapezoidalMotionProfile;
import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;

public class test {
    public static void main(String... args){
        new Graph("graph", new NewTrapezoidalMotionProfile(20,8,8,10,-10,0,0));
        new Graph("graph", new NewTrapezoidalMotionProfile(20,9,9,10,-10,0,0));
        new Graph("graph", new NewTrapezoidalMotionProfile(20,10,10,10,-10,0,0));
        new Graph("graph", new NewTrapezoidalMotionProfile(20,11,11,10,-10,0,0));
        new Graph("graph", new NewTrapezoidalMotionProfile(20,15,15,10,-10,0,0));
    }
}
