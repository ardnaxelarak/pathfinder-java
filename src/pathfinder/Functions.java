package pathfinder;

import pathfinder.MySQLConnection;
import pathfinder.dice.DiceRoll;

import java.sql.SQLException;
import java.util.Random;

public class Functions
{
	private static MySQLConnection conn = null;
	private static Random rand;
	public static void init(String url, String user, String password) throws SQLException
	{
		rand = new Random();
		conn = new MySQLConnection(url, user, password);
	}

	public static CharacterTemplate getTemplate(int id)
	{
		try
		{
			return conn.loadCharacter(id);
		}
		catch (SQLException e)
		{
			return null;
		}
	}

	public static Encounter getEncounter(int id)
	{
		try
		{
			return conn.loadEncounter(id);
		}
		catch (SQLException e)
		{
			return null;
		}
	}

	public static DiceRoll parseDiceRoll(String roll)
	{
		return new DiceRoll(roll, rand);
	}

	public static int roll(String roll)
	{
		return parseDiceRoll(roll).roll();
	}

	public static int roll()
	{
		return rand.nextInt(20) + 1;
	}

	public static double random()
	{
		return rand.nextDouble();
	}

	public static void log(String message, Object... args)
	{
	}

	public static void close()
	{
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
			}
		}
	}
}
