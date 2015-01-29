package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.BasicTextColumn;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Functions;

/* java package imports */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DoubleMappedTextColumn<T1, T2> implements DialogColumn
{
    private BasicTextColumn base;
    private ArrayList<T1> list1;
    private ArrayList<T2> list2;
    private Function<? super T1, String> textFunction;
    private Function<? super T2, Color> backColorFunction;
    private Function<? super T2, Color> foreColorFunction;
    private boolean updateOnPaint;

    public DoubleMappedTextColumn(Font font, Function<? super T1, String> textFunction, int xGap, int yGap, Function<? super T2, Color> backColorFunction, Function<? super T2, Color> foreColorFunction)
    {
        this.base = new BasicTextColumn(font, xGap, yGap);
        this.textFunction = textFunction;
        this.backColorFunction = backColorFunction;
        this.foreColorFunction = foreColorFunction;
        this.list1 = new ArrayList<T1>();
        this.list2 = new ArrayList<T2>();
        this.updateOnPaint = false;
    }

    public DoubleMappedTextColumn(Font font, Function<? super T1, String> textFunction, int xGap, int yGap, Function<? super T2, Color> backColorFunction, Color foreColor)
    {
        this(font, textFunction, xGap, yGap, backColorFunction, Functions.constant(foreColor));
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
        updateObject1(index);
    }

    public T1 getObject1(int index)
    {
        return list1.get(index);
    }

    public void setObject2(int index, T2 t)
    {
        list2.set(index, t);
        updateObject2(index);
    }

    public T2 getObject2(int index)
    {
        return list2.get(index);
    }

    private void updateObject1(int index)
    {
        T1 t1 = list1.get(index);

        base.setText(index, textFunction.apply(t1));
    }

    private void updateObject2(int index)
    {
        T2 t2 = list2.get(index);

        base.setBackColor(index, backColorFunction.apply(t2));
        base.setForeColor(index, foreColorFunction.apply(t2));
    }

    public void updateObjects()
    {
        for (int i = 0; i < base.getNum(); i++)
        {
            updateObject1(i);
            updateObject2(i);
        }
    }

    @Override
    public void draw(Graphics2D g, RowData rows)
    {
        if (updateOnPaint)
            updateObjects();
        base.draw(g, rows);
    }
}
