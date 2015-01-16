package pathfinder.gui.dialog;

import pathfinder.enums.HorizontalLayout;
import pathfinder.enums.VerticalLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.SelectionColumn;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class MultiColoredTextColumn implements SelectionColumn
{
	private Color foreColor;
	private Font font;
	private FontMetrics fm;
	private String[] texts;
	private Color[] backColors;
	private int xGap, yGap;
	private int width;
	private HorizontalLayout hl;
	private VerticalLayout vl;
	private boolean useMaxLayout;
	public MultiColoredTextColumn(Font font, int xGap, int yGap, Color foreColor)
	{
		this.foreColor = foreColor;
		this.font = font;
		this.fm = null;
		this.xGap = xGap;
		this.yGap = yGap;
		this.width = -1;
		this.texts = new String[0];
		this.backColors = new Color[0];
		this.vl = VerticalLayout.CENTER;
		this.hl = HorizontalLayout.LEFT;
		this.useMaxLayout = true;
	}

	public void setFixedWidth(int width)
	{
		this.width = width;
	}

	public void setVariableWidth()
	{
		this.width = -1;
	}

	public void setHorizontalLayout(HorizontalLayout hl)
	{
		this.hl = hl;
	}

	public void setVerticalLayout(VerticalLayout vl)
	{
		this.vl = vl;
	}

	public void setUseMaxLayout(boolean value)
	{
		useMaxLayout = value;
	}

	public int getMaxWidth()
	{
		if (width >= 0)
			return width;
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

	private void putText(Graphics g, String text, int x, int y, int width, int height)
	{
		Rectangle2D bounds = fm.getStringBounds(text, g);
		double asc, des, xv, yv;
		if (useMaxLayout)
		{
			asc = fm.getAscent();
			des = fm.getDescent();
		}
		else
		{
			asc = -bounds.getY();
			des = (bounds.getY() + bounds.getHeight());
		}
		switch (hl)
		{
		case LEFT:
			xv = x + xGap;
			break;
		case RIGHT:
			xv = x + width - bounds.getWidth() - xGap;
			break;
		case CENTER:
			xv = x + width / 2 - bounds.getWidth() / 2;
			break;
		default:
			xv = x + xGap;
			break;
		}
		switch (vl)
		{
		case TOP:
			yv = y + yGap + asc;
			break;
		case CENTER:
			yv = y + (asc + height - des) / 2;
			break;
		case BOTTOM:
			yv = y + height - des - yGap;
			break;
		default:
			yv = y + yGap + asc;
			break;
		}
		g.drawString(text, (int)xv, (int)yv);
	}

	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
		g.setFont(font);
		int curY = y;
		int num = texts.length;
		for (int i = 0; i < num; i++)
		{
			g.setColor(backColors[i]);
			g.fillRect(x, curY, width, height);
			g.setColor(foreColor);
			putText(g, texts[i], x, curY, width, height);

			curY += height + border;
		}
	}
}
