package pathfinder.format;

import pathfinder.Mapping;
import pathfinder.format.Formatter;

public class MappingFormatter<T> implements Formatter<T, String>
{
	Mapping<T> m;
	public MappingFormatter(Mapping<T> m)
	{
		this.m = m;
	}

	public String getValue(T t)
	{
		return String.format("%c", m.getChar(t));
	}
}
