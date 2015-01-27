package pathfinder;

import pathfinder.enums.Ability;

public final class Skill
{
    private int id;
    private String name;
    private Ability ability;
    private boolean trained;

    public Skill(int id, String name, Ability ability, boolean trained)
    {
        this.id = id;
        this.name = name;
        this.ability = ability;
        this.trained = trained;
    }

    public int getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Ability getAbility()
    {
        return ability;
    }

    public boolean getTrained()
    {
        return trained;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
