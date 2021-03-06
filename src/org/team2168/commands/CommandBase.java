package org.team2168.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team2168.OI;
import org.team2168.subsystems.*;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static Drivetrain drivetrain = Drivetrain.getInstance();
    public static IntakePosition intakePosition = IntakePosition.getInstance();
    public static IntakeRollers intakeRollers = IntakeRollers.getInstance();
    public static Tusks catapultTusks = Tusks.getInstance();
    public static Winch catapultWinch = Winch.getInstance();
    public static Flashlight flashlight = Flashlight.getInstance();
    public static Vision vision = Vision.getInstance();
    public static SolenoidBallTapper solenoidTapper = SolenoidBallTapper.getInstance();
    public static ServoBallTapper servoTapper = ServoBallTapper.getInstance();
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command your subsystem is running on the SmartDashboard
        //SmartDashboard.putData(exampleSubsystem);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
