package pathfinder.mapping;

import pathfinder.mapping.Mapper;

public class IdentityMapper<T> implements Mapper<T, T>
{
	public T getValue(T t)
	{
		return t;
	}
}
