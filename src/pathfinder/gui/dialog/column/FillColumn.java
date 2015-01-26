package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.column.BasicColumn;

import java.awt.Color;
import java.awt.Graphics;

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

    public void draw(Graphics g, int x, int y, int width, int height, int border)
    {
        int curY = y;
        for (Color color : colors)
        {
            g.setColor(color);
            g.fillRect(x, curY, width, height);
            curY += height + border;
        }
        super.draw(g, x, y, width, height, border);
    }
}
