package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.column.ObjectColumn;

import java.util.LinkedList;

public class ObjectColumnCollection<T>
{
    private LinkedList<ObjectColumn<T>> list;

    public ObjectColumnCollection()
    {
        list = new LinkedList<ObjectColumn<T>>();
    }

    public void add(ObjectColumn<T> column)
    {
        list.add(column);
    }

    public void setObject(int index, T t)
    {
        for (ObjectColumn<T> column : list)
        {
            column.setObject(index, t);
        }
    }
}
