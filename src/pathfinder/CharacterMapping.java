package pathfinder;

import pathfinder.Character;

import java.util.LinkedList;

public class CharacterMapping
{
	private Character[] list;
	public CharacterMapping()
	{
		list = new Character[52];
	}

	public char addCharacter(Character c)
	{
		int i = list.length - 1;
		char id = ' ';
		if (list[i] != null)
		{
			for (i = 0; i < list.length && list[i] != null; i++);
			list[i] = c;
			if (i < 26)
				id = (char)('a' + i);
			else
				id = (char)('A' + i - 26);
		}
		else
		{
			for (; i >= 0 && list[i] == null; i--);
			i++;
			list[i] = c;
			if (i < 26)
				id = (char)('a' + i);
			else
				id = (char)('A' + i - 26);
		}
		return id;
	}

	public Character fromChar(char identifier)
	{
		if (identifier >= 'a' && identifier <= 'z')
			return list[identifier - 'a'];
		if (identifier >= 'A' && identifier <= 'Z')
			return list[identifier - 'A' + 26];
		return null;
	}

	public int getIndex(Character c)
	{
		for (int i = 0; i < 52; i++)
			if (list[i] == c)
				return i;
		return -1;
	}

	public int getChar(Character c)
	{
		int i = getIndex(c);
		if (i < 0)
			return ' ';
		else if (i < 26)
			return 'a' + i;
		else
			return 'A' + i - 26;
	}

	public void removeCharacter(Character c)
	{
		int i = getIndex(c);
		list[i] = null;
	}
}
