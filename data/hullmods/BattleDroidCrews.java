package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class BattleDroidCrews extends BaseHullMod {

	private static final float CREW_MULT = 0f;
	private static final float MAL_MULT = 1.25f;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getMinCrewMod().modifyMult(id, CREW_MULT);
		stats.getCriticalMalfunctionChance().modifyMult(id, MAL_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round((1f - CREW_MULT) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round((1f - MAL_MULT) * 100f) + "%";
		return null;
	}


}
