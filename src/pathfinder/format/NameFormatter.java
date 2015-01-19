package pathfinder.format;

import pathfinder.Character;
import pathfinder.format.Formatter;

public class NameFormatter implements Formatter<Character, String>
{
	public String getValue(Character c)
	{
		return c.getName();
	}
}
