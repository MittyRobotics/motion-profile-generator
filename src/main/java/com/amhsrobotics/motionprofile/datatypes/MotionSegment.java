package com.amhsrobotics.motionprofile.datatypes;

import com.amhsrobotics.motionprofile.math.Function;

public class MotionSegment {
	private double t;
	private double distance;
	private Function f;

	public MotionSegment(double t, double distance, Function f) {
		this.t = t;
		this.distance = distance;
		this.f = f;
	}

	public double getT() {
		return t;
	}

	public double getDistance() {
		return distance;
	}
	
	public Function getF() {
		return f;
	}
	
	public void setF(Function f) {
		this.f = f;
	}
}
