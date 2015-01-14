package pathfinder;

import pathfinder.CharacterTemplate;
import pathfinder.Character;

import java.util.Collection;
import java.util.LinkedList;

public class Group
{
	Character[] characters;

	public Group()
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
		addAll(c);
	}

	public void addAll(Character... list)
	{
		Character[] newList = new Character[characters.length + list.length];
		int k = 0;
		for (Character ch : characters)
			newList[k++] = ch;
		for (Character ch : list)
			newList[k++] = ch;
		characters = newList;
	}

	public void addAll(Collection<Character> list)
	{
		Character[] newList = new Character[characters.length + list.size()];
		int k = 0;
		for (Character ch : characters)
			newList[k++] = ch;
		for (Character ch : list)
			newList[k++] = ch;
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

	public void rollInitiative()
	{
		for (Character c : characters)
			c.rollInitiative();
	}

	public void printCharacters()
	{
		for (Character c : characters)
		{
			System.out.printf("%s [%d / %d]\n", c.getName(), c.getCurrentHP(), c.getMaxHP());
		}
	}
}
