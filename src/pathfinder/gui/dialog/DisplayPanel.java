package pathfinder.gui.dialog;

import pathfinder.Functions;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.SelectionColumn;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DisplayPanel extends JPanel implements FontMetricsFetcher
{
	private int height = 16;
	private int[] widths;
	private int border = 5;
	private int numRows;
	private SelectionColumn[] columns;

	public DisplayPanel(SelectionColumn... columns)
	{
		this.columns = columns;
		for (SelectionColumn sc : columns)
			sc.setFontMetricsFetcher(this);
		widths = new int[columns.length];
		numRows = 0;
	}

	public void setNumRows(int numRows)
	{
		this.numRows = numRows;
		for (SelectionColumn sc : columns)
			sc.setNum(numRows);
	}

	public void update()
	{
		int num = columns.length;
		int maxHeight = 0;
		int totWidth = 0;
		for (int i = 0; i < num; i++)
		{
			SelectionColumn sc = columns[i];
			widths[i] = sc.getMaxWidth();
			int curH = sc.getMaxHeight();
			if (curH > maxHeight)
				maxHeight = curH;
			totWidth += widths[i];
		}
		height = maxHeight;
		setPreferredSize(new Dimension(totWidth, border + numRows * (height + border)));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		Functions.enableTAA(g);
		int x = 0;
		int num = columns.length;
		for (int i = 0; i < num; i++)
		{
			SelectionColumn sc = columns[i];
			sc.draw(g, x, border, widths[i], height, border);
			x += widths[i];
		}
	}
}
