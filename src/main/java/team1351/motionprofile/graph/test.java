package team1351.motionprofile.graph;

import team1351.motionprofile.TrapezoidalMotionProfile;

public class test {
    public static void main(String... args){
        new Graph("graph", new TrapezoidalMotionProfile(6,12,8,7.9,0.06, true));
    }
}
