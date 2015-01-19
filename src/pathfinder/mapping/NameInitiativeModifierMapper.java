package pathfinder.mapping;

import pathfinder.Character;
import pathfinder.Functions;
import pathfinder.mapping.Mapper;

public class NameInitiativeModifierMapper implements Mapper<Character, String>
{
	public String getValue(Character c)
	{
		return String.format("%s (%s)", c.getName(), Functions.modifierString(c.getInitiativeModifier()));
	}
}
