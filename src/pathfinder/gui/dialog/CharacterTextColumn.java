package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.chars.CharacterFormatter;
import pathfinder.gui.dialog.MultiColoredTextColumn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class CharacterTextColumn extends MultiColoredTextColumn
{
	private Color npcColor, pcColor;
	private Character[] characters;
	private CharacterFormatter cf;
	private boolean updateOnPaint;
	public CharacterTextColumn(Font font, CharacterFormatter cf, int xGap, int yGap, Color pcBackColor, Color npcBackColor, Color foreColor)
	{
		super(font, xGap, yGap, foreColor);
		this.npcColor = npcBackColor;
		this.pcColor = pcBackColor;
		this.cf = cf;
		updateOnPaint = false;
	}

	public void setUpdateOnPaint(boolean value)
	{
		updateOnPaint = value;
	}

	public boolean getUpdateOnPaint()
	{
		return updateOnPaint;
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
		updateCharacter(index);
	}

	public Character getCharacter(int index)
	{
		return characters[index];
	}

	private void updateCharacter(int index)
	{
		Character c = characters[index];
		super.setText(index, cf.getString(c));
		if (c.isPC())
			super.setBackColor(index, pcColor);
		else
			super.setBackColor(index, npcColor);
	}

	public void updateCharacters()
	{
		for (int i = 0; i < super.getNum(); i++)
			updateCharacter(i);
	}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
		if (updateOnPaint)
			updateCharacters();
		super.draw(g, x, y, width, height, border);
	}
}
