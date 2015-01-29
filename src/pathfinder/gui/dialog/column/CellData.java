package pathfinder.gui.dialog.column;

/* java package imports */
import java.awt.Rectangle;

public final class CellData
{
    private final int x, y, width, height, index;

    public CellData(int x, int y, int width, int height, int index)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.index = index;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getIndex()
    {
        return index;
    }

    public Rectangle getRectangle()
    {
        return new Rectangle(x, y, width, height);
    }
}
