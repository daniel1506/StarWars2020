package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class DeflectorShield extends BaseHullMod {

	public static final int HARD_FLUX_DISSIPATION_PERCENT = 25;
	public static final float SHIELD_BONUS = 20f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * 0.01f);
		stats.getHardFluxDissipationFraction().modifyFlat(id, (float)HARD_FLUX_DISSIPATION_PERCENT * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_BONUS + "%";
		if (index == 1) return "" + HARD_FLUX_DISSIPATION_PERCENT + "%";
		return null;
	}

	//public boolean isApplicableToShip(ShipAPI ship) {
	//	return ship != null && ship.getShield() != null;
	//}
	
	//public String getUnapplicableReason(ShipAPI ship) {
	//	return "Ship has no shields";
	//}
}
