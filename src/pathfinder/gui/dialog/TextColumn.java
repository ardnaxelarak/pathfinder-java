package pathfinder.gui.dialog;

import pathfinder.gui.dialog.MultiColoredTextColumn;

import java.awt.Color;
import java.awt.Font;
public class TextColumn extends MultiColoredTextColumn
{
	private Color backColor;
	public TextColumn(Font font, int xGap, int yGap, Color backColor, Color foreColor)
	{
		super(font, xGap, yGap, foreColor);
		this.backColor = backColor;
	}

	@Override
	public void setNum(int num)
	{
		super.setNum(num);
		for (int i = 0; i < num; i++)
			super.setBackColor(i, backColor);
	}
}
