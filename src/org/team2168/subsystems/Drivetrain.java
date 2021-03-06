package org.team2168.subsystems;

import org.team2168.RobotMap;
import org.team2168.PIDController.sensors.AverageEncoder;
import org.team2168.commands.drivetrain.DrivetrainWithJoystick;
import org.team2168.utils.FalconGyro;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The drivetrain subsystem
 */
public class Drivetrain extends Subsystem {
	private static Drivetrain instance = null;
	private static final boolean INVERT_LEFT = true;
	private static final boolean INVERT_RIGHT = false;
	private static Talon rightMotor;
	private static Talon leftMotor;
	private static FalconGyro gyro;
	private static AverageEncoder driveTrainEncoderLeft,
			driveTrainEncoderRight;
	private double lastLeftSpeed = 0.0;
	private double lastRightSpeed = 0.0;

	/**
	 * A private constructor to prevent multiple instances from being created.
	 */
	private Drivetrain() {
		rightMotor = new Talon(RobotMap.rightDriveMotor.getInt());
		leftMotor = new Talon(RobotMap.leftDriveMotor.getInt());
		gyro = new FalconGyro(RobotMap.gyroPort.getInt());
		gyro.setSensitivity(0.0070);

		driveTrainEncoderRight = new AverageEncoder(
				RobotMap.driveTrainEncoderRightA.getInt(),
				RobotMap.driveTrainEncoderRightB.getInt(),
				RobotMap.driveEncoderPulsePerRot,
				RobotMap.driveEncoderDistPerTick,
				RobotMap.rightDriveTrainEncoderReverse,
				RobotMap.driveEncodingType, RobotMap.driveSpeedReturnType,
				RobotMap.drivePosReturnType, RobotMap.driveAvgEncoderVal);
		// Set min period and rate before reported stopped
		driveTrainEncoderRight.setMaxPeriod(RobotMap.driveEncoderMinPeriod);
		driveTrainEncoderRight.setMinRate(RobotMap.driveEncoderMinRate);
		driveTrainEncoderRight.start();

		driveTrainEncoderLeft = new AverageEncoder(
				RobotMap.driveTrainEncoderLeftA.getInt(),
				RobotMap.driveTrainEncoderLeftB.getInt(),
				RobotMap.driveEncoderPulsePerRot,
				RobotMap.driveEncoderDistPerTick,
				RobotMap.leftDriveTrainEncoderReverse,
				RobotMap.driveEncodingType, RobotMap.driveSpeedReturnType,
				RobotMap.drivePosReturnType, RobotMap.driveAvgEncoderVal);
		// Set min period and rate before reported stopped
		driveTrainEncoderLeft.setMaxPeriod(RobotMap.driveEncoderMinPeriod);
		driveTrainEncoderLeft.setMinRate(RobotMap.driveEncoderMinRate);
		driveTrainEncoderLeft.start();
	}

