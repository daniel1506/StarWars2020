package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class BattleDroidCrews extends BaseHullMod {

	private static final float CREW_MULT = 0.8f;
	private static final float CREW_MULT_SMOD = 0f;
	private static final float MAL_MULT = 1.25f;
	private static final float MAL_MULT_SMOD = 1.10f;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		boolean sMod = isSMod(stats);
		
		stats.getMinCrewMod().modifyMult(id, sMod ? CREW_MULT_SMOD : CREW_MULT);
		stats.getCriticalMalfunctionChance().modifyMult(id, sMod ? MAL_MULT_SMOD : MAL_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round((1f - CREW_MULT) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round((MAL_MULT - 1f) * 100f) + "%";
		return null;
	}
	
	public String getSModDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round((1f - CREW_MULT_SMOD) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round((MAL_MULT_SMOD - 1f) * 100f) + "%";
		return null;
	}


}
