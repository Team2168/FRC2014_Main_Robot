
package org.team2168.subsystems;

import org.team2168.RobotMap;
import org.team2168.utils.MomentaryDoubleSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This subsystem controls the exit angle of the ball when it is fired from 
 * the catapult.
 */
public class Tusks extends Subsystem {
	private static Tusks instance = null;
	
	private volatile boolean longRange;
	private volatile boolean shortRange;
	private volatile boolean truss;
	
	
    // TUSK ACTUATOR OPERATION
	//        --------------------------------
	//  ------|------|     --||--------|     |
	//        -//------------//-----------//--
	//         C              B           A
	//
	// To achieve the three positions on this actuator the following states must
	// be established. Truth table:
	//
	// POSITION      C           B          A                 NAME
	//   Extended    Vent        Pressure   Vent              Long Range
	//   Retracted   Pressure    Vent       Vent              Truss Shot
	//   Middle      Pressure    Vent       Pressure          Short Range
	
	//Actuator Ports B and C
	private static MomentaryDoubleSolenoid tuskSolenoid1;
	//Actuator Port A
	private static MomentaryDoubleSolenoid tuskSolenoid2;

	/**
	 * A private constructor to prevent multiple instances from being created.
	 */
	private Tusks() {
		
		longRange = false;
		shortRange = false;
		truss = false;
		
		tuskSolenoid1 = new MomentaryDoubleSolenoid(RobotMap.catExtPort1.getInt(),
				RobotMap.catRetPort1.getInt());
		tuskSolenoid2 = new MomentaryDoubleSolenoid(RobotMap.catExtPort2.getInt(),
				RobotMap.catRetPort2.getInt());
	}
	
	/**
	 * 
	 * @return the instance of this subsystem.
	 */
	public static Tusks getInstance() {
		if (instance == null) {
			instance = new Tusks();
		}
		return instance;
	}
    
	/**
	 * Sets the default command for the subsystem
	 */
	public void initDefaultCommand() {
    }

	/**
	 * This method is for shooting close to the goal by changing the position 
	 * of the tusks.
	 */
	public void longRangeShot(){
		tuskSolenoid1.set(DoubleSolenoid.Value.kReverse);
		tuskSolenoid2.set(DoubleSolenoid.Value.kReverse);
		
		longRange = true;
		shortRange = false;
		truss = false;
	}

	/**
	 * This method is for shooting over the truss by changing the position of
	 * the tusks.
	 */
	public void trussShot() {
		tuskSolenoid1.set(DoubleSolenoid.Value.kForward);
		tuskSolenoid2.set(DoubleSolenoid.Value.kReverse);
		
		longRange = false;
		shortRange = false;
		truss = true;
	}

	/**
	 * This method is for shooting long range by changing the position of
	 * the tusks.
	 */
	public void shortRangeShot(){
		tuskSolenoid1.set(DoubleSolenoid.Value.kForward);
		tuskSolenoid2.set(DoubleSolenoid.Value.kForward);
		
		longRange = false;
		shortRange = true;
		truss = false;
		
	}
	
	public void setSolenoid1(DoubleSolenoid.Value state) {
		tuskSolenoid1.set(state);
	}
	
	public void setSolenoid2(DoubleSolenoid.Value state) {
		tuskSolenoid2.set(state);
	}
	
	public boolean isLongRangeShot()
	{
		return longRange;
	}
	
	public boolean isShortRangeShot()
	{
		return shortRange;
	}
	
	public boolean isTrussShot()
	{
		return truss;
	}
	
	
	
}

