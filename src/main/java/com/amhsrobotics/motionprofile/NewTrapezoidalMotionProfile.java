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


    private double phaseOneEndVelocity;
    private double phaseOneEndTime;
    private double phaseTwoEndVelocity;
    private double phaseTwoEndTime;
    private double phaseThreeEndTime;
    private double phaseThreeEndVelocity;
    private double phaseFourEndTime;
    private double phaseFourEndVelocity;

    public NewTrapezoidalMotionProfile(double setpoint, double maxAcceleration, double maxDeceleration, double maxVelocity, double startVelocity, double endVelocity, double startPoint) {
        this.setpoint = setpoint;
        this.maxAcceleration = maxAcceleration;
        this.maxDeceleration = maxDeceleration;
        this.startVelocity = startVelocity;
        this.endVelocity = endVelocity;
        this.maxVelocity = maxVelocity;
        this.startPoint = startPoint;

        double theoreticalTTotal = Math.sqrt(this.setpoint / maxAcceleration);
        double theoreticalMaxVelocity = theoreticalTTotal * maxAcceleration;
        double tAccel = (maxVelocity - startVelocity) / maxAcceleration;
        double tDecel = (maxVelocity - endVelocity) / maxDeceleration;
        double dAccel = maxVelocity * tAccel / 2;
        double dDecel = maxVelocity * tDecel / 2;
        double dCruise = this.setpoint - dAccel - dDecel;
        double tCruise = dCruise / maxVelocity;
        double tTotal = tAccel + tDecel + tCruise;
        double newMaxVelocity = maxVelocity;

        if ((dCruise <= 0 && maxAcceleration > 0) || (dCruise >= 0 && maxAcceleration < 0) || maxVelocity == 0) {
            tAccel = theoreticalMaxVelocity / maxAcceleration;
            tDecel = theoreticalMaxVelocity / maxAcceleration;
            dAccel = theoreticalMaxVelocity * tAccel / 2;
            dDecel = theoreticalMaxVelocity * tDecel / 2;
            tCruise = 0;
            dCruise = 0;
            newMaxVelocity = theoreticalMaxVelocity;
            tTotal = tAccel + tDecel;
        }


        this.maxVelocity = newMaxVelocity;
        this.tTotal = tTotal;

        accelerationSegment = new MotionSegment(tAccel, dAccel);
        cruiseSegment = new MotionSegment(tCruise, dCruise);
        decelerationSegment = new MotionSegment(tDecel, dDecel);

    }

    public MotionFrame getFrameAtTime(double t) {

        double velocity = getVelocityAtTime(t, maxAcceleration, maxVelocity, accelerationSegment, cruiseSegment);
        double position = getPositionAtTime(t, velocity, maxVelocity, accelerationSegment, cruiseSegment);

        if (t >= tTotal) {
            return new MotionFrame(position + startPoint, velocity, 0, t);
        } else {
            //System.out.println(position + "" + reversed);
            return new MotionFrame(position + startPoint, velocity, 0, t);
        }
    }


    private double getVelocityAtTime(double _t, double _acceleration, double _maxVelocity, MotionSegment _accelerationSegment, MotionSegment _cruiseSegment) {
        double output;

        double _tAccel = _accelerationSegment.getT();
        double _tCruise = _cruiseSegment.getT();
        if (_t < _tAccel) {
            output = _t * _acceleration + startVelocity;
        } else if (_t < _tCruise + _tAccel) {
            output = _maxVelocity;
        } else if (_t >= tTotal) {
            output = endVelocity;
        } else {
            output = _maxVelocity - (_t - _tAccel - _tCruise) * _acceleration;
        }
        return output;
    }

    private double getPositionAtTime(double _t, double _velocity, double _maxVelocity, MotionSegment _accelerationSegment, MotionSegment _cruiseSegment) {
        double output = 0;
        double _tAccel = _accelerationSegment.getT();
        double _dAccel = _accelerationSegment.getDistance();
        double _tCruise = _cruiseSegment.getT();
        double _dCruise = _cruiseSegment.getDistance();

        System.out.println(_tAccel + " " + _dAccel + " " + _tCruise + " " + _dCruise);

        //Phase 1
        if (_t <= _tAccel && _velocity <= 0) {
            output = _velocity * _t;
            output += (startVelocity - _velocity) * _t / 2;
            phaseOneEndVelocity = output;
            phaseOneEndTime = _t;
        }
        //Phase 2
        else if (_t <= _tAccel && _velocity >= 0) {
            output =  ((_t- phaseOneEndTime) * _velocity / 2)+ phaseOneEndVelocity;
            phaseTwoEndVelocity = output;
            phaseTwoEndTime = _t;
        }
        //Phase 3
        else if (_t <= _tAccel + _tCruise) {
            output = phaseTwoEndVelocity;
            output += _maxVelocity * (_t - _tAccel);
            phaseThreeEndVelocity = output;
            phaseThreeEndTime = _t;
        }
        //Phase 4
        if (_t >= _tCruise + _tAccel && _velocity >= 0) {
            output = phaseThreeEndVelocity;
            output += _velocity * (_t - phaseThreeEndTime);
            output += (_maxVelocity - _velocity) * (_t - phaseThreeEndTime) / 2;
            phaseFourEndVelocity = output;
            phaseFourEndTime = _t;
        }
        //Phase 5
        else if (_t >= _tCruise && _velocity <= 0) {
            System.out.println(_t - phaseFourEndTime + " d " + phaseFourEndTime);
            output =  ((_t- phaseFourEndTime) * _velocity / 2)+ phaseFourEndVelocity;
        }
        return output;

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
    public double getPhaseOneEndVelocity() {
        return phaseOneEndVelocity;
    }

    public void setPhaseOneEndVelocity(double phaseOneEndVelocity) {
        this.phaseOneEndVelocity = phaseOneEndVelocity;
    }

    public double getPhaseOneEndTime() {
        return phaseOneEndTime;
    }

    public void setPhaseOneEndTime(double phaseOneEndTime) {
        this.phaseOneEndTime = phaseOneEndTime;
    }

    public double getPhaseTwoEndVelocity() {
        return phaseTwoEndVelocity;
    }

    public void setPhaseTwoEndVelocity(double phaseTwoEndVelocity) {
        this.phaseTwoEndVelocity = phaseTwoEndVelocity;
    }

    public double getPhaseTwoEndTime() {
        return phaseTwoEndTime;
    }

    public void setPhaseTwoEndTime(double phaseTwoEndTime) {
        this.phaseTwoEndTime = phaseTwoEndTime;
    }

    public double getPhaseThreeEndTime() {
        return phaseThreeEndTime;
    }

    public void setPhaseThreeEndTime(double phaseThreeEndTime) {
        this.phaseThreeEndTime = phaseThreeEndTime;
    }

    public double getPhaseThreeEndVelocity() {
        return phaseThreeEndVelocity;
    }

    public void setPhaseThreeEndVelocity(double phaseThreeEndVelocity) {
        this.phaseThreeEndVelocity = phaseThreeEndVelocity;
    }

    public double getPhaseFourEndTime() {
        return phaseFourEndTime;
    }

    public void setPhaseFourEndTime(double phaseFourEndTime) {
        this.phaseFourEndTime = phaseFourEndTime;
    }

    public double getPhaseFourEndVelocity() {
        return phaseFourEndVelocity;
    }

    public void setPhaseFourEndVelocity(double phaseFourEndVelocity) {
        this.phaseFourEndVelocity = phaseFourEndVelocity;
    }
}

