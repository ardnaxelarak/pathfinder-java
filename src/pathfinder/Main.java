package pathfinder;

import pathfinder.MySQLConnection;
import pathfinder.dice.DiceRollParser;
import pathfinder.dice.DiceRoll;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main
{
	public static void main(String[] args)
	{
		String url = "jdbc:mysql://localhost:3306/pathfinder_info";
		String user = "ruby_user";
		String password = "dNLe7cncmWcbYDdU";
		try
		{
			MySQLConnection conn = new MySQLConnection(url, user, password);
			ResultSet rs = conn.execute("SELECT id, name FROM characters");
			while (rs.next())
			{
				System.out.printf("%3d: %s\n", rs.getInt("id"), rs.getString("name"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		DiceRollParser drp = new DiceRollParser();
		DiceRoll d = drp.parse("2d4 - d3 + 2");
		System.out.println(d);
		System.out.println(d.doubleAverage());
		for (int i = 0; i < 20; i++)
			System.out.println(d.roll());
	}
}
