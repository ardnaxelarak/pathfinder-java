package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.BasicTextColumn;
import pathfinder.gui.dialog.column.ObjectColumn;
import pathfinder.gui.dialog.column.RowData;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Functions;

/* java package imports */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MappedTextColumn<T> implements ObjectColumn<T>
{
    private BasicTextColumn base;
    private ArrayList<T> list;
    private Function<? super T, String> textFunction;
    private Function<? super T, Color> backColorFunction;
    private Function<? super T, Color> foreColorFunction;
    private boolean updateOnPaint;

    public MappedTextColumn(Font font, Function<? super T, String> textFunction, int xGap, int yGap, Function<? super T, Color> backColorFunction, Function<? super T, Color> foreColorFunction)
    {
        base = new BasicTextColumn(font, xGap, yGap);
        this.textFunction = textFunction;
        this.backColorFunction = backColorFunction;
        this.foreColorFunction = foreColorFunction;
        this.list = new ArrayList<T>();
        this.updateOnPaint = false;
    }

    public MappedTextColumn(Font font, Function<? super T, String> textFunction, int xGap, int yGap, Function<? super T, Color> backColorFunction, Color foreColor)
    {
        this(font, textFunction, xGap, yGap, backColorFunction, Functions.constant(foreColor));
    }

    public MappedTextColumn(Font font, Function<? super T, String> textFunction, int xGap, int yGap, Color backColor, Color foreColor)
    {
        this(font, textFunction, xGap, yGap, Functions.constant(backColor), Functions.constant(foreColor));
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

        base.setText(index, textFunction.apply(t));
        base.setBackColor(index, backColorFunction.apply(t));
        base.setForeColor(index, foreColorFunction.apply(t));
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
