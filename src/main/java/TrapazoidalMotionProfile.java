public class TrapazoidalMotionProfile {
	private MotionSegment accelerationSegment;
	private MotionSegment cruiseSegment;
	private MotionSegment decelerationSegment;

	private double max_acceleration;
	private double max_velocity;
	private double setpoint;
	private int steps;
	private double t_total;

	private double prev_velocity=0;
	private double prev_time=0;

	public TrapazoidalMotionProfile(double max_acceleration, double max_velocity, double setpoint, int steps){

		this.max_acceleration = max_acceleration;
		this.setpoint = setpoint;
		this.steps = steps;

		double theoretical_tTotal = Math.sqrt(setpoint/max_acceleration);
		double theoretical_max_velocity = theoretical_tTotal*max_acceleration;
		double tAccel = max_velocity/max_acceleration;
		double tDecel = max_velocity/max_acceleration;
		double dAccel = max_velocity*tAccel/2;
		double dDecel = max_velocity*tDecel/2;
		double dCruise = setpoint-dAccel-dDecel;
		double tCruise = dCruise/max_velocity;
		double tTotal = tAccel + tDecel + tCruise;
		double new_max_velocity = max_velocity;

		if(dCruise <= 0){
			tAccel = theoretical_max_velocity/max_acceleration;
			tDecel = theoretical_max_velocity/max_acceleration;
			dAccel = theoretical_max_velocity*tAccel/2;
			dDecel = theoretical_max_velocity*tDecel/2;
			tCruise = 0;
			dCruise = 0;
			new_max_velocity = theoretical_max_velocity;
			tTotal = tAccel+tDecel;
		}

		this.max_velocity = new_max_velocity;
		this.t_total = tTotal;

		accelerationSegment = new MotionSegment(SegmentType.ACCELERATION,tAccel,dAccel);
		cruiseSegment = new MotionSegment(SegmentType.CRUISE,tCruise,dCruise);
		decelerationSegment = new MotionSegment(SegmentType.DECELERATION,tDecel,dDecel);
	}

	public double stepsToTime(int _steps){
		return t_total/steps * _steps;
	}

	public MotionFrame getFrameAtTime(double t){
		double velocity = getVelocityAtTime(t, max_acceleration, max_velocity, accelerationSegment, cruiseSegment);
		double position = getPositionAtTime(t,velocity,max_velocity,max_acceleration,accelerationSegment,cruiseSegment);

		double acceleration = getAccelerationAtTime(velocity,prev_velocity,t,prev_time);
		this.prev_velocity = velocity;
		this.prev_time = t;

		return new MotionFrame(position,velocity,acceleration,t);
	}

	private double getVelocityAtTime(double _t, double _acceleration, double _max_velocity, MotionSegment _acceleration_segment, MotionSegment _cruise_segment){
		double output = 0;
		double _tAccel = _acceleration_segment.getT();
		double _tCruise = _cruise_segment.getT();
		if(_t < _tAccel){
			output = _t* _acceleration;
		}
		else if(_t < _tCruise +  _tAccel){
			output = _max_velocity;
		}
		else{
			output = _max_velocity-(_t- _tAccel-_tCruise)*_acceleration;
		}
		return output;
	}

	private double getPositionAtTime(double _t, double _velocity, double _max_velocity, double _acceleration, MotionSegment _acceleration_segment, MotionSegment _cruise_segment){
		double output=0;
		double _tAccel = _acceleration_segment.getT();
		double _dAccel = _acceleration_segment.getDistance();
		double _tCruise = _cruise_segment.getT();
		double _dCruise = _cruise_segment.getDistance();
		if(_t < _tAccel){
			output = _t*_velocity/2;
		}
		else if(_t < _tAccel + _tCruise){
			output = _dAccel;
			output += _max_velocity*(_t-_tAccel);
		}
		else{
			output = _dAccel + _dCruise;
			_t = _t-(_tCruise+_tAccel);
			output += _velocity*_t;
			output += (_max_velocity-_velocity) * _t/2;
		}
		return output;
	}
	private double getAccelerationAtTime(double _velocity, double _prev_velocity, double _t, double _prevTime){
		return (_velocity-_prev_velocity)/(_t-_prevTime);
	}

	public int getSteps(){
		return steps;
	}
	public double getSetpoint(){
		return setpoint;
	}
	public double getMax_acceleration(){
		return max_acceleration;
	}
	public double getMax_velocity(){
		return max_velocity;
	}
	public double getT_total(){
		return t_total;
	}
	public double getPrev_velocity(){
		return prev_velocity;
	}
	public double getPrev_time(){
		return prev_time;
	}
}
