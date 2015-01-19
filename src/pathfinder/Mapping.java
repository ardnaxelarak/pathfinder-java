package pathfinder;

import java.util.ArrayList;

public class Mapping<T>
{
	private ArrayList<T> list;
	public Mapping()
	{
		list = new ArrayList<T>(52);
		for (int i = 0; i < 52; i++)
			list.add(null);
	}

	public char add(T t)
	{
		int i = list.size() - 1;
		char id = ' ';
		if (list.get(i) != null)
		{
			for (i = 0; i < list.size() && list.get(i) != null; i++);
			list.set(i, t);
			if (i < 26)
				id = (char)('a' + i);
			else if (i < 52)
				id = (char)('A' + i - 26);
			else
				id = '+';
		}
		else
		{
			for (; i >= 0 && list.get(i) == null; i--);
			i++;
			list.set(i, t);
			if (i < 26)
				id = (char)('a' + i);
			else
				id = (char)('A' + i - 26);
		}
		return id;
	}

	public T fromChar(char identifier)
	{
		if (identifier >= 'a' && identifier <= 'z')
			return list.get(identifier - 'a');
		if (identifier >= 'A' && identifier <= 'Z')
			return list.get(identifier - 'A' + 26);
		return null;
	}

	public int getIndex(T t)
	{
		return list.indexOf(t);
	}

	public char getChar(T t)
	{
		int i = list.indexOf(t);
		if (i < 0)
			return ' ';
		else if (i < 26)
			return (char)('a' + i);
		else if (i < 52)
			return (char)('A' + i - 26);
		else
			return '+';
	}

	public void remove(T t)
	{
		int i = list.indexOf(t);
		list.set(i, null);
	}
}
