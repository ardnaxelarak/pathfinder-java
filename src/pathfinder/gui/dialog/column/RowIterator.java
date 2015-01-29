package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.gui.dialog.column.CellData;

/* java package imports */
import java.util.Iterator;

public class RowIterator implements Iterator<CellData>
{
    private int x, y, width, height, border, startIndex, endIndex;
    private int curY, index;

    public RowIterator(int x, int y, int width, int height, int border, int startIndex, int numRows)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
        this.startIndex = startIndex;
        this.endIndex = startIndex + numRows;
        index = startIndex;
        curY = y;
    }

    public boolean hasNext()
    {
        return index < endIndex;
    }

    public CellData next()
    {
        if (hasNext())
        {
            CellData ret = new CellData(x, curY, width, height, index);
            curY += height + border;
            index += 1;
            return ret;
        }
        else
        {
            throw new java.util.NoSuchElementException();
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    public void reset()
    {
        index = startIndex;
        curY = y;
    }
}
