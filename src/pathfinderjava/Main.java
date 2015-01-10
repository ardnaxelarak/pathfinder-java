package pathfinder;

import pathfinder.MySQLConnection;
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
	}
}
