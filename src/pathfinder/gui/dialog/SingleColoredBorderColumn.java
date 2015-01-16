package pathfinder.gui.dialog;

import pathfinder.gui.dialog.BorderColumn;

import java.awt.Color;
import java.awt.Graphics;

public class SingleColoredBorderColumn extends BorderColumn
{
	private Color color;
	private int num;
	public SingleColoredBorderColumn(int width, Color color)
	{
		super(width);
		this.color = color;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getNum()
	{
		return num;
	}

	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
		int curY = y;
		for (int i = 0; i < num; i++)
		{
			g.setColor(color);
			g.fillRect(x, curY, width, height);
			curY += height + border;
		}
		super.draw(g, x, y, width, height, border);
	}
}
