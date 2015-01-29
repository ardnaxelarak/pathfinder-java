package pathfinder;

/* local package imports */
import pathfinder.Skill;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

public class SkillSet
{
    private TreeMap<Integer, Integer> list;

    public SkillSet(ResultSet set) throws SQLException
    {
        list = new TreeMap<Integer, Integer>();
        int skill, modifier;
        while (set.next())
        {
            skill = set.getInt("skill");
            modifier = set.getInt("modifier");
            list.put(skill, modifier);
        }
    }

    public Optional<Integer> getModifier(int skillnum)
    {
        return Optional.fromNullable(list.get(skillnum));
    }

    public Optional<Integer> getModifier(Skill s)
    {
        return getModifier(s.getID());
    }
}
