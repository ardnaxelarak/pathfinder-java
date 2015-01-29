package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.gui.dialog.column.DialogColumn;

public interface ObjectColumn<T> extends DialogColumn
{
    public void setObject(int index, T t);

    public T getObject(int index);
}
