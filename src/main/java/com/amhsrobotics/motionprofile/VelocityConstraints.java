package com.amhsrobotics.motionprofile;

public class VelocityConstraints {
	private double maxAcceleration;
	private double maxDeceleration;
	private double maxVelocity;
	private double startVelocity;
	private double endVelocity;
	
	public VelocityConstraints(double maxAcceleration, double maxDeceleration, double maxVelocity, double startVelocity, double endVelocity) {
		this.maxAcceleration = maxAcceleration;
		this.maxDeceleration = maxDeceleration;
		this.maxVelocity = maxVelocity;
		this.startVelocity = startVelocity;
		this.endVelocity = endVelocity;
	}
	
	public double getMaxAcceleration() {
		return maxAcceleration;
	}
	
	public void setMaxAcceleration(double maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	
	public double getMaxDeceleration() {
		return maxDeceleration;
	}
	
	public void setMaxDeceleration(double maxDeceleration) {
		this.maxDeceleration = maxDeceleration;
	}
	
	public double getMaxVelocity() {
		return maxVelocity;
	}
	
	public void setMaxVelocity(double maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	
	public double getStartVelocity() {
		return startVelocity;
	}
	
	public void setStartVelocity(double startVelocity) {
		this.startVelocity = startVelocity;
	}
	
	public double getEndVelocity() {
		return endVelocity;
	}
	
	public void setEndVelocity(double endVelocity) {
		this.endVelocity = endVelocity;
	}
}
