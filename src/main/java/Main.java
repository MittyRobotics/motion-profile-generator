import motion_profile.TrapezoidalMotionProfile;

public class Main {

	public static void main(String... args){
		final Graph graph = new Graph("Motion Profile Test", new TrapezoidalMotionProfile(1,5,12,0.05));
	}


}
