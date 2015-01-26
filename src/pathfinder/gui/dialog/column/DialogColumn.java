package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.RowData;

import java.awt.Graphics2D;

public interface DialogColumn
{
    public int getMaxWidth();

    public int getMaxHeight();

    public void setNum(int num);

    public int getNum();

    public void setFontMetricsFetcher(FontMetricsFetcher fmf);

    public void draw(Graphics2D g, RowData rows);
}
