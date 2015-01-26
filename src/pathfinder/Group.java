package pathfinder;

/* local package imports */
import pathfinder.Character;
import pathfinder.CharacterTemplate;

/* java package imports */
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Group implements Iterable<Character>
{
	LinkedList<Character> characters;

	public Group()
	{
		characters = new LinkedList<Character>();
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
		characters.add(c);
	}

	public void addAll(Collection<Character> list)
	{
		characters.addAll(list);
	}

	public Character get(int index)
	{
		return characters.get(index);
	}

	public int size()
	{
		return characters.size();
	}

	public Iterator<Character> iterator()
	{
		return characters.iterator();
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
