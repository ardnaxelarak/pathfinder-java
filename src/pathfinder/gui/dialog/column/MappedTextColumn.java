package pathfinder.gui.dialog.column;

import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.BasicTextColumn;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;
import pathfinder.mapping.ConstantMapper;
import pathfinder.mapping.Mapper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MappedTextColumn<T> implements DialogColumn
{
    private BasicTextColumn base;
    private ArrayList<T> list;
    private Mapper<T, String> textMapper;
    private Mapper<T, Color> backColorMapper;
    private Mapper<T, Color> foreColorMapper;
    private boolean updateOnPaint;

    public MappedTextColumn(Font font, Mapper<T, String> textMapper, int xGap, int yGap, Mapper<T, Color> backColorMapper, Mapper<T, Color> foreColorMapper)
    {
        base = new BasicTextColumn(font, xGap, yGap);
        this.textMapper = textMapper;
        this.backColorMapper = backColorMapper;
        this.foreColorMapper = foreColorMapper;
        this.list = new ArrayList<T>();
        this.updateOnPaint = false;
    }

    public MappedTextColumn(Font font, Mapper<T, String> textMapper, int xGap, int yGap, Mapper<T, Color> backColorMapper, Color foreColor)
    {
        this(font, textMapper, xGap, yGap, backColorMapper, new ConstantMapper<T, Color>(foreColor));
    }

    public MappedTextColumn(Font font, Mapper<T, String> textMapper, int xGap, int yGap, Color backColor, Color foreColor)
    {
        this(font, textMapper, xGap, yGap, new ConstantMapper<T, Color>(backColor), new ConstantMapper<T, Color>(foreColor));
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
        base.setFixedWidth(width);
    }

    public void setVariableWidth()
    {
        base.setVariableWidth();
    }

    public void setTextLayout(TextLayout tl)
    {
        base.setTextLayout(tl);
    }

    public TextLayout getTextLayout()
    {
        return base.getTextLayout();
    }

    @Override
    public int getMaxWidth()
    {
        return base.getMaxWidth();
    }

    @Override
    public int getMaxHeight()
    {
        return base.getMaxHeight();
    }

    @Override
    public void setFontMetricsFetcher(FontMetricsFetcher fmf)
    {
        base.setFontMetricsFetcher(fmf);
    }

    @Override
    public void setNum(int num)
    {
        base.setNum(num);
        list.clear();
        for (int i = 0; i < num; i++)
            list.add(null);
    }

    @Override
    public int getNum()
    {
        return base.getNum();
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

        base.setText(index, textMapper.getValue(t));
        base.setBackColor(index, backColorMapper.getValue(t));
        base.setForeColor(index, foreColorMapper.getValue(t));
    }

    public void updateObjects()
    {
        for (int i = 0; i < base.getNum(); i++)
            updateObject(i);
    }

    @Override
    public void draw(Graphics2D g, RowData rows)
    {
        if (updateOnPaint)
            updateObjects();
        base.draw(g, rows);
    }
}
