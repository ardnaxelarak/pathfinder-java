package pathfinder.gui.dialog;

/* local package import */
import pathfinder.enums.TextLayout;

/* java package imports */
import java.awt.Font;
import java.awt.Rectangle;

public final class TextColumnFormat
{
    private Font font;
    private int xGap, yGap;
    private TextLayout layout;

    public TextColumnFormat(Font font, TextLayout layout, int xGap, int yGap)
    {
        this.font = font;
        this.layout = layout;
        this.xGap = xGap;
        this.yGap = yGap;
    }

    /* getter methods */
    public Font getFont()
    {
        return font;
    }

    public TextLayout getTextLayout()
    {
        return layout;
    }

    public int getXGap()
    {
        return xGap;
    }

    public int getYGap()
    {
        return yGap;
    }
    /* end getter methods */

    public Rectangle getInner(Rectangle outer)
    {
        Rectangle inner = new Rectangle(outer);
        inner.grow(-xGap, -yGap);
        return inner;
    }
}
