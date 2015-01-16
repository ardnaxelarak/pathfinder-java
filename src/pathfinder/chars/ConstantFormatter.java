package pathfinder.chars;

import pathfinder.Character;
import pathfinder.chars.CharacterFormatter;

public class ConstantFormatter implements CharacterFormatter
{
	private String value;
	public ConstantFormatter(String value)
	{
		this.value = value;
	}

	public String getString(Character c)
	{
		return value;
	}
}
