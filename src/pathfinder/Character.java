package pathfinder;

import pathfinder.dice.DiceRoll;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Character
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
	public Character(ResultSet set) throws SQLException
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

	public String getURL()
	{
		return url;
	}

	public DiceRoll getHP()
	{
		return hp;
	}
}
