package pathfinder.comps;

import java.util.Comparator;

import pathfinder.Character;
import pathfinder.CharacterMapping;

public class MappingComparator implements Comparator<Character>
{
	private CharacterMapping mapping;
	public MappingComparator(CharacterMapping mapping)
	{
		this.mapping = mapping;
	}

	@Override
	public int compare(Character c1, Character c2)
	{
		int i1 = mapping.getIndex(c1), i2 = mapping.getIndex(c2);
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
