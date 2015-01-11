package pathfinder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection
{
	private Connection con;
	private Statement st;
	public MySQLConnection(String url, String user, String password) throws SQLException
	{
		con = DriverManager.getConnection(url, user, password);
		st = con.createStatement();
	}

	public ResultSet execute(String query) throws SQLException
	{
		return st.executeQuery(query);
	}

	public void close() throws SQLException
	{
		con.close();
		st.close();
	}
}
