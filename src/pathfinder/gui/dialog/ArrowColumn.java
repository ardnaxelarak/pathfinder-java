package pathfinder.gui.dialog;

import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.SelectionColumn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class ArrowColumn implements SelectionColumn
{
	private int size, index, num;
	private Color color;
	private Polygon arrow;

	public ArrowColumn(int size, Color color)
	{
		this.size = size;
		this.color = color;
		this.index = 0;

		arrow = new Polygon();
		arrow.addPoint(0, -size);
		arrow.addPoint(0, size);
		arrow.addPoint(size, 0);
	}

	public int getMaxWidth()
	{
		return size;
	}

	public int getMaxHeight()
	{
		return 2 * size;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getNum()
	{
		return num;
	}

	public void setFontMetricsFetcher(FontMetricsFetcher fmf)
	{
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
		if (index >= 0 && index < num)
		{
			g.setColor(color);
			int yd = y + index * (height + border) + height / 2;
			arrow.translate(x, yd);
			g.fillPolygon(arrow);
			arrow.translate(-x, -yd);
		}
	}
}
