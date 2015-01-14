package pathfinder;

import pathfinder.Group;
import pathfinder.MySQLConnection;
import pathfinder.parsing.DiceRollLexer;
import pathfinder.parsing.DiceRollParser;
import pathfinder.parsing.DiceRollParser.RollContext;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

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

	public static Group getEncounter(int id)
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

	public static Group getParty(int campaign)
	{
		try
		{
			return conn.loadParty(campaign);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static int roll(String roll)
	{
		DiceRollLexer lexer = new DiceRollLexer(new ANTLRInputStream(roll));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DiceRollParser parser = new DiceRollParser(tokens);
		return parser.roll(rand).rollValue;
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
