package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class RedundantShieldGenerators extends BaseHullMod {

	public static final int FLUX_CAPACITY_BOUNS = 10;
	public static final int OVERLOAD_MULT = 10;
	//public static final float SHIELD_BONUS = 20f;
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 5f);
		mag.put(HullSize.DESTROYER, 10f);
		mag.put(HullSize.CRUISER, 15f);
		mag.put(HullSize.CAPITAL_SHIP, 20f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		boolean sMod = isSMod(stats);
		if (sMod) {
			stats.getOverloadTimeMod().modifyMult(id, 1f + OVERLOAD_MULT * 0.01f);
		}
		stats.getShieldDamageTakenMult().modifyMult(id, 1f - (Float) mag.get(hullSize) * 0.01f);
		stats.getFluxCapacity().modifyMult(id, 1f + (float) FLUX_CAPACITY_BOUNS * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) SHIELD_BONUS + "%";
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue() + "%";
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		if (index == 4) return "" + FLUX_CAPACITY_BOUNS + "%";
		return null;
	}
	
	@Override
	public String getSModDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) OVERLOAD_MULT + "%";
		return null;
	}
	
	@Override
	public boolean isSModEffectAPenalty() {
		return true;
	}

	//public boolean isApplicableToShip(ShipAPI ship) {
	//	return ship != null && ship.getShield() != null;
	//}
	
	//public String getUnapplicableReason(ShipAPI ship) {
	//	return "Ship has no shields";
	//}
}
