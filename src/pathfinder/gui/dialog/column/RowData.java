package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.column.CellData;
import pathfinder.gui.dialog.column.RowIterator;

import java.util.Iterator;

public class RowData implements Iterable<CellData>
{
    private int x, y, width, height, border, startIndex, endIndex;
    public RowData(int x, int y, int width, int height, int border, int startIndex, int endIndex)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public Iterator<CellData> iterator()
    {
        return new RowIterator(x, y, width, height, border, startIndex, endIndex);
    }
}
