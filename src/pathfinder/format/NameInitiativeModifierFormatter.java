package pathfinder.format;

import pathfinder.Character;
import pathfinder.Functions;
import pathfinder.format.Formatter;

public class NameInitiativeModifierFormatter implements Formatter<Character, String>
{
	public String getValue(Character c)
	{
		return String.format("%s (%s)", c.getName(), Functions.modifierString(c.getInitiativeModifier()));
	}
}
