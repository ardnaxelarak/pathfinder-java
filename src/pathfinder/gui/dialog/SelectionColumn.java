package pathfinder.gui.dialog;

import java.awt.Graphics;

public interface SelectionColumn
{
	public int getMaxWidth();
	public int getMaxHeight();
	public void setNum(int num);
	public int getNum();
	public void setFontMetricsFetcher(FontMetricsFetcher fmf);
	public void draw(Graphics g, int x, int y, int width, int height, int border);
}
