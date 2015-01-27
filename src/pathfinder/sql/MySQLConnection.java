package pathfinder.sql;

/* local package imports */
import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.Functions;
import pathfinder.Group;
import pathfinder.Skills;

/* java package imports */
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
    private PreparedStatement encList, charList, skillsList;

	public MySQLConnection(String url, String user, String password) throws SQLException
	{
		con = DriverManager.getConnection(url, user, password);
		st = con.createStatement();
		charSelect = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
		encSelect = con.prepareStatement("SELECT creature, num FROM e_creatures WHERE eid = ?");
		partySelect = con.prepareStatement("SELECT pc FROM parties WHERE campaign = ?");
        encList = con.prepareStatement("SELECT e.id, e.name, e.acr FROM encounters e LEFT JOIN (SELECT * FROM marked_encounters WHERE campaign = ?) AS m ON e.id = m.encounter ORDER BY m.campaign DESC, e.name");
        charList = con.prepareStatement("SELECT c.id, c.name, c.cr FROM characters c LEFT JOIN (SELECT * FROM marked_characters WHERE campaign = ?) AS m ON c.id = m.creature ORDER BY m.campaign DESC, c.name");
        skillsList = con.prepareStatement("SELECT s.id, s.name, a.short_name, s.trained_only FROM skills s LEFT JOIN abilities a ON s.ability_mod = a.id");
	}

	public ResultSet execute(String query) throws SQLException
	{
		return st.executeQuery(query);
	}

    public ResultSet getEncounterList(int campaignID) throws SQLException
    {
        encList.setInt(1, campaignID);
        return encList.executeQuery();
    }

    public ResultSet getCharacterList(int campaignID) throws SQLException
    {
        charList.setInt(1, campaignID);
        return charList.executeQuery();
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

    public Skills loadSkills() throws SQLException
    {
        return new Skills(skillsList.executeQuery());
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
