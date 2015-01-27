package pathfinder.gui.dialog.column;

public interface ObjectColumn<T> extends DialogColumn
{
    public void setObject(int index, T t);

    public T getObject(int index);
}
