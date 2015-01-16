package pathfinder.chars;

import static pathfinder.Functions.modifierString;
import pathfinder.Character;
import pathfinder.chars.CharacterFormatter;

public class NameInitiativeModifierFormatter implements CharacterFormatter
{
	public String getString(Character c)
	{
		return String.format("%s (%s)", c.getName(), modifierString(c.getInitiativeModifier()));
	}
}
