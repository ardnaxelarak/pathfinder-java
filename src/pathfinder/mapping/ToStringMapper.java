package pathfinder.mapping;

import pathfinder.mapping.Mapper;

public class ToStringMapper<T> implements Mapper<T, String>
{
	public String getValue(Object o)
	{
		return o.toString();
	}
}
