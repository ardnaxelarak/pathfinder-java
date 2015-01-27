package pathfinder;

/* local package imports */
import pathfinder.CharacterTemplate;
import pathfinder.enums.Ability;
import pathfinder.enums.Status;
import pathfinder.event.CharacterListener;
import pathfinder.event.DamageEvent;

/* java package imports */
import java.util.LinkedList;

public class Character
{
    private CharacterTemplate template;
    private int maxHP, damage, initiativeRoll;
    private double randomModifier;
    private boolean regenBlocked, isPC;
    private LinkedList<Condition> conditions;
//  private SkillSet skills;
    private Status status;
    private String name;
    private LinkedList<CharacterListener> listeners;

    public Character(CharacterTemplate template)
    {
        this(template, template.getName());
    }

    public Character(CharacterTemplate template, String name)
    {
        this.template = template;
        maxHP = Functions.roll(template.getHP());
        damage = 0;
        regenBlocked = false;
        status = Status.NORMAL;
        conditions = new LinkedList<Condition>();
//      skills = new SkillSet();
        this.name = name;
        this.isPC = false;
        initiativeRoll = Integer.MIN_VALUE;
        randomModifier = Functions.random();
        listeners = new LinkedList<CharacterListener>();
    }

    public void addListener(CharacterListener l)
    {
        listeners.add(l);
    }

    public void removeListener(CharacterListener l)
    {
        listeners.remove(l);
    }

    public void reset()
    {
        damage = 0;
        regenBlocked = false;
        // clear conditions and temp effects
        status = Status.NORMAL;
    }

    public boolean isPC()
    {
        return isPC;
    }

    public void setPC()
    {
        isPC = true;
    }

    public void setNotPC()
    {
        isPC = false;
    }

    public void rollInitiative()
    {
        setInitiativeRoll(Functions.roll() + template.getInitiativeModifier());
    }

    public void addCondition(Condition cond)
    {
        conditions.add(cond);
    }

    /* Status setters */
    private void setStatus(Status status)
    {
        boolean changed = (this.status != status);
        this.status = status;
        if (changed)
        {
            for (CharacterListener listener : listeners)
                listener.statusChanged(this);
        }
    }

    public void kill()
    {
        setStatus(Status.DEAD);
    }

    public void startDying()
    {
        setStatus(Status.DYING);
    }

    public void disable()
    {
        setStatus(Status.DISABLED);
    }

    public void stabalize()
    {
        setStatus(Status.STABLE);
    }

    public boolean makeDyingCheck()
    {
        if (status != Status.DYING)
            return false;
        int value = Functions.roll();
        if (value == 20)
        {
            stabalize();
            return true;
        }
        value += getModifier(Ability.CON) + getCurrentHP();
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
            Functions.log("DR reduces damage to %d.", amount);
            if (amount == 0)
                return;
        }
        if (template.getRegeneration() > 0 && suppressRegen && !regenBlocked)
        {
            regenBlocked = true;
            Functions.log("Regeneration is suppressed for %s", name);
        }
        if (getCurrentHP() - amount <= -template.getCON() && (template.getRegeneration() <= 0 || regenBlocked))
        {
            kill();
            Functions.log("%s is dead", name);
        }
        else if (getCurrentHP() >= 0 && getCurrentHP() < amount)
        {
            if (template.hasFerocity())
            {
                // addCondition(staggered);
                Functions.log("%s is at negative hitpoints and staggered", name);
            }
            else
            {
                startDying();
                Functions.log("%s is dying", name);
            }
        }
        else if (getCurrentHP() == amount)
        {
            disable();
            Functions.log("%s is disabled", name);
        }
        damage += amount;
        DamageEvent e = new DamageEvent(this, amount);
        for (CharacterListener cl : listeners)
            cl.characterDamaged(e);
    }

    public void heal(int amount)
    {
        if (amount > damage)
            amount = damage;
        if (amount <= 0)
            return;
        damage -= amount;
        Functions.log("%s heals %d", name, amount);
        DamageEvent e = new DamageEvent(this, -amount);
        for (CharacterListener cl : listeners)
            cl.characterDamaged(e);
    }

    public void heal(String amount)
    {
        int num = Functions.roll(amount);
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
            Functions.log("%s resumes regeneration", name);
        }
        else if (template.getRegeneration() > 0)
        {
            heal(template.getRegeneration());
        }
    }

    public CharacterTemplate getTemplate()
    {
        return template;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        for (CharacterListener cl : listeners)
            cl.nameChanged(this);
    }

    public String getTemplateName()
    {
        return template.getName();
    }

    public int getAbility(Ability ability)
    {
        switch (ability)
        {
            case STR:
                return template.getSTR();
            case DEX:
                return template.getDEX();
            case CON:
                return template.getCON();
            case INT:
                return template.getINT();
            case WIS:
                return template.getWIS();
            case CHA:
                return template.getCHA();
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getModifier(Ability ability)
    {
        return getAbility(ability) / 2 - 5;
    }

    public int getMaxHP()
    {
        return maxHP;
    }

    public int getCurrentHP()
    {
        return maxHP - damage;
    }

    public int getInitiativeModifier()
    {
        return template.getInitiativeModifier();
    }

    public int getInitiativeRoll()
    {
        return initiativeRoll;
    }

    public void setInitiativeRoll(int roll)
    {
        initiativeRoll = roll;
        randomModifier = Functions.random();
        for (CharacterListener cl : listeners)
            cl.initiativeModified(this);
    }

    public double getRandomModifier()
    {
        return randomModifier;
    }

    public int getDamage()
    {
        return damage;
    }

    public Status getStatus()
    {
        return status;
    }

    public String toString()
    {
        return name;
    }
}
