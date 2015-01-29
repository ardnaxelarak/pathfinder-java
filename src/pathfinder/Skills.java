package pathfinder;

/* local package imports */
import pathfinder.Character;
import pathfinder.enums.Ability;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeMap;

public class Skills extends Indexer<Skill>
{
    private TreeMap<Integer, Skill> list;

	public Skills(ResultSet set) throws SQLException
	{
        list = new TreeMap<Integer, Skill>();
        int id;
        String name;
        Ability ability;
        boolean trained;
        Skill current;
        while (set.next())
        {
            id = set.getInt("id");
            name = set.getString("name");
            ability = Ability.getAbility(set.getString("short_name"));
            trained = set.getBoolean("trained_only");
            current = new Skill(id, name, ability, trained);
            list.put(id, current);
        }
	}

    public Optional<Skill> get(int id)
    {
        return Optional.fromNullable(list.get(id));
    }

    public int getIndex(Skill s)
    {
        return s.getID();
    }

    public int getModifier(int skill, Character c)
    {
        Optional<Skill> fetch = get(skill);
        if (fetch.isPresent())
        {
            Skill s = fetch.get();
            return getModifier(s, c);
        }
        else
        {
            throw new java.util.NoSuchElementException("key not found: " + skill);
        }
    }

    public int getModifier(Skill skill, Character c)
    {
        Optional<Integer> skillModifier = c.getTemplate().getSkillSet().getModifier(skill);
        if (skillModifier.isPresent())
            return skillModifier.get();
        else
            return c.getModifier(skill.getAbility());
    }

    public Collection<Skill> getSkillList()
    {
        return list.values();
    }
}
