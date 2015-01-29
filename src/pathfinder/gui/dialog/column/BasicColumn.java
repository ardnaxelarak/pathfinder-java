package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;

/* java package imports */
import java.awt.Graphics2D;

public class BasicColumn implements DialogColumn
{
    private int width, num;
    public BasicColumn(int width)
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
    public void draw(Graphics2D g, RowData rows)
    {
    }
}
