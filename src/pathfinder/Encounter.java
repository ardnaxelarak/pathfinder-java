package pathfinder;

/* local package imports */
import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.Indexer;
import pathfinder.event.EncounterListener;

/* java package imports */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Encounter implements Iterable<Character>
{
    private List<Character> characters;
    private List<EncounterListener> listeners;
    private int round, index;
    private Indexer<Character> indexer;

    public Encounter(Indexer<Character> indexer)
    {
        listeners = new LinkedList<EncounterListener>();
        characters = new ArrayList<Character>(20);
        index = -1;
        this.indexer = indexer;
    }

    public void addListener(EncounterListener listener)
    {
        listeners.add(listener);
    }

    private void setIndex(int index)
    {
        this.index = index;
        Character c = getCurrent();
        for (EncounterListener listener : listeners)
            listener.selectionUpdated(c);
    }

    public void addCharacter(Character c)
    {
        synchronized(this)
        {
            characters.add(c);
        }
        for (EncounterListener listener : listeners)
            listener.charactersAdded(Collections.singleton(c));
    }

    public void removeCharacter(Character c)
    {
        boolean contained;
        synchronized(this)
        {
            contained = characters.remove(c);
        }
        if (contained)
        {
            for (EncounterListener listener : listeners)
                listener.charactersRemoved(Collections.singleton(c));
        }
    }

    public void removeAll(Collection<Character> list)
    {
        LinkedList<Character> removed = new LinkedList<Character>();
        for (Character c : list)
        {
            synchronized(this)
            {
                if (characters.remove(c))
                    removed.add(c);
            }
        }
        if (removed.size() > 0)
        {
            for (EncounterListener listener : listeners)
                listener.charactersRemoved(removed);
        }
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
        for (EncounterListener listener : listeners)
            listener.charactersAdded(list);
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
        setIndex(0);
    }

    public Character next()
    {
        if (index < 0)
        {
            if (characters.size() > 0)
                setIndex(0);
        }
        else
        {
            if (index + 1 >= characters.size())
            {
                setIndex(0);
                setRound(round + 1);
            }
            else
            {
                setIndex(index + 1);
            }
        }
        return getCurrent();
    }

    public Character prev()
    {
        if (index < 0)
        {
            if (characters.size() > 0)
                setIndex(characters.size() - 1);
        }
        else
        {
            if (index == 0)
            {
                setIndex(characters.size() - 1);
                setRound(round - 1);
            }
            else
            {
                setIndex(index - 1);
            }
        }
        return getCurrent();
    }

    public int size()
    {
        return characters.size();
    }

    public int getRound()
    {
        return round;
    }

    public void sortInitiative()
    {
        Collections.sort(characters, Character.INITIATIVE_COMPARATOR);
        for (EncounterListener listener : listeners)
            listener.charactersReordered(this);
    }

    public void rollNPCInitiatives()
    {
        synchronized(this)
        {
            for (Character c : characters)
            {
                if (!(c.isPC()))
                    c.rollInitiative();
            }
        }
    }

    public Character getCurrent()
    {
        if (index >= 0 && index < characters.size())
            return characters.get(index);
        else
            return null;
    }

    public Iterator<Character> iterator()
    {
        return characters.iterator();
    }

    public List<Character> getPCs()
    {
        List<Character> list = new LinkedList<Character>();
        synchronized(this)
        {
            for (Character c : characters)
                if (c.isPC())
                    list.add(c);
        }
        Collections.sort(list, indexer.INDEXING_COMPARATOR);
        return list;
    }

    public List<Character> getCharacters()
    {
        List<Character> list = new LinkedList<Character>();
        synchronized(this)
        {
            for (Character c : characters)
                list.add(c);
        }
        return list;
    }

    public Set<CharacterTemplate> getTemplates()
    {
        Set<CharacterTemplate> templates = new TreeSet<CharacterTemplate>();
        synchronized(this)
        {
            for (Character c : characters)
                templates.add(c.getTemplate());
        }
        return templates;
    }
}
