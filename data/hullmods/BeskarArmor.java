package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class BeskarArmor extends BaseHullMod {

	public static final float ARMOR_BOUNS = 20f;
	public static float SMOD_MANEUVER_PENALTY = 10f;	
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 200f);
		mag.put(HullSize.DESTROYER, 300f);
		mag.put(HullSize.CRUISER, 400f);
		mag.put(HullSize.CAPITAL_SHIP, 500f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		boolean sMod = isSMod(stats);
		
		stats.getArmorBonus().modifyFlat(id, (Float) mag.get(hullSize));
		stats.getArmorDamageTakenMult().modifyMult(id, 1f - ARMOR_BOUNS * 0.01f);
		
		if (sMod) {
			stats.getAcceleration().modifyMult(id, 1f - SMOD_MANEUVER_PENALTY * 0.01f);
			stats.getDeceleration().modifyMult(id, 1f - SMOD_MANEUVER_PENALTY * 0.01f);
			stats.getTurnAcceleration().modifyMult(id, 1f - SMOD_MANEUVER_PENALTY * 0.01f);
			stats.getMaxTurnRate().modifyMult(id, 1f - SMOD_MANEUVER_PENALTY * 0.01f);
		}
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 4) return "" + (int) ARMOR_BOUNS + "%";
		return null;
	}
	
	@Override
	public String getSModDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) SMOD_MANEUVER_PENALTY + "%";
		return null;
	}
	
	@Override
	public boolean isSModEffectAPenalty() {
		return true;
	}

	public boolean isApplicableToShip(ShipAPI ship) {		
		return true;
	}
}