	/**
	 * 
	 * @return the instance of this subsystem.
	 */
	public static Drivetrain getInstance() {
		if (instance == null) {
			instance = new Drivetrain();
		}
		return instance;
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DrivetrainWithJoystick());
	}

	/**
	 * Drive the left motors.
	 * 
	 * @param speed Normalized speed to drive the motors (1.0 to -1.0)
	 * @param limiter true to enable the rate limiter.
	 */
	public void driveLeft(double speed, boolean limiter) {
		if (INVERT_LEFT)
			speed = -speed;

		if(limiter) {
			lastLeftSpeed = rateLimit(speed, lastLeftSpeed,
					RobotMap.driveRateLimit.getDouble());
		} else {
			lastLeftSpeed = speed;
		}

		leftMotor.set(lastLeftSpeed);
	}
	
	/**
	 * Drive the left motors. Rate limiter enabled by default.
	 * @param speed
	 */
	public void driveLeft(double speed) {
		driveLeft(speed, true);
	}

	/**
	 * Drive the right motors.
	 * 
	 * @param speed Normalized speed to drive the motors (1.0 to -1.0)
	 * @param limiter true to enable the rate limiter.
	 */
	public void driveRight(double speed, boolean limiter) {
		if (INVERT_RIGHT)
			speed = -speed;

		if(limiter) {
			lastRightSpeed = rateLimit(speed, lastRightSpeed,
					RobotMap.driveRateLimit.getDouble());
		} else {
			lastRightSpeed = speed;
		}

		rightMotor.set(lastRightSpeed);
	}
	
	/**
	 * Drive the right motors. Use rate limiter by default.
	 * @param speed Normalized speed to drive the motors (1.0 to -1.0)
	 */
	public void driveRight(double speed) {
		driveRight(speed, true);
	}

	/**
	 * A method to drive the motors on the drivetrain with.
	 * 
	 * @param rightSpeed the speed to drive the right motor at
	 * @param leftSpeed the speed to drive the left motor at
	 * @param limiter true to enable the rate limiter.
	 */
	public void drive(double rightSpeed, double leftSpeed, boolean limiter) {
		this.driveRight(rightSpeed, limiter);
		this.driveLeft(leftSpeed, limiter);
	}
	
	/**
	 * A method to drive the motors on the drivetrain with. Rate limiter is
	 *   enabled by default.
	 * 
	 * @param rightSpeed the speed to drive the right motor at
	 * @param leftSpeed the speed to drive the left motor at
	 */
	public void drive(double rightSpeed, double leftSpeed) {
		drive(rightSpeed, leftSpeed, true);
	}

	/**
	 * Gets the distance the right encoder has turned
	 * 
	 * @return distance in inches
	 */
	public double getRightEncoderDistance() {
		return driveTrainEncoderRight.getDistance();
	}

	/**
	 * Gets the distance the left encoder has turned
	 * 
	 * @return distance in inches
	 */
	public double getLeftEncoderDistance() {
		return driveTrainEncoderLeft.getDistance();
	}

	/**
	 * Averages the distance of the two encoders to get the distance the robot
	 * has traveled.
	 * 
	 * @return distance in inches
	 */
	public double getAveragedEncoderDistance() {
		return (getLeftEncoderDistance() + getRightEncoderDistance()) / 2;
	}

	/**
	 * Reset right encoder to 0.0
	 */
	public void resetRightEncoder() {
		driveTrainEncoderRight.reset();
	}

	/**
	 * Reset left encoder to 0.0
	 */
	public void resetLeftEncoder() {
		driveTrainEncoderLeft.reset();
	}

	/**
	 * Reset both encoders to 0.0;
	 */
	public void resetEncoders() {
		resetLeftEncoder();
		resetRightEncoder();
	}

	/**
	 * Get the current angle of the gyro.
	 * 
	 * @return the angle of the gyro, in degrees.
	 */
	public double getGyroAngle() {
		return gyro.getAngle();
	}

	/**
	 * Re-initialize the gyro. This should not be called during a match.
	 */
	public void reInitGyro() {
		gyro.reInitGyro();
	}

	/**
	 * Set the current robot heading to 0.0
	 */
	public void resetGyro() {
		gyro.reset();
	}

	/**
	 * A simple rate limiter.
	 * http://www.chiefdelphi.com/forums/showpost.php?p=1212189&postcount=3
	 * 
	 * @param input the input value (speed from command/joystick)
	 * @param speed the speed currently being traveled at
	 * @param limit the rate limit
	 * @return the new output speed (rate limited)
	 */
	public static double rateLimit(double input, double speed, double limit) {
		return rateLimit(input, speed, limit, limit);
	}

	/**
	 * A simple rate limiter.
	 * http://www.chiefdelphi.com/forums/showpost.php?p=1212189&postcount=3
	 * 
	 * @param input the input value (speed from command/joystick)
	 * @param speed the speed currently being traveled at
	 * @param posRateLimit the rate limit for accelerating
	 * @param negRateLimit the rate limit for decelerating
	 * @return the new output speed (rate limited)
	 */
	public static double rateLimit(double input, double speed,
			double posRateLimit, double negRateLimit) {
		if (input > 0) {
			if (input > (speed + posRateLimit)) {
				// Accelerating positively
				speed = speed + posRateLimit;
			} else if (input < (speed - negRateLimit)) {
				// Decelerating positively
				speed = speed - negRateLimit;
			} else {
				speed = input;
			}
		} else {
			if (input < (speed - posRateLimit)) {
				// Accelerating negatively
				speed = speed - posRateLimit;
			} else if (input > (speed + negRateLimit)) {
				// Decelerating negatively
				speed = speed + negRateLimit;
			} else {
				speed = input;
			}
		}

		return speed;
	}
}
