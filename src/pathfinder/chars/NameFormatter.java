package pathfinder.chars;

import pathfinder.Character;
import pathfinder.chars.CharacterFormatter;

public class NameFormatter implements CharacterFormatter
{
	public String getString(Character c)
	{
		return c.getName();
	}
}
