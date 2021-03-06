package com.amhsrobotics.motionprofile;

import com.amhsrobotics.motionprofile.datatypes.MechanismBounds;
import com.amhsrobotics.motionprofile.datatypes.MotionFrame;
import com.amhsrobotics.motionprofile.datatypes.MotionSegment;
import com.amhsrobotics.motionprofile.datatypes.VelocityConstraints;
import com.amhsrobotics.motionprofile.math.Function;
import com.amhsrobotics.motionprofile.math.IntegralMath;

public class TrapezoidalMotionProfile {


    private MotionSegment accelerationSegment;
    private MotionSegment cruiseSegment;
    private MotionSegment decelerationSegment;
    private double tTotal;

    private double setpoint;
    private double adjustedSetpoint;
    private double maxAcceleration;
    private double maxDeceleration;
    private double startVelocity;
    private double endVelocity;
    private double maxVelocity;
    private double startPosition;

    private double minPosition;
    private double maxPosition;

    private double prevTime;
    private double prevVelocity;

    private boolean isFinished;

    /**
     * TrapezoidalMotionProfile Constructor
     * <p>
     * Creates a new trapezoidal motion profile
     * <p>
     * This motion profile works in both absolute and relative space. Absolute space is where a setpoint of 2000 ticks
     * will move the motor to the encoder position of 2000, and relative space is where a setpoint of 2000 ticks will
     * move the motor 2000 ticks further than it's current position.
     * <p>
     * To use absolute space, input the mechanisms's starting position in the MechanismBounds parameter. To use relative
     * space, leave the starting position at 0.
     * <p>
     * If the mechanism does not have any minimum and maximum position bounds (ex: flywheel, drive motor), leave the
     * minimum and maximum bounds in the MechanismBounds parameter at 0.
     *
     * @param setpoint            setpoint
     * @param velocityConstraints velocity constraints of the motion profile
     * @param mechanismBounds     the min and max position bounds as well as starting position
     */
    public TrapezoidalMotionProfile(double setpoint, VelocityConstraints velocityConstraints, MechanismBounds mechanismBounds) {
        

        if (setpoint < 0) {
            velocityConstraints.setMaxAcceleration(-velocityConstraints.getMaxAcceleration());
            velocityConstraints.setMaxVelocity(-velocityConstraints.getMaxVelocity());
            velocityConstraints.setMaxDeceleration(-velocityConstraints.getMaxDeceleration());
        }

        this.maxAcceleration = velocityConstraints.getMaxAcceleration();
        this.maxDeceleration = velocityConstraints.getMaxDeceleration();
        this.startVelocity = velocityConstraints.getStartVelocity();
        this.endVelocity = velocityConstraints.getEndVelocity();
        this.maxVelocity = velocityConstraints.getMaxVelocity();
        this.startPosition = mechanismBounds.getCurrentPosition();
        this.minPosition = mechanismBounds.getMinPosition();
        this.maxPosition = mechanismBounds.getMaxPosition();
        this.setpoint = setpoint;
        this.adjustedSetpoint =  setpoint - mechanismBounds.getCurrentPosition();

        //Initial calculation
        calculateMotionProfile();


        double c = accelerationSegment.getT();

        double f = cruiseSegment.getT() + accelerationSegment.getT();

        double finalVelocity = IntegralMath.integral(f, tTotal, decelerationSegment.getF()) + IntegralMath.integral(0, c, accelerationSegment.getF()) + IntegralMath.integral(c, f, cruiseSegment.getF());

        double setpointDifference = adjustedSetpoint - finalVelocity;

        this.adjustedSetpoint = adjustedSetpoint + setpointDifference;

        //Adjusted setpoint calculation
        calculateMotionProfile();
    }

    /**
     * Calculates the base outline (the 3 {@link MotionSegment}s) of the motion profile.
     */
    private void calculateMotionProfile() {

        double theoreticalMaxVelocity = Math.sqrt((maxDeceleration * (startVelocity * startVelocity) + 2 * maxAcceleration * adjustedSetpoint * maxDeceleration) / (maxAcceleration + maxDeceleration));

        if(maxVelocity < 0){
			theoreticalMaxVelocity = -theoreticalMaxVelocity;
		}

        double tAccel = (maxVelocity - startVelocity) / maxAcceleration;
        double tDecel = (maxVelocity - endVelocity) / maxDeceleration;
        double dAccel = maxVelocity * tAccel / 2;
        double dDecel = maxVelocity * tDecel / 2;

        double dCruise = this.adjustedSetpoint - dAccel - dDecel;
        double tCruise = dCruise / maxVelocity;

        double tTotal = tAccel + tDecel + tCruise;
        double dTotal = dAccel + dCruise + dDecel;

        if ((dCruise <= 0 && maxAcceleration > 0) || (dCruise >= 0 && maxAcceleration < 0) || maxVelocity == 0) {
            this.maxVelocity = theoreticalMaxVelocity;

            tAccel = (maxVelocity - startVelocity) / maxAcceleration;
            tDecel = (maxVelocity - endVelocity) / maxDeceleration;
            dAccel = maxVelocity * tAccel / 2;
            dDecel = maxVelocity * tDecel / 2;

            dCruise = this.adjustedSetpoint - dAccel - dDecel;
            tCruise = dCruise / maxVelocity;

            tTotal = tAccel + tDecel + tCruise;
            dTotal = dAccel + dCruise + dDecel;

        }

        this.tTotal = tTotal;


        double d = maxVelocity;
        double a = startVelocity;
        double c = tAccel;
        double h = endVelocity;
        double g = tTotal;
        double f = tCruise + tAccel;

        Function accelerationFunction = new Function() {
            @Override
            public double f(double x) {
                return ((d - a) / c) * x + a;
            }
        };
        Function cruiseFunction = new Function() {
            @Override
            public double f(double x) {
                return d;
            }
        };
        Function decelerationFunction = new Function() {
            @Override
            public double f(double x) {
                return (h - d) / (g - f) * (x - f) + d;
            }
        };

        accelerationSegment = new MotionSegment(tAccel, dAccel, accelerationFunction);
        cruiseSegment = new MotionSegment(tCruise, dCruise, cruiseFunction);
        decelerationSegment = new MotionSegment(tDecel, dDecel, decelerationFunction);

        if(maxAcceleration == 0 || maxDeceleration == 0){
			accelerationSegment = new MotionSegment(0, 0, accelerationFunction);
			cruiseSegment = new MotionSegment(0, 0, cruiseFunction);
			decelerationSegment = new MotionSegment(0, 0, decelerationFunction);
		}
    }

