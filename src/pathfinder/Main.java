package pathfinder;

import pathfinder.CharacterTemplate;
import pathfinder.Functions;
import pathfinder.MySQLConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

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
		Encounter enc = Functions.getEncounter(3);
		if (enc != null)
			enc.printCharacters();
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine())
		{
			String roll = sc.nextLine();
			System.out.println(Functions.roll(roll));
		}
		Functions.close();
	}
}
