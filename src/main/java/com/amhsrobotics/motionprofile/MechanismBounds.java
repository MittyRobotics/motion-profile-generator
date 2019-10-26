package com.amhsrobotics.motionprofile;

public class MechanismBounds {
	private double minPosition;
	private double maxPosition;
	private double currentPosition;
	
	public MechanismBounds(double currentPosition, double minPosition, double maxPosition) {
		this.minPosition = minPosition;
		this.maxPosition = maxPosition;
		this.currentPosition = currentPosition;
	}
	
	public double getMinPosition() {
		return minPosition;
	}
	
	public void setMinPosition(double minPosition) {
		this.minPosition = minPosition;
	}
	
	public double getMaxPosition() {
		return maxPosition;
	}
	
	public void setMaxPosition(double maxPosition) {
		this.maxPosition = maxPosition;
	}

	public double getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(double currentPosition) {
		this.currentPosition = currentPosition;
	}
}
