package pathfinder;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Optional;

/* java package imports */
import java.util.Comparator;

public abstract class Indexer<T>
{
    public final Function<T, String> INDEXING_FUNCTION = new Function<T, String>()
    {
        public String apply(T t)
        {
            return getChar(t) + "";
        }
    };

    public final Comparator<T> INDEXING_COMPARATOR = new Comparator<T>()
    {
        public int compare(T t1, T t2)
        {
            int i1 = getIndex(t1);
            int i2 = getIndex(t2);
            if (i1 == i2)
                return 0;
            else if (i1 < 0)
                return 1;
            else if (i2 < 0)
                return -1;
            else if (i1 < i2)
                return -1;
            else
                return 1;
        }
    };

    public abstract Optional<T> get(int index);

    public Optional<T> fromChar(char identifier)
    {
        Optional<Integer> index = indexFromChar(identifier);
        if (index.isPresent())
        {
            return get(index.get().intValue());
        }
        else
        {
            return Optional.absent();
        }
    }

    public abstract int getIndex(T t);

    public char getChar(T t)
    {
        return charFromIndex(getIndex(t));
    }

    public static char charFromIndex(int index)
    {
        if (index < 0)
            return ' ';
        else if (index < 26)
            return (char)('a' + index);
        else if (index < 52)
            return (char)('A' + index - 26);
        else
            return '+';
    }

    public static char charFromIndex(Optional<Integer> index)
    {
        if (index.isPresent())
            return charFromIndex(index.get());
        else
            return ' ';
    }

    public static Optional<Integer> indexFromChar(char c)
    {
        if (c >= 'a' && c <= 'z')
            return Optional.of(c - 'a');
        else if (c >= 'A' && c <= 'Z')
            return Optional.of(c - 'A' + 26);
        else
            return Optional.absent();
    }
}
