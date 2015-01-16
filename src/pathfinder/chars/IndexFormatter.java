package pathfinder.chars;

import pathfinder.Character;
import pathfinder.CharacterMapping;
import pathfinder.chars.CharacterFormatter;

public class IndexFormatter implements CharacterFormatter
{
	CharacterMapping cm;
	public IndexFormatter(CharacterMapping cm)
	{
		this.cm = cm;
	}

	public String getString(Character c)
	{
		return String.format("%c", cm.getChar(c));
	}
}
