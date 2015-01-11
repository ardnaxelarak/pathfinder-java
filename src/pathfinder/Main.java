package pathfinder;

import pathfinder.CharacterTemplate;
import pathfinder.Functions;
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
			CharacterTemplate c = conn.loadCharacter(8);
			System.out.println(c.getURL());
			DiceRoll d = c.getHP();
			System.out.println(d);
			System.out.println(d.doubleAverage());
			System.out.println(d.roll());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
