package pathfinder.mapping;

import pathfinder.mapping.Mapper;

public class ConstantMapper<T1, T2> implements Mapper<T1, T2>
{
	private T2 value;
	public ConstantMapper(T2 value)
	{
		this.value = value;
	}

	public T2 getValue(T1 c)
	{
		return value;
	}
}
