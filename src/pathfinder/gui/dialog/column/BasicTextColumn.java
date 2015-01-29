package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.Helper;
import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.CellData;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;

/* java package imports */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class BasicTextColumn implements DialogColumn
{
    private Font font;
    private FontMetrics fm;
    private int xGap, yGap;
    private String[] texts;
    private Color[] backColors;
    private Color[] foreColors;
    private TextLayout layout;
    private int width, num;

    public BasicTextColumn(Font font, int xGap, int yGap)
    {
        this.font = font;
        this.fm = null;
        this.xGap = xGap;
        this.yGap = yGap;
        this.width = -1;
        this.texts = new String[0];
        this.backColors = new Color[0];
        this.foreColors = new Color[0];
        this.layout = TextLayout.CENTER_LEFT;
    }

    public void setFixedWidth(int width)
    {
        this.width = width;
    }

    public void setVariableWidth()
    {
        this.width = -1;
    }

    public void setTextLayout(TextLayout tl)
    {
        this.layout = tl;
    }

    public TextLayout getTextLayout()
    {
        return this.layout;
    }

    @Override
    public int getMaxWidth()
    {
        if (width >= 0)
            return width;
        int wid = 0;
        int cur;
        for (String s : texts)
        {
            cur = fm.stringWidth(s);
            if (cur > wid)
                wid = cur;
        }
        return wid + 2 * xGap;
    }

    @Override
    public int getMaxHeight()
    {
        return fm.getAscent() + fm.getDescent() + 2 * yGap;
    }

    @Override
    public void setFontMetricsFetcher(FontMetricsFetcher fmf)
    {
        fm = fmf.getFontMetrics(font);
    }

    @Override
    public void setNum(int num)
    {
        this.num = num;
        texts = new String[num];
        backColors = new Color[num];
        foreColors = new Color[num];
    }

    @Override
    public int getNum()
    {
        return this.num;
    }

    public void setText(int index, String text)
    {
        this.texts[index] = text;
    }

    public void setBackColor(int index, Color color)
    {
        this.backColors[index] = color;
    }

    public void setForeColor(int index, Color color)
    {
        this.foreColors[index] = color;
    }

    @Override
    public void draw(Graphics2D g, RowData rows)
    {
        g.setFont(font);
        for (CellData cd : rows)
        {
            int i = cd.getIndex();
            Rectangle rect = cd.getRectangle();
            g.setColor(backColors[i]);
            g.fill(rect);
            g.setColor(foreColors[i]);
            rect.grow(-xGap, -yGap);
            Helper.drawAlignedString(g, fm, texts[i], rect, layout);
        }
    }
}
