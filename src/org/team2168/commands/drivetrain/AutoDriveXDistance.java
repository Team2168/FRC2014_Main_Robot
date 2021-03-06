package org.team2168.commands.drivetrain;

import org.team2168.RobotMap;
import org.team2168.commands.CommandBase;

public class AutoDriveXDistance extends CommandBase{
	private double distance;
	private double speed;
	private double endDistance;
	private boolean finished;
	private double angle;
	private boolean drivingForward = false;
	
	/**
	 * Move the drivetrain forward the specified distance.
	 * @param distance in inches
	 */
	public AutoDriveXDistance(double distance) {
		requires(drivetrain);
		this.distance = distance;
		this.speed = RobotMap.autoNormalSpeed.getDouble();
	}
	
	public AutoDriveXDistance(double distance, double speed) {
		requires(drivetrain);
		this.distance = distance;
		this.speed = speed;
	}

	protected void initialize() {
		finished = false;
		drivetrain.drive(0, 0, false);
		drivetrain.resetEncoders();
		//drivetrain.resetGyro();
		endDistance = drivetrain.getAveragedEncoderDistance() + distance;
		angle = drivetrain.getGyroAngle();
		
		drivingForward = drivetrain.getAveragedEncoderDistance() < endDistance;
		
		//don't drive if the destination position is really close to our
		//current position.
		finished = Math.abs(distance) < 0.5;
	}

	protected void execute() {
		//TODO set the margin of error
		
		double rightSpeed = speed;
		double leftSpeed = speed;
		double currentDistance = drivetrain.getAveragedEncoderDistance();

		//precalculate the steering adjustment value
		double steeringAdjust = ((-1.0/45.0) * Math.abs(drivetrain.getGyroAngle() - angle));
		
		//make the left/right motors go less fast, to correct angle
		//if the current angle is less than the angle we want to be at, by more than 0.2 degrees
		if (drivetrain.getGyroAngle() < angle) {
			//speed = (-1/450)x + 1, where x is the difference between the
			//  current angle and the angle we want to be heading
			if (currentDistance < endDistance) {
				rightSpeed = steeringAdjust + rightSpeed;
			} else {
				leftSpeed = steeringAdjust + leftSpeed;
			}
		} else if (drivetrain.getGyroAngle() > angle) {
			if (currentDistance < endDistance) {
				leftSpeed = steeringAdjust + leftSpeed;
			} else {
				rightSpeed = steeringAdjust + rightSpeed;
			}
		}

		//check if the robot is at, or past our destination position
		if (finished ||
				(drivingForward && currentDistance >= endDistance) ||
				(!drivingForward && currentDistance <= endDistance)) {
			//we're there, stop
			drivetrain.drive(0, 0, false);
			finished = true; 
		} else if(currentDistance < endDistance) {
			//Drive forward 
			drivetrain.drive(rightSpeed, leftSpeed); //use ratelimiter
		} else {
			//Drive backwards
			drivetrain.drive(-rightSpeed, -leftSpeed); //use ratelimiter
		}
//		System.out.println("Right Speed: " + rightSpeed + 
//				" Left Speed: " + leftSpeed + 
//				" Angle = " + drivetrain.getGyroAngle() + 
//				" Distance = " + currentDistance +
//				" 'angle' =" + angle);
		
		//System.out.println(drivetrain.getRightTicks() + " " + drivetrain.getAveragedEncoderDistance());
	}
	
	

	protected void interrupted() {
		drivetrain.drive(0, 0, false); //bypass rate limiter
	}

	protected boolean isFinished() {
		return finished;
	}

	protected void end() {
		drivetrain.drive(0, 0, false); //bypass rate limiter
	}
	
}
