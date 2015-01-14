package pathfinder;

import pathfinder.CharacterTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class MySQLConnection
{
	private Connection con;
	private Statement st;
	private PreparedStatement charSelect, encSelect, partySelect;
	public MySQLConnection(String url, String user, String password) throws SQLException
	{
		con = DriverManager.getConnection(url, user, password);
		st = con.createStatement();
		charSelect = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
		encSelect = con.prepareStatement("SELECT creature, num FROM e_creatures WHERE eid = ?");
		partySelect = con.prepareStatement("SELECT pc FROM parties WHERE campaign = ?");
	}

	public ResultSet execute(String query) throws SQLException
	{
		return st.executeQuery(query);
	}

	public CharacterTemplate loadCharacter(int id) throws SQLException
	{
		ResultSet rs = null;
		try
		{
			charSelect.setInt(1, id);
			rs = charSelect.executeQuery();
			CharacterTemplate c = new CharacterTemplate(rs);
			return c;
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
	}

	public Group loadEncounter(int id) throws SQLException
	{
		ResultSet rs = null;
		int charID, num;
		LinkedList<Character> list = new LinkedList<Character>();
		CharacterTemplate ct;
		try
		{
			encSelect.setInt(1, id);
			rs = encSelect.executeQuery();
			while (rs.next())
			{
				charID = rs.getInt("creature");
				num = Functions.roll(rs.getString("num"));
				ct = Functions.getTemplate(charID);
				if (ct != null)
				{
					if (num == 1)
						list.add(new Character(ct));
					else
						for (int i = 0; i < num; i++)
							list.add(new Character(ct, String.format("%s %d", ct.getName(), i + 1)));
				}
			}

			Group g = new Group();
			g.addAll(list);
			return g;
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
	}

	public Group loadParty(int campaign) throws SQLException
	{
		ResultSet rs = null;
		int charID;
		LinkedList<Character> list = new LinkedList<Character>();
		CharacterTemplate ct;
		Character c;
		try
		{
			partySelect.setInt(1, campaign);
			rs = partySelect.executeQuery();
			while (rs.next())
			{
				charID = rs.getInt("pc");
				ct = Functions.getTemplate(charID);
				if (ct != null)
				{
					c = new Character(ct);
					c.setPC();
					list.add(c);
				}
			}

			Group g = new Group();
			g.addAll(list);
			return g;
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
