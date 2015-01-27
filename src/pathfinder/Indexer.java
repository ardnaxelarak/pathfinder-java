package pathfinder;

/* local package imports */
import pathfinder.comps.IndexingComparator;
import pathfinder.mapping.IndexingMapper;

/* guava package imports */
import com.google.common.base.Optional;

public abstract class Indexer<T>
{
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

    public IndexingMapper<T> getMapper()
    {
        return new IndexingMapper<T>(this);
    }

    public IndexingComparator<T> getComparator()
    {
        return new IndexingComparator<T>(this);
    }
}
