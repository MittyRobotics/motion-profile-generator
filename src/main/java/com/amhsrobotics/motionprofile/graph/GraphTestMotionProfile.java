package com.amhsrobotics.motionprofile.graph;

import com.amhsrobotics.motionprofile.TrapezoidalMotionProfile;
import com.amhsrobotics.motionprofile.datatypes.MechanismBounds;
import com.amhsrobotics.motionprofile.datatypes.VelocityConstraints;

/**
 * Testing motion profile class. This generates a test motion profile and graphs it.
 */
public class GraphTestMotionProfile {
    public static void main(String... args){
        double acceleration = 5; 		//units/sec^2
        double deceleration = 5; 		//units/sec^2
        double maxVelocity = 10; 		//units/sec
        double startVelocity = 0; 		//units/sec
        double endVelocity = 0; 		//units/sec
        double lowerPositionBound = 0; 		//units
        double upperPositionBound = 124.5; 	//units
        double currentPosition = 22.4; 		//units
        double setpoint = 80; 			//units

        VelocityConstraints velocityConstraints = new VelocityConstraints(
                acceleration,
                deceleration,
                maxVelocity,
                startVelocity,
                endVelocity
        );

        MechanismBounds mechanismBounds = new MechanismBounds(
                currentPosition,
                lowerPositionBound,
                upperPositionBound
        );

        TrapezoidalMotionProfile motionProfile = new TrapezoidalMotionProfile(
                setpoint,
                velocityConstraints,
                mechanismBounds
        );

        new GraphMotionProfile(motionProfile);

    }
}
