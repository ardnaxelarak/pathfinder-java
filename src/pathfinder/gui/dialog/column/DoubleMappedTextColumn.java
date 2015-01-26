package pathfinder.gui.dialog.column;

import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.BasicTextColumn;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.mapping.ConstantMapper;
import pathfinder.mapping.Mapper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class DoubleMappedTextColumn<T1, T2> implements DialogColumn
{
    private BasicTextColumn base;
    private ArrayList<T1> list1;
    private ArrayList<T2> list2;
    private Mapper<T1, String> textMapper;
    private Mapper<T2, Color> backColorMapper;
    private Mapper<T2, Color> foreColorMapper;
    private boolean updateOnPaint;

    public DoubleMappedTextColumn(Font font, Mapper<T1, String> textMapper, int xGap, int yGap, Mapper<T2, Color> backColorMapper, Mapper<T2, Color> foreColorMapper)
    {
        this.base = new BasicTextColumn(font, xGap, yGap);
        this.textMapper = textMapper;
        this.backColorMapper = backColorMapper;
        this.foreColorMapper = foreColorMapper;
        this.list1 = new ArrayList<T1>();
        this.list2 = new ArrayList<T2>();
        this.updateOnPaint = false;
    }

    public DoubleMappedTextColumn(Font font, Mapper<T1, String> textMapper, int xGap, int yGap, Mapper<T2, Color> backColorMapper, Color foreColor)
    {
        this(font, textMapper, xGap, yGap, backColorMapper, new ConstantMapper<T2, Color>(foreColor));
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
        list1.clear();
        list2.clear();
        for (int i = 0; i < num; i++)
        {
            list1.add(null);
            list2.add(null);
        }
    }

    @Override
    public int getNum()
    {
        return base.getNum();
    }

    public void setObject1(int index, T1 t)
    {
        list1.set(index, t);
        updateObject(index);
    }

    public T1 getObject1(int index)
    {
        return list1.get(index);
    }

    public void setObject2(int index, T2 t)
    {
        list2.set(index, t);
        updateObject(index);
    }

    public T2 getObject2(int index)
    {
        return list2.get(index);
    }

    private void updateObject(int index)
    {
        T1 t1 = list1.get(index);
        T2 t2 = list2.get(index);

        base.setText(index, textMapper.getValue(t1));
        base.setBackColor(index, backColorMapper.getValue(t2));
        base.setForeColor(index, foreColorMapper.getValue(t2));
    }

    public void updateObjects()
    {
        for (int i = 0; i < base.getNum(); i++)
            updateObject(i);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height, int border)
    {
        if (updateOnPaint)
            updateObjects();
        base.draw(g, x, y, width, height, border);
    }
}
