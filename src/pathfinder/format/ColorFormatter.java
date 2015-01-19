package pathfinder.format;

import pathfinder.Character;
import pathfinder.format.Formatter;

import java.awt.Color;

public class ColorFormatter implements Formatter<Character, Color>
{
	private Color pcColor, npcColor;

	public ColorFormatter(Color pcColor, Color npcColor)
	{
		this.pcColor = pcColor;
		this.npcColor = npcColor;
	}

	public Color getValue(Character c)
	{
		if (c.isPC())
			return pcColor;
		else
			return npcColor;
	}
}
