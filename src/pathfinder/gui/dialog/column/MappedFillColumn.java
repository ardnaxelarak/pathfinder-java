package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.FillColumn;
import pathfinder.gui.dialog.column.RowData;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Functions;

/* java package imports */
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MappedFillColumn<T> implements ObjectColumn<T>
{
    private FillColumn base;
    private ArrayList<T> list;
    private Function<? super T, Color> colorFunction;
    private boolean updateOnPaint;

    public MappedFillColumn(int width, Function<? super T, Color> colorFunction)
    {
        base = new FillColumn(width);
        this.colorFunction = colorFunction;
        this.list = new ArrayList<T>();
        this.updateOnPaint = false;
    }

    public MappedFillColumn(int width, Color color)
    {
        this(width, Functions.constant(color));
    }

    public void setUpdateOnPaint(boolean value)
    {
        updateOnPaint = value;
    }

    public boolean getUpdateOnPaint()
    {
        return updateOnPaint;
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

        base.setColor(index, colorFunction.apply(t));
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
