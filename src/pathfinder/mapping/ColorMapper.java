package pathfinder.mapping;

import pathfinder.Character;
import pathfinder.mapping.Mapper;

import java.awt.Color;

public class ColorMapper implements Mapper<Character, Color>
{
	private Color pcColor, npcColor;

	public ColorMapper(Color pcColor, Color npcColor)
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
