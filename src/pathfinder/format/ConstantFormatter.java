package pathfinder.format;

import pathfinder.format.Formatter;

public class ConstantFormatter<T1, T2> implements Formatter<T1, T2>
{
	private T2 value;
	public ConstantFormatter(T2 value)
	{
		this.value = value;
	}

	public T2 getValue(T1 c)
	{
		return value;
	}
}
