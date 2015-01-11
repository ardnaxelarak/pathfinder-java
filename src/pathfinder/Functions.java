package pathfinder;

import pathfinder.dice.DiceRoll;
import pathfinder.dice.DiceRollParser;

import java.util.Random;

public class Functions
{
	private static DiceRollParser drp;
	private static Random rand;
	static
	{
		rand = new Random();
		drp = new DiceRollParser(rand);
	}

	public static DiceRoll parseDiceRoll(String roll)
	{
		return drp.parse(roll);
	}

	public static int roll()
	{
		return rand.nextInt(20) + 1;
	}

	public static void log(String message, Object... args)
	{
	}
}
