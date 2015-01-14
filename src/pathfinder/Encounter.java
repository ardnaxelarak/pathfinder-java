package pathfinder;

import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.event.EncounterListener;

import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.synchronizedSortedSet;
import java.util.Collection;
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

	private void charactersAdded(Collection<Character> list)
	{
		for (EncounterListener listener : listeners)
			listener.charactersAdded(list);
	}

	private void charactersRemoved(Collection<Character> list)
	{
		for (EncounterListener listener : listeners)
			listener.charactersRemoved(list);
	}

	private void selectionUpdated(Character c)
	{
		for (EncounterListener listener : listeners)
			listener.selectionUpdated(c);
	}

	public void addCharacter(Character c)
	{
		synchronized(this)
		{
			characters.add(c);
		}
		charactersAdded(singleton(c));
	}

	public void addGroup(Group g)
	{
		Collection<Character> list = new LinkedList<Character>();
		for (Character c : g)
			list.add(c);
		synchronized(this)
		{
			characters.addAll(list);
		}
		charactersAdded(list);
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
}
