package motion_profile;

import motion_profile.enums.SegmentType;

public class MotionSegment {
	private SegmentType segment;
	private double t;
	private double distance;
	public MotionSegment(SegmentType segment, double t, double distance){
		this.segment = segment;
		this.t = t;
		this.distance = distance;
	}
	public SegmentType getSegment(){
		return segment;
	}
	public double getT(){
		return t;
	}
	public double getDistance(){
		return distance;
	}
}
