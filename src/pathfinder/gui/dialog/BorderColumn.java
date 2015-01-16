package pathfinder.gui.dialog;

import pathfinder.gui.dialog.SelectionColumn;

import java.awt.Graphics;

public class BorderColumn implements SelectionColumn
{
	private int width, num;
	public BorderColumn(int width)
	{
		this.width = width;
		num = 0;
	}

	@Override
	public int getMaxWidth()
	{
		return width;
	}

	@Override
	public int getMaxHeight()
	{
		return 0;
	}

	@Override
	public void setNum(int num)
	{
		this.num = num;
	}

	@Override
	public int getNum()
	{
		return num;
	}

	@Override
	public void setFontMetricsFetcher(FontMetricsFetcher fmf)
	{
	}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
	}
}
