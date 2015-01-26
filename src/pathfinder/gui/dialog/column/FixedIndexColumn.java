package pathfinder.gui.dialog.column;

import pathfinder.Functions;
import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.CellData;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;
import pathfinder.mapping.ConstantMapper;
import pathfinder.mapping.Mapper;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.lang.model.type.NullType;

public class FixedIndexColumn<T> implements DialogColumn
{
    private ArrayList<T> list;
    private Font font;
    private FontMetrics fm;
    private int xGap, yGap;
    private Mapper<T, Color> backColorMapper, foreColorMapper;
    private Color[] backColors;
    private Color[] foreColors;
    private TextLayout layout;
    private int width, num;
    private boolean updateOnPaint, updateOnAdd;

    public FixedIndexColumn(Font font, int xGap, int yGap, Mapper<T, Color> backColorMapper, Mapper<T, Color> foreColorMapper)
    {
        this.font = font;
        this.fm = null;
        this.xGap = xGap;
        this.yGap = yGap;
        this.width = -1;
        this.backColors = new Color[0];
        this.foreColors = new Color[0];
        this.backColorMapper = backColorMapper;
        this.foreColorMapper = foreColorMapper;
        this.layout = TextLayout.CENTER_LEFT;
        this.list = new ArrayList<T>();
        this.updateOnPaint = false;
        this.updateOnAdd = false;
    }

    public FixedIndexColumn(Font font, int xGap, int yGap, Mapper<T, Color> backColorMapper, Color foreColor)
    {
        this(font, xGap, yGap, backColorMapper, new ConstantMapper<T, Color>(foreColor));
    }

    public static FixedIndexColumn<NullType> singleColor(Font font, int xGap, int yGap, Color backColor, Color foreColor)
    {
        FixedIndexColumn<NullType> ret = new FixedIndexColumn<NullType>(font, xGap, yGap, new ConstantMapper<NullType, Color>(backColor), new ConstantMapper<NullType, Color>(foreColor));
        ret.updateOnAdd = true;
        return ret;
    }

    public void setUpdateOnPaint(boolean value)
    {
        updateOnPaint = value;
    }

    public boolean getUpdateOnPaint()
    {
        return updateOnPaint;
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
        String[] texts = new String[52];
        int k = 0;
        for (char c = 'a'; c <= 'z'; c++)
            texts[k++] = c + "";
        for (char c = 'A'; c <= 'Z'; c++)
            texts[k++] = c + "";
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
        backColors = new Color[num];
        foreColors = new Color[num];
        list.clear();
        for (int i = 0; i < num; i++)
            list.add(null);
        if (updateOnAdd)
            updateObjects();
    }

    @Override
    public int getNum()
    {
        return this.num;
    }

    public void setObject(int index, T t)
    {
        list.set(index, t);
        updateObject(index);
    }

    public T getObject(int index)
    {
        return list.get(index);
    }

    private void updateObject(int index)
    {
        T t = list.get(index);

        backColors[index] = backColorMapper.getValue(t);
        foreColors[index] = foreColorMapper.getValue(t);
    }

    public void updateObjects()
    {
        for (int i = 0; i < num; i++)
            updateObject(i);
    }

    @Override
    public void draw(Graphics2D g, RowData rows)
    {
        if (updateOnPaint)
            updateObjects();
        g.setFont(font);
        char c = 'a';
        for (CellData cd : rows)
        {
            int i = cd.getIndex();
            Rectangle rect = cd.getRectangle();
            g.setColor(backColors[i]);
            g.fill(rect);
            g.setColor(foreColors[i]);
            rect.grow(-xGap, -yGap);
            Functions.drawAlignedString(g, fm, c + "", rect, layout);
            if (c == 'z')
                c = 'A';
            else
                c++;
        }
    }
}
