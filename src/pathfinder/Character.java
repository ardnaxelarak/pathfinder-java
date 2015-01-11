package pathfinder;

import static pathfinder.Functions.log;
import static pathfinder.Functions.roll;
import pathfinder.CharacterTemplate;
import pathfinder.dice.DiceRoll;
import pathfinder.enums.Status;

import java.util.LinkedList;

public class Character
{
	private CharacterTemplate template;
	private int maxHP, damage, initiativeRoll;
	private boolean regenBlocked;
	private LinkedList<Condition> conditions;
	private SkillSet skills;
	private Status status;
	private String name;
	public Character(CharacterTemplate template)
	{
		this.template = template;
		maxHP = template.getHP().roll();
		damage = 0;
		regenBlocked = false;
		status = Status.NORMAL;
		conditions = new LinkedList<Condition>();
		skills = new SkillSet();
		name = template.getName();
	}

	public void reset()
	{
		damage = 0;
		regenBlocked = false;
		// clear conditions and temp effects
		status = Status.NORMAL;
	}

	public void rollInitiative()
	{
		initiativeRoll = roll() + template.getInitiativeModifier();
	}

	public void addCondition(Condition cond)
	{
		conditions.add(cond);
	}

	public void kill()
	{
		status = Status.DEAD;
	}

	public void startDying()
	{
		status = Status.DYING;
	}

	public void disable()
	{
		status = Status.DISABLED;
	}

	public void stabalize()
	{
		status = Status.STABLE;
	}

	public boolean makeDyingCheck()
	{
		if (status != Status.DYING)
			return false;
		int value = roll();
		if (value == 20)
		{
			stabalize();
			return true;
		}
		value += getCONModifier() + getCurrentHP();
		if (value >= 10)
		{
			stabalize();
			return true;
		}
		else
		{
			takeDamage(1, true, false);
			return false;
		}
	}

	public void takeDamage(int amount, boolean bypassDR, boolean suppressRegen)
	{
		if (status == Status.DEAD)
			return;
		if (amount <= 0)
			return;
		if (template.getDR() > 0 && !bypassDR)
		{
			amount -= template.getDR();
			if (amount < 0)
				amount = 0;
			log("DR reduces damage to %d.", amount);
			if (amount == 0)
				return;
		}
		if (template.getRegeneration() > 0 && suppressRegen && !regenBlocked)
		{
			regenBlocked = true;
			log("Regeneration is suppressed for %s", name);
		}
		if (getCurrentHP() - amount <= -template.getCON() && (template.getRegeneration() <= 0 || regenBlocked))
		{
			kill();
			log("%s is dead", name);
		}
		else if (getCurrentHP() >= 0 && getCurrentHP() < amount)
		{
			if (template.hasFerocity())
			{
				// addCondition(staggered);
				log("%s is at negative hitpoints and staggered", name);
			}
			else
			{
				startDying();
				log("%s is dying", name);
			}
		}
		else if (getCurrentHP() == amount)
		{
			disable();
			log("%s is disabled", name);
		}
		damage += amount;
	}

	public void heal(int amount)
	{
		if (amount > damage)
			amount = damage;
		if (amount <= 0)
			return;
		damage -= amount;
		log("%s heals %d", name, amount);
	}

	public void heal(String amount)
	{
		int num = Functions.parseDiceRoll(amount).roll();
		heal(num);
	}

	public void applyHealing()
	{
		if (template.getFastHealing() > 0)
		{
			heal(template.getFastHealing());
		}
		if (regenBlocked)
		{
			regenBlocked = false;
			log("%s resumes regeneration", name);
		}
		else if (template.getRegeneration() > 0)
		{
			heal(template.getRegeneration());
		}
	}

	public int getSTRModifier()
	{
		return template.getSTR() / 2 - 5;
	}

	public int getDEXModifier()
	{
		return template.getDEX() / 2 - 5;
	}

	public int getCONModifier()
	{
		return template.getCON() / 2 - 5;
	}

	public int getINTModifier()
	{
		return template.getINT() / 2 - 5;
	}

	public int getWISModifier()
	{
		return template.getWIS() / 2 - 5;
	}

	public int getCHAModifier()
	{
		return template.getCHA() / 2 - 5;
	}

	public int getMaxHP()
	{
		return maxHP;
	}

	public int getCurrentHP()
	{
		return maxHP - damage;
	}

	public int getDamage()
	{
		return damage;
	}
}
