package pathfinder.comps;

import java.util.Comparator;

import pathfinder.Character;

public class InitiativeComparator implements Comparator<Character>
{
	@Override
	public int compare(Character c1, Character c2)
	{
		if (c1 == c2)
			return 0;
		else if (c1.getInitiativeRoll() < c2.getInitiativeRoll())
			return 1;
		else if (c1.getInitiativeRoll() > c2.getInitiativeRoll())
			return -1;
		else if (c1.getInitiativeModifier() < c2.getInitiativeModifier())
			return 1;
		else if (c1.getInitiativeModifier() > c2.getInitiativeModifier())
			return -1;
		else if (c1.getRandomModifier() < c2.getRandomModifier())
			return 1;
		else if (c1.getRandomModifier() > c2.getRandomModifier())
			return -1;
		else
			return 0;
	}
}
