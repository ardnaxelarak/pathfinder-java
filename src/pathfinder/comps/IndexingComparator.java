package pathfinder.comps;

import pathfinder.Indexer;

import java.util.Comparator;

public class IndexingComparator<T> implements Comparator<T>
{
	private Indexer<T> indexer;
	public IndexingComparator(Indexer<T> indexer)
	{
		this.indexer = indexer;
	}

	@Override
	public int compare(T t1, T t2)
	{
		int i1 = indexer.getIndex(t1), i2 = indexer.getIndex(t2);
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
}
