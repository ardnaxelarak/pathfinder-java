package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.CellData;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class ArrowColumn implements DialogColumn
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

    public void draw(Graphics2D g, RowData rows)
    {
        for (CellData cd : rows)
        {
            if (cd.getIndex() == index)
            {
                g.setColor(color);
                int xd = cd.getX();
                int yd = cd.getY() + cd.getHeight() / 2;
                arrow.translate(xd, yd);
                g.fillPolygon(arrow);
                arrow.translate(-xd, -yd);
            }
        }
    }
}
