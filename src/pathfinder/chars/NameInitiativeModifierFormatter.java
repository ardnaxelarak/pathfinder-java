package pathfinder.chars;

import pathfinder.Character;
import pathfinder.Functions;
import pathfinder.chars.CharacterFormatter;

public class NameInitiativeModifierFormatter implements CharacterFormatter
{
	public String getString(Character c)
	{
		return String.format("%s (%s)", c.getName(), Functions.modifierString(c.getInitiativeModifier()));
	}
}
