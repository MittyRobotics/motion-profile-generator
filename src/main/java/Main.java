
public class Main {






	public static void main(String... args){
		final Graph graph = new Graph("Motion Profile Test", generateMotionProfile(1,3,200,12));
	}

	static double[][] generateMotionProfile(double acceleration, double max_velocity, int steps, double setpoint){
		double start_velocity = 0;
		double end_velocity = 0;

		double theoretical_tTotal = Math.sqrt(setpoint/acceleration);
		double theoretical_max_velocity = theoretical_tTotal*acceleration;
		double tAccel = max_velocity/acceleration;
		double tDecel = max_velocity/acceleration;
		double dAccel = max_velocity*tAccel/2;
		double dDecel = max_velocity*tDecel/2;
		double dCruise = setpoint-dAccel-dDecel;
		double tCruise = dCruise/max_velocity;
		double tTotal = tAccel + tDecel + tCruise;
		double new_max_velocity = max_velocity;

		//System.out.println("Theory max: " + theoretical_max_velocity + " Theory time: " + theoretical_tTotal + " Total time: " + tTotal + " Accel time: " + tAccel + " Cruise Time: " + tCruise + " Decel Time: " + tDecel + " Accel Dist: " + dAccel + " Cruise Dist: " + dCruise + " Decel Dist: " + dDecel + " Test Dist: " );
		if(dCruise <= 0){
			tAccel = theoretical_max_velocity/acceleration;
			tDecel = theoretical_max_velocity/acceleration;
			dAccel = theoretical_max_velocity*tAccel/2;
			dDecel = theoretical_max_velocity*tDecel/2;
			tCruise = 0;
			dCruise = 0;
			new_max_velocity = theoretical_max_velocity;
			tTotal = tAccel+tDecel;
		}



		double[][] output = new double[steps][4];
		double prevVelocity = 0;
		double prevTime = 0;
		for(int i = 0; i < output.length; i++){
			output[i][3] = tTotal/steps * i;
			double t = output[i][3];
			if(t < tAccel){
				output[i][1] = getVelocityAtTime(1,t,acceleration,new_max_velocity, tAccel,tCruise);
				output[i][0] = getPositionAtTime(1,t,output[i][1],new_max_velocity,acceleration,tAccel,tCruise,dAccel,dCruise);
			}
			else if(t < tAccel + tCruise){
				output[i][1] = getVelocityAtTime(2,t,acceleration,new_max_velocity,tAccel,tCruise);
				output[i][0] = getPositionAtTime(2,t,output[i][1],new_max_velocity,acceleration,tAccel,tCruise,dAccel,dCruise);
			}
			else{
				output[i][1] = getVelocityAtTime(3,t,acceleration,new_max_velocity,tAccel,tCruise);
				output[i][0] = getPositionAtTime(3,t,output[i][1],new_max_velocity,acceleration,tAccel,tCruise,dAccel,dCruise);
			}
			output[i][2] = getAccelerationAtTime(output[i][1],prevVelocity,t,prevTime);

			prevVelocity = output[i][1];
			prevTime = t;
		}


		// System.out.println("Theory max: " + theoretical_max_velocity +  " Theory time: " + theoretical_tTotal  + " Total time: " + tTotal + " Accel time: " + tAccel + " Cruise Time: " + tCruise + " Decel Time: " + tDecel + " Accel Dist: " + dAccel + " Cruise Dist: " + dCruise + " Decel Dist: " + dDecel + " Test Dist: " );
		return output;
	}

	static double getVelocityAtTime(int segment, double t, double acceleration, double max_velocity, double tAccel, double tCruise){
		double output = 0;
		if(segment == 1){
			output = t*acceleration;
		}
		else if(segment == 2){
			output = max_velocity;
		}
		else{
			output = max_velocity-(t-tAccel-tCruise)*acceleration;
		}
		return output;
	}

	static double getPositionAtTime(int segment, double t, double velocity, double max_velocity, double acceleration, double tAccel, double tCruise, double dAccel, double dCruise){
		double output=0;
		if(segment == 1){
			output = t*velocity/2;
		}
		else if(segment == 2){
			output = dAccel;
			output += max_velocity*(t-tAccel);
		}
		else{
			output = dAccel + dCruise;
			t = t-(tCruise+tAccel);
			output += velocity*t;
			output += (max_velocity-velocity) * t/2;
			// System.out.println(max_velocity);

		}
		return output;
	}
	static double getAccelerationAtTime(double velocity, double prevVelocity, double t, double prevTime){
		return (velocity-prevVelocity)/(t-prevTime);
	}



}
