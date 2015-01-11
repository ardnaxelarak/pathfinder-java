package pathfinder.dice;

public class IntegerPiece implements Piece
{
	private int value;
	public IntegerPiece(int value)
	{
		this.value = value;
	}

	public int roll()
	{
		return value;
	}

	public int doubleAverage()
	{
		return value * 2;
	}

	public int maxRoll()
	{
		return value;
	}

	public int minRoll()
	{
		return value;
	}

	public String toString()
	{
		return String.format("%d", value);
	}
}
