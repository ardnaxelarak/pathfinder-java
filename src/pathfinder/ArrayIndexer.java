package pathfinder;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.util.ArrayList;

public class ArrayIndexer<T> extends Indexer<T>
{
    private ArrayList<T> list;

    public ArrayIndexer()
    {
        list = new ArrayList<T>(52);
        for (int i = 0; i < 52; i++)
            list.add(null);
    }

    public char add(T t)
    {
        int i = list.size() - 1;
        if (list.get(i) != null)
        {
            for (i = 0; i < list.size() && list.get(i) != null; i++);
            if (i >= 52)
                list.add(t);
            else
                list.set(i, t);
        }
        else
        {
            for (; i >= 0 && list.get(i) == null; i--);
            i++;
            list.set(i, t);
        }
        return charFromIndex(i);
    }

    public Optional<T> get(int index)
    {
        try
        {
            return Optional.of(list.get(index));
        }
        catch (IndexOutOfBoundsException e)
        {
            return Optional.absent();
        }
    }

    public int getIndex(T t)
    {
        int index = list.indexOf(t);
        if (index < 0)
            throw new java.util.NoSuchElementException();
        else
            return index;
    }

    public void remove(T t)
    {
        int i = list.indexOf(t);
        list.set(i, null);
        condense();
    }

    private void condense()
    {
        if (list.size() <= 52)
            return;
        for (int i = 0; i < 52 && list.size() > 52; i++)
        {
            if (list.get(i) == null)
                list.set(i, list.remove(52));
        }
    }
}
