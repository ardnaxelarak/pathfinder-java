package pathfinder.event;

import pathfinder.Character;

public final class DamageEvent
{
    private final Character character;
    private final int amount;

    public DamageEvent(Character character, int amount)
    {
        this.character = character;
        this.amount = amount;
    }

    public Character getCharacter()
    {
        return character;
    }

    public int getAmount()
    {
        return amount;
    }
}
