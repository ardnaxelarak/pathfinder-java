package pathfinder.event;

import pathfinder.Character;

public interface CharacterListener
{
	public void initiativeModified(Character c);

	public void nameChanged(Character c);
}
