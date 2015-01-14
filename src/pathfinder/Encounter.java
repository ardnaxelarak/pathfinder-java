package pathfinder;

import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.event.EncounterListener;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Encounter implements Iterable<Character>
{
	private NavigableSet<Character> characters;
	private Character current = null;
	private List<EncounterListener> listeners;
	private int round;
	public Encounter()
	{
		listeners = new LinkedList<EncounterListener>();
		characters = new TreeSet<Character>();
	}

	public void addListener(EncounterListener listener)
	{
		listeners.add(listener);
	}

	private void characterUpdated(Character c)
	{
		for (EncounterListener listener : listeners)
			listener.characterUpdated(c);
	}

	private void characterAdded(Character c)
	{
		for (EncounterListener listener : listeners)
			listener.characterAdded(c);
	}

	private void characterRemoved(Character c)
	{
		for (EncounterListener listener : listeners)
			listener.characterRemoved(c);
	}

	private void selectionUpdated(Character c)
	{
		for (EncounterListener listener : listeners)
			listener.selectionUpdated(c);
	}

	public void addCharacter(Character c)
	{
		characters.add(c);
		characterAdded(c);
	}

	private void setRound(int round)
	{
		this.round = round;
		for (EncounterListener listener : listeners)
			listener.roundUpdated();
	}

	public void start()
	{
		round = 0;
		current = characters.first();
		selectionUpdated(current);
	}

	public Character next()
	{
		if (current == null)
			current = characters.first();
		else
		{
			current = characters.higher(current);
			if (current == null)
			{
				current = characters.first();
				setRound(round + 1);
			}
		}
		selectionUpdated(current);
		return current;
	}

	public Character prev()
	{
		if (current == null)
			current = characters.last();
		else
		{
			current = characters.lower(current);
			if (current == null)
			{
				current = characters.last();
				setRound(round - 1);
			}
		}
		selectionUpdated(current);
		return current;
	}

	public int size()
	{
		return characters.size();
	}

	public int getRound()
	{
		return round;
	}

	public Character getCurrent()
	{
		return current;
	}

	public Iterator<Character> iterator()
	{
		return characters.iterator();
	}

	public Iterable<Character> shallowCopy()
	{
		return new LinkedList<Character>(characters);
	}
}
