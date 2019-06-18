import motion_profile.TrapazoidalMotionProfile;

public class Main {

	public static void main(String... args){
		final Graph graph = new Graph("Motion Profile Test", new TrapazoidalMotionProfile(1,3,12,200));
	}


}
