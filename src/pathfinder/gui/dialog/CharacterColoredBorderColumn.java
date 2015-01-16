package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.gui.dialog.ColoredBorderColumn;

import java.awt.Color;
import java.awt.Graphics;

public class CharacterColoredBorderColumn extends ColoredBorderColumn
{
	private Character[] characters;
	private Color pcColor, npcColor;
	public CharacterColoredBorderColumn(int width, Color pcColor, Color npcColor)
	{
		super(width);
		this.pcColor = pcColor;
		this.npcColor = npcColor;
		characters = new Character[0];
	}

	@Override
	public void setNum(int num)
	{
		super.setNum(num);
		characters = new Character[num];
	}

	public void setCharacter(int index, Character c)
	{
		characters[index] = c;
		if (c.isPC())
			super.setColor(index, pcColor);
		else
			super.setColor(index, npcColor);
	}

	public Character getCharacter(int index)
	{
		return characters[index];
	}
}