    /**
     * Calculates the {@link MotionFrame} at a certain time.
     *
     * @param t time of the motion frame
     * @return a new {@link MotionFrame} at time t
     */
    public MotionFrame getFrameAtTime(double t) {

        double velocity = getVelocityAtTime(t);
        double position = getPositionAtTime(t);
        double acceleration = getAccelerationAtTime(t, velocity);

        if (!(minPosition == 0 && maxPosition == 0)) {
            position = Math.min(position, maxPosition);
            position = Math.max(position, minPosition);
        }

        isFinished = t <= tTotal;

        if (t < tTotal) {
            return new MotionFrame(position, velocity, acceleration, t);
        } else {
            return new MotionFrame(setpoint, endVelocity, acceleration, t);
        }
    }

    /**
     * Returns the velocity of the motion profile at time t.
     *
     * @param t the time of the desired velocity value
     * @return the velocity of the motion profile at time t
     */
    private double getVelocityAtTime(double t) {
        double output;

        if (t < accelerationSegment.getT()) {
            output = t * maxAcceleration + startVelocity;
        } else if (t < cruiseSegment.getT() + accelerationSegment.getT()) {
            output = maxVelocity;
        } else if (t >= tTotal) {
            output = endVelocity;
        } else {
            output = maxVelocity - (t - accelerationSegment.getT() - cruiseSegment.getT()) * maxDeceleration;
        }
        return output;
    }

    /**
     * Returns the position of the motion profile at time t.
     *
     * @param t the time of the desired position value
     * @return the position of the motion profile at time t
     */
    private double getPositionAtTime(double t) {
        double output = 0;

        double c = accelerationSegment.getT();
        double f = cruiseSegment.getT() + accelerationSegment.getT();

        if (t <= accelerationSegment.getT()) {
            output = IntegralMath.integral(0, t, accelerationSegment.getF());
        } else if (t <= cruiseSegment.getT() + accelerationSegment.getT()) {
            output = IntegralMath.integral(c, t, cruiseSegment.getF()) + IntegralMath.integral(0, c, accelerationSegment.getF());
        } else {
            output = IntegralMath.integral(f, t, decelerationSegment.getF()) + IntegralMath.integral(0, c, accelerationSegment.getF()) + IntegralMath.integral(c, f, cruiseSegment.getF());
        }

        return output + startPosition;

    }

    /**
     * Returns the acceleration of the motion profile at time t.
     *
     * @param t the time of the desired acceleration value
     * @return the acceleration of the motion profile at time t
     */
    private double getAccelerationAtTime(double t, double velocity) {

        double acceleration;
        if (t == 0) {
            acceleration = 0;
        } else {
            acceleration = (velocity - prevVelocity) / (t - prevTime);
        }

        this.prevVelocity = velocity;
        this.prevTime = t;
        return acceleration;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
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

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(double startPosition) {
        this.startPosition = startPosition;
    }

    public MotionSegment getAccelerationSegment() {
        return accelerationSegment;
    }

    public void setAccelerationSegment(MotionSegment accelerationSegment) {
        this.accelerationSegment = accelerationSegment;
    }

    public MotionSegment getCruiseSegment() {
        return cruiseSegment;
    }

    public void setCruiseSegment(MotionSegment cruiseSegment) {
        this.cruiseSegment = cruiseSegment;
    }

    public MotionSegment getDecelerationSegment() {
        return decelerationSegment;
    }

    public void setDecelerationSegment(MotionSegment decelerationSegment) {
        this.decelerationSegment = decelerationSegment;
    }

    public double gettTotal() {
        return tTotal;
    }

    public void settTotal(double tTotal) {
        this.tTotal = tTotal;
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

    public double getPrevTime() {
        return prevTime;
    }

    public void setPrevTime(double prevTime) {
        this.prevTime = prevTime;
    }

    public double getPrevVelocity() {
        return prevVelocity;
    }

    public void setPrevVelocity(double prevVelocity) {
        this.prevVelocity = prevVelocity;
    }

    /**
     * Returns if the time inputted into the motion profile is greater than or equal to the calculated total time of the
     * motion profile.
     * <p>
     * This returns if the calculated motion profile is finished, however the actual mechanism will not have exactly
     * the same motion as the motion profile, so it is not a good practice to use this as your final check to see if the
     * motion is finished.
     *
     * @return if the motion profile is finished
     */
    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}

