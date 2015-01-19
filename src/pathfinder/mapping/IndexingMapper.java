package pathfinder.mapping;

import pathfinder.Indexer;
import pathfinder.mapping.Mapper;

public class IndexingMapper<T> implements Mapper<T, String>
{
	Indexer<T> indexer;
	public IndexingMapper(Indexer<T> indexer)
	{
		this.indexer = indexer;
	}

	public String getValue(T t)
	{
		return String.format("%c", indexer.getChar(t));
	}
}
