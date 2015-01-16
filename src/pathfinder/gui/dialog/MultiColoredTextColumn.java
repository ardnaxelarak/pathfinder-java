package pathfinder.gui.dialog;

import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.SelectionColumn;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
public class MultiColoredTextColumn implements SelectionColumn
{
	private Color foreColor;
	private Font font;
	private FontMetrics fm;
	private String[] texts;
	private Color[] backColors;
	private int xGap, yGap;
	public MultiColoredTextColumn(Font font, int xGap, int yGap, Color foreColor)
	{
		this.foreColor = foreColor;
		this.font = font;
		this.fm = null;
		this.xGap = xGap;
		this.yGap = yGap;
		this.texts = new String[0];
		this.backColors = new Color[0];
	}

	public int getMaxWidth()
	{
		int wid = 0;
		int cur;
		for (String s : texts)
		{
			cur = fm.stringWidth(s);
			if (cur > wid)
				wid = cur;
		}
		return wid + 2 * xGap;
	}

	public int getMaxHeight()
	{
		return fm.getAscent() + fm.getDescent() + 2 * yGap;
	}

	public void setFontMetricsFetcher(FontMetricsFetcher fmf)
	{
		fm = fmf.getFontMetrics(font);
	}

	public void setNum(int num)
	{
		texts = new String[num];
		backColors = new Color[num];
	}

	public int getNum()
	{
		return texts.length;
	}

	public void setText(int index, String s)
	{
		texts[index] = s;
	}

	public String getText(int index)
	{
		return texts[index];
	}

	public void setBackColor(int index, Color c)
	{
		backColors[index] = c;
	}

	public Color getBackColor(int index)
	{
		return backColors[index];
	}

	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
		int curY = y;
		int yMod = (fm.getAscent() + height - fm.getDescent()) / 2;
		int num = texts.length;
		for (int i = 0; i < num; i++)
		{
			g.setColor(backColors[i]);
			g.fillRect(x, curY, width, height);
			g.setColor(foreColor);
			g.drawString(texts[i], x + xGap, curY + yMod);

			curY += height + border;
		}
	}
}
