package pathfinder;

import pathfinder.CharacterTemplate;
import pathfinder.Functions;
import pathfinder.MySQLConnection;
import pathfinder.dice.DiceRoll;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main
{
	public static void main(String[] args)
	{
		String url = "jdbc:mysql://localhost:3306/pathfinder_info";
		String user = "ruby_user";
		String password = "dNLe7cncmWcbYDdU";
		try
		{
			Functions.init(url, user, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		Encounter enc = Functions.getEncounter(4);
		if (enc != null)
			enc.printCharacters();
		Functions.close();
	}
}
