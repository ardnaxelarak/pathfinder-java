package pathfinder.event;

import pathfinder.Character;
import pathfinder.Encounter;

import java.util.Collection;

public interface EncounterListener
{
    public void charactersAdded(Collection<Character> list);

    public void charactersRemoved(Collection<Character> list);

    public void selectionUpdated(Character c);

    public void charactersReordered(Encounter e);

    public void roundUpdated();
}
