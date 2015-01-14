package pathfinder;

import pathfinder.CharacterTemplate;
import pathfinder.Functions;
import pathfinder.MySQLConnection;
import pathfinder.gui.MainDisplay;

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
		MainDisplay md = new MainDisplay();
		Group enc = Functions.getEncounter(3);
		if (enc != null)
		{
			enc.rollInitiative();
			md.addGroup(enc);
			enc.printCharacters();
		}
		Group party = Functions.getParty(1);
		if (party != null)
		{
			party.rollInitiative();
			md.addGroup(party);
			party.printCharacters();
		}
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine())
		{
			String roll = sc.nextLine();
			System.out.println(Functions.roll(roll));
		}
		Functions.close();
	}
}
