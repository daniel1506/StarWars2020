package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class JediCommander extends BaseHullMod {

	public static final float CR_LOSS_PER_SECOND_PERCENT = 25f;
	//public static final float PEAK_CR_DURATION_BONUS = 120f;
	public static final float MALFUNCTION_CHANCE_PERCENT = 0.5f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getCRLossPerSecondPercent().modifyFlat(id, 0f - CR_LOSS_PER_SECOND_PERCENT);
		stats.getCRLossPerSecondPercent().modifyMult(id, 1f - CR_LOSS_PER_SECOND_PERCENT / 100f);
		//stats.getPeakCRDuration().modifyFlat(id, PEAK_CR_DURATION_BONUS);
		stats.getCriticalMalfunctionChance().modifyMult(id, MALFUNCTION_CHANCE_PERCENT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) CR_LOSS_PER_SECOND_PERCENT;
		//if (index == 1) return "" + (int) PEAK_CR_DURATION_BONUS;
		if (index == 1) return "" + (int) (MALFUNCTION_CHANCE_PERCENT * 100f);
		return null;
	}

	//public boolean isApplicableToShip(ShipAPI ship) {
	//	return ship != null && ship.getShield() != null;
	//}
	
	//public String getUnapplicableReason(ShipAPI ship) {
	//	return "Ship has no shields";
	//}
}
