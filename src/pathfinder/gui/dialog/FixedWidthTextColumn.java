package pathfinder.gui.dialog;

import pathfinder.gui.dialog.TextColumn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
public class FixedWidthTextColumn extends TextColumn
{
	private int width;
	public FixedWidthTextColumn(Font font, int xGap, int yGap, int width, Color backColor, Color foreColor)
	{
		super(font, xGap, yGap, backColor, foreColor);
		this.width = width;
	}

	@Override
	public int getMaxWidth()
	{
		return width;
	}
}
