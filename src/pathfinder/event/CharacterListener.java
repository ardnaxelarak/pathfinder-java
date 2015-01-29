package pathfinder.event;

import pathfinder.Character;
import pathfinder.event.DamageEvent;

public interface CharacterListener
{
    public void initiativeModified(Character c);

    public void nameChanged(Character c);

    public void statusChanged(Character c);

    public void characterDamaged(DamageEvent e);
}
