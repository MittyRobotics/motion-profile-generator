# motion-profile-generator

[![](https://jitpack.io/v/Mittyrobotics/motion-profile-generator.svg)](https://jitpack.io/#Mittyrobotics/motion-profile-generator)

Simple motion profile generator for FRC teams.
## Getting Started
This project can be imported using jitpack.io.

Go to https://jitpack.io/#Mittyrobotics/motion-profile-generator for detailed instructions on how to add this to your project using Gradle, Maven, Sbt, or Leiningen aswell as copy and paste code for the latest version.

<details><summary>Gradle</summary>
Add this to your root build.gradle:
  
```python
allprojects {
 repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency:
```python
dependencies {
    compile 'com.github.Mittyrobotics:motion-profile-generator:ENTER_LATEST_VERSION_HERE'
}
```
Replace ENTER_LATEST_VERSION_HERE with the latest version shown on the badge at the top of the README.
</details>
<details><summary>Maven</summary>
Add the JitPack repository to your build file:
  
```python
<repositories>
	<repository>
		   <id>jitpack.io</id>
		   <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency:
```python
<dependency>
	   <groupId>com.github.Mittyrobotics</groupId>
	   <artifactId>motion-profile-generator</artifactId>
	   <version>ENTER_LATEST_VERSION_HERE</version>
</dependency>
  ```
Replace ENTER_LATEST_VERSION_HERE with the latest version shown on the badge at the top of the README.
</details>

## Usage
Create a new TrapezoidalMotionProfile object. Units are in whatever units your position setpoint is in. Units can be anything as long as they are consistent throughout all values passed into the motion profile.
```java
double maxAcceleration = 2; //Maximum acceleration of mechanism (units/second^2)
double maxVelocity = 5; //Maximum velocity of mechanism (units/second)
double setPoint = 12; //Setpoint for mechanism to move to (units)

TrapezoidalMotionProfile motionProfile = new TrapezoidalMotionProfile(maxAcceleration, maxVelocity, setpoint);
```
To get the output of the motion profile, keep track of the time in milliseconds since the begining of the motion profile following. Then get the MotionFrame at that time every time you want to update the motion profile.
```java
double t; //Set this to the time in seconds since you first start following the motion profile.
MotionFrame frame = motionProfile.getFrameAtTime(t);

double feedForwardPosition = frame.getPosition(); //Feedforward position value at time t
double feedForwardVelocity = frame.getVelocity();  //Feedforward velocity value at time t
double feedForwardAcceleration = frame.getAcceleration(); //Feedforward acceleration value at time t
```
The TrapezoidalMotionProfile also supports giving an initial position value. The initial position is then just added to all position output values. This is useful if you want to move your mechanism based on real world position rather than relative position. In other words, if you want to say "I want to move the lift to 10 inches high" instead of "I want to move the lift 10 inches higher than the current position"
```java
double maxAcceleration = 2; //Maximum acceleration of mechanism (units/second^2)
double maxVelocity = 5; //Maximum velocity of mechanism (units/second)
double currentPosition = 4; //The current position of the mechanism (units)
double setPoint = 12; //Setpoint for mechanism to move to (units)
TrapezoidalMotionProfile motionProfile = new TrapezoidalMotionProfile(maxAcceleration, maxVelocity, currentPosition, setpoint);
```
You can follow the feedforward values with your method of choice. For a mechanism such as a slider or linear lift, a good way to following the motion profile is to update the setpoint of a PID loop on the motors with the feedforward position value at the specified looptime you gave the motion profile object.

To check if the motion profile is finished, check for ``` motionProfile.isFinished();``` This will return true if the time input into the motion profile is greater than the total calculated time of the motion profile. That will tell you that the feedforward position is equal to final position. You may still want to continue the PID loop after the motion profile is finished, however, because the feed-forward value is only the setpoint, and the mechanism still needs to follow that value. 

A good way to check if it is finished is to have an ```&&``` statement to check for both the motion profile isFinished() and the sensor position is within a certain threshold of the setpoint to make sure the mechanism has finished moving to the setpoint.
