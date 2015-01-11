package pathfinder;

import pathfinder.dice.DiceRoll;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterTemplate
{
	private int id, strength, dexterity, constitution, intelligence, wisdom,
				charisma, initiative_modifier, speed, fly_speed, swim_speed,
				climb_speed, burrow_speed, space, reach, ac, touch_ac,
				flat_footed_ac, fast_healing, regeneration, fort, ref,
				will, dr, sr;
	private String name, base_attack_bonus, fly_maneuver, url,
				regeneration_bypass, dr_bypass;
	private DiceRoll hp;
	private boolean ferocity;
	private double cr;
	public CharacterTemplate(ResultSet set) throws SQLException
	{
		set.next();
		id = set.getInt("id");
		name = set.getString("name");
		cr = set.getDouble("cr");
		strength = set.getInt("strength");
		dexterity = set.getInt("dexterity");
		constitution = set.getInt("constitution");
		intelligence = set.getInt("intelligence");
		wisdom = set.getInt("wisdom");
		charisma = set.getInt("charisma");
		base_attack_bonus = set.getString("base_attack_bonus");
		initiative_modifier = set.getInt("initiative");
		speed = set.getInt("speed");
		fly_speed = set.getInt("fly_speed");
		fly_maneuver = set.getString("fly_maneuver");
		swim_speed = set.getInt("swim_speed");
		climb_speed = set.getInt("climb_speed");
		burrow_speed = set.getInt("burrow_speed");
		space = set.getInt("space");
		reach = set.getInt("reach");
		ac = set.getInt("ac");
		touch_ac = set.getInt("touch_ac");
		flat_footed_ac = set.getInt("flat_footed_ac");
		hp = Functions.parseDiceRoll(set.getString("hp"));
		ferocity = set.getBoolean("ferocity");
		fast_healing = set.getInt("fast_healing");
		regeneration = set.getInt("regeneration");
		regeneration_bypass = set.getString("regeneration_bypass");
		fort = set.getInt("fort");
		ref = set.getInt("ref");
		will = set.getInt("will");
		dr = set.getInt("dr");
		dr_bypass = set.getString("dr_bypass");
		sr = set.getInt("sr");
		url = set.getString("url");
	}

	public int getID()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public double getCR()
	{
		return cr;
	}

	public int getSTR()
	{
		return strength;
	}

	public int getDEX()
	{
		return dexterity;
	}

	public int getCON()
	{
		return constitution;
	}

	public int getINT()
	{
		return intelligence;
	}

	public int getWIS()
	{
		return wisdom;
	}

	public int getCHA()
	{
		return charisma;
	}

	public String getBAB()
	{
		return base_attack_bonus;
	}

	public int getInitiativeModifier()
	{
		return initiative_modifier;
	}

	public int getLandSpeed()
	{
		return speed;
	}

	public int getFlySpeed()
	{
		return fly_speed;
	}

	public String getFlyManeuverability()
	{
		return fly_maneuver;
	}

	public int getSwimSpeed()
	{
		return swim_speed;
	}

	public int getClimbSpeed()
	{
		return climb_speed;
	}

	public int getBurrowSpeed()
	{
		return burrow_speed;
	}

	public int getSpace()
	{
		return space;
	}

	public int getReach()
	{
		return reach;
	}

	public int getAC()
	{
		return ac;
	}

	public int getTouchAC()
	{
		return touch_ac;
	}

	public int getFlatFootedAC()
	{
		return flat_footed_ac;
	}

	public DiceRoll getHP()
	{
		return hp;
	}

	public boolean hasFerocity()
	{
		return ferocity;
	}

	public int getFastHealing()
	{
		return fast_healing;
	}

	public int getRegeneration()
	{
		return regeneration;
	}

	public String getRegenerationBypass()
	{
		return regeneration_bypass;
	}

	public int getFortSave()
	{
		return fort;
	}

	public int getRefSave()
	{
		return ref;
	}

	public int getWillSave()
	{
		return will;
	}

	public int getDR()
	{
		return dr;
	}

	public String getDRBypass()
	{
		return dr_bypass;
	}

	public int getSR()
	{
		return sr;
	}

	public String getURL()
	{
		return url;
	}
}
