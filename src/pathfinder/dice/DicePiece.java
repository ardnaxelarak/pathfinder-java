package pathfinder.dice;

import java.util.Random;

public class DicePiece implements Piece
{
	private int num, sides, multiplier;
	private Random rand;
	public DicePiece(int num, int sides, int multiplier)
	{
		this(num, sides, multiplier, new Random());
	}

	public DicePiece(int num, int sides, int multiplier, Random rand)
	{
		this.num = num;
		this.sides = sides;
		this.multiplier = multiplier;
		this.rand = rand;
	}

	public int roll()
	{
		int value = 0;
		for (int i = 0; i < num; i++)
			value += rand.nextInt(sides) + 1;
		return value * multiplier;
	}

	public int doubleAverage()
	{
		return num * multiplier * (sides + 1);
	}

	public int maxRoll()
	{
		return num * multiplier * sides;
	}

	public int minRoll()
	{
		return num * multiplier;
	}

	public String toString()
	{
		if (multiplier == 1)
			return String.format("%dd%d", num, sides);
		else
			return String.format("%dd%d*%d", num, sides, multiplier);
	}
}
