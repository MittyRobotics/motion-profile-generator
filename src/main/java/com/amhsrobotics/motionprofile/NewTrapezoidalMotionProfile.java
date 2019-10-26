package com.amhsrobotics.motionprofile;

public class NewTrapezoidalMotionProfile {


    private MotionSegment accelerationSegment;
    private MotionSegment cruiseSegment;
    private MotionSegment decelerationSegment;
    private double tTotal;

    private double setpoint;
    private double maxAcceleration;
    private double maxDeceleration;
    private double startVelocity;
    private double endVelocity;
    private double maxVelocity;
    private double startPoint;
    
    private double prevTime;
    private double prevVelocity;
    
    public NewTrapezoidalMotionProfile(double setpoint, double maxAcceleration, double maxDeceleration, double maxVelocity, double startVelocity, double endVelocity, double startPoint) {
        if(setpoint < 0){
            maxAcceleration = -maxAcceleration;
            maxVelocity = -maxVelocity;
            maxDeceleration = -maxDeceleration;
        }
        
        this.maxAcceleration = maxAcceleration;
        this.maxDeceleration = maxDeceleration;
        this.startVelocity = startVelocity;
        this.endVelocity = endVelocity;
        this.maxVelocity = maxVelocity;
        this.startPoint = startPoint;
        this.setpoint = setpoint;


        //Initial calculation
        calculateMotionProfile();
    
        
        double c = accelerationSegment.getT();

        double f = cruiseSegment.getT() + accelerationSegment.getT();
        
        double finalVelocity = IntegralMath.integral(f, tTotal,decelerationSegment.getF()) + IntegralMath.integral(0,c,accelerationSegment.getF()) + IntegralMath.integral(c,f, cruiseSegment.getF());
        System.out.println(finalVelocity);
        
        double setpointDifference = setpoint - finalVelocity;
        
        this.setpoint = setpoint + setpointDifference;
        
        //Adjusted setpoint calculation
        calculateMotionProfile();
    }
    
    private void calculateMotionProfile(){

        double theoreticalMaxVelocity = Math.sqrt((maxDeceleration*(startVelocity*startVelocity)+2*maxAcceleration*setpoint*maxDeceleration)/(maxAcceleration+maxDeceleration));
        
        double tAccel = (maxVelocity - startVelocity) / maxAcceleration;
        double tDecel = (maxVelocity - endVelocity) / maxDeceleration;
        double dAccel = maxVelocity * tAccel / 2;
        double dDecel = maxVelocity * tDecel / 2;
    
        double dCruise = this.setpoint - dAccel  - dDecel;
        double tCruise = dCruise / maxVelocity;
    
        double tTotal = tAccel + tDecel + tCruise;
        double dTotal = dAccel + dCruise + dDecel;
        
        if ((dCruise <= 0 && maxAcceleration > 0) || (dCruise >= 0 && maxAcceleration < 0) || maxVelocity == 0) {
            this.maxVelocity = theoreticalMaxVelocity - 0.001;
            tAccel = (maxVelocity - startVelocity) / maxAcceleration;
            tDecel = (maxVelocity - endVelocity) / maxDeceleration;
            dAccel = maxVelocity * tAccel / 2;
            dDecel = maxVelocity * tDecel / 2;
    
            dCruise = this.setpoint - dAccel  - dDecel;
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
                return ((d-a)/c)*x + a;
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
                return (h-d)/(g-f) * (x-f) + d;
            }
        };
    
        accelerationSegment = new MotionSegment(tAccel, dAccel,accelerationFunction);
        cruiseSegment = new MotionSegment(tCruise, dCruise,cruiseFunction);
        decelerationSegment = new MotionSegment(tDecel, dDecel,decelerationFunction);
    }

    public MotionFrame getFrameAtTime(double t) {

        double velocity = getVelocityAtTime(t);
        double position = getPositionAtTime(t);
        double acceleration = getAccelerationAtTime(t, velocity);

        if (t >= tTotal) {
            return new MotionFrame(position + startPoint, velocity, acceleration, t);
        } else {
            return new MotionFrame(position + startPoint, velocity, acceleration, t);
        }
    }


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
    
    
    private double getPositionAtTime(double t) {
        double output = 0;
        
        double c = accelerationSegment.getT();
        double f = cruiseSegment.getT() + accelerationSegment.getT();
    
        if(t <= accelerationSegment.getT()){
            output = IntegralMath.integral(0, t,accelerationSegment.getF());
        }
        else if(t <= cruiseSegment.getT() +  accelerationSegment.getT()){
            output = IntegralMath.integral(c, t,cruiseSegment.getF()) + IntegralMath.integral(0,c,accelerationSegment.getF());
        }
        else{
            output = IntegralMath.integral(f, t,decelerationSegment.getF()) + IntegralMath.integral(0,c,accelerationSegment.getF()) + IntegralMath.integral(c,f, cruiseSegment.getF());
        }

        return output;
        
    }
    
    private double getAccelerationAtTime(double t, double velocity) {
    
        double acceleration;
        if(t == 0){
            acceleration = 0;
        }
        else {
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

    public double getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(double startPoint) {
        this.startPoint = startPoint;
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

}

