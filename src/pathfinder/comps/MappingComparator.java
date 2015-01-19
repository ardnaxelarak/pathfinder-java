package pathfinder.comps;

import pathfinder.Mapping;

import java.util.Comparator;

public class MappingComparator<T> implements Comparator<T>
{
	private Mapping<T> mapping;
	public MappingComparator(Mapping<T> mapping)
	{
		this.mapping = mapping;
	}

	@Override
	public int compare(T t1, T t2)
	{
		int i1 = mapping.getIndex(t1), i2 = mapping.getIndex(t2);
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
