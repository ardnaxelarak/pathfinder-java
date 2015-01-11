package pathfinder;

import pathfinder.Character;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection
{
	private Connection con;
	private Statement st;
	private PreparedStatement charSelect;
	public MySQLConnection(String url, String user, String password) throws SQLException
	{
		con = DriverManager.getConnection(url, user, password);
		st = con.createStatement();
		charSelect = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
	}

	public ResultSet execute(String query) throws SQLException
	{
		return st.executeQuery(query);
	}

	public Character loadCharacter(int id) throws SQLException
	{
		ResultSet rs = null;
		try
		{
			charSelect.setInt(1, id);
			rs = charSelect.executeQuery();
			Character c = new Character(rs);
			return c;
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
	}

	public void close() throws SQLException
	{
		con.close();
		st.close();
	}
}
