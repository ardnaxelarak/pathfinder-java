package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.column.BasicColumn;
import pathfinder.gui.dialog.column.CellData;
import pathfinder.gui.dialog.column.RowData;

import java.awt.Color;
import java.awt.Graphics2D;

public class FillColumn extends BasicColumn
{
    private Color[] colors;

    public FillColumn(int width)
    {
        super(width);
        colors = new Color[0];
    }

    public void setNum(int num)
    {
        colors = new Color[num];
    }

    public int getNum()
    {
        return colors.length;
    }

    public void setColor(int index, Color color)
    {
        colors[index] = color;
    }

    public Color getColor(int index)
    {
        return colors[index];
    }

    public void draw(Graphics2D g, RowData rows)
    {
        for (CellData cd : rows)
        {
            int i = cd.getIndex();
            g.setColor(colors[i]);
            g.fill(cd.getRectangle());
        }
        super.draw(g, rows);
    }
}
