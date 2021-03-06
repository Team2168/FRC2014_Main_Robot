package org.team2168.commands.winch;

import org.team2168.commands.Sleep;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class FireAndReload extends CommandGroup {

	public FireAndReload() {
		//fire the ball
		addSequential(new Fire());

		//wait for the catapult to reach its fired position
		addSequential(new WaitUntilFired());
		addSequential(new Sleep(), 0.3);
		//engage the winch drum
		addSequential(new Reload());
	}
}
