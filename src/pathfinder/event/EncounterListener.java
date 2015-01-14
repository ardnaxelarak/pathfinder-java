package pathfinder.event;

import pathfinder.Character;

public interface EncounterListener
{
	public void characterUpdated(Character c);
	public void characterAdded(Character c);
	public void characterRemoved(Character c);
	public void selectionUpdated(Character c);
	public void roundUpdated();
}
