package pathfinder;

import pathfinder.CharacterTemplate;
import pathfinder.Character;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Encounter
{
	Character[] characters;
	public Encounter(ResultSet set) throws SQLException
	{
		int charID, num;
		LinkedList<Character> list = new LinkedList<Character>();
		CharacterTemplate ct;
		while (set.next())
		{
			charID = set.getInt("creature");
			num = Functions.roll(set.getString("num"));
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
		characters = new Character[list.size()];
		characters = list.toArray(characters);
	}

	public Encounter()
	{
		characters = new Character[0];
	}

	public void addCharacter(CharacterTemplate ct, String name)
	{
		addCharacter(new Character(ct, name));
	}

	public void addCharacter(CharacterTemplate ct)
	{
		addCharacter(new Character(ct));
	}

	public void addCharacter(Character c)
	{
		Character[] newList = new Character[characters.length + 1];
		int k = 0;
		for (Character ch : characters)
			newList[k++] = ch;
		newList[k++] = c;
		characters = newList;
	}

	public Character getCharacter(int index)
	{
		return characters[index];
	}

	public int numCharacters()
	{
		return characters.length;
	}

	public void printCharacters()
	{
		for (Character c : characters)
		{
			System.out.printf("%s [%d / %d]\n", c.getName(), c.getCurrentHP(), c.getMaxHP());
		}
	}
}
