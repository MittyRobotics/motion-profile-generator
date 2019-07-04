package team1351.motionprofile;

public class MotionSegment {
	private double t;
	private double distance;

	public MotionSegment(double t, double distance) {
		this.t = t;
		this.distance = distance;
	}

	public double getT() {
		return t;
	}

	public double getDistance() {
		return distance;
	}
}
