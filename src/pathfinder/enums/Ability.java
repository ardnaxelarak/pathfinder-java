package pathfinder.enums;

public enum Ability
{
	STR ("Strength"),
    DEX ("Dexterity"),
    CON ("Constitution"),
    INT ("Intelligence"),
    WIS ("Wisdom"),
    CHA ("Charisma"),
    ERROR ("Error");

	private final String name;

	Ability(String name)
	{
        this.name = name;
	}

    public String getName()
    {
        return name;
    }

    public static Ability getAbility(String shortname)
    {
        try
        {
            return valueOf(shortname);
        }
        catch (IllegalArgumentException ex)
        {
            System.err.printf("Ability \"%s\" is invalid.\n", shortname);
            return ERROR;
        }
    }
}
