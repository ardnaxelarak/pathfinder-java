package pathfinder.mapping;

import pathfinder.Character;
import pathfinder.mapping.Mapper;

public class NameMapper implements Mapper<Character, String>
{
	public String getValue(Character c)
	{
		return c.getName();
	}
}
