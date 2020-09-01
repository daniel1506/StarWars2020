package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class Hyperdrive extends BaseHullMod {

	private static final float FUEL_MULT = 0.5f;
	private static final int BURN_LEVEL_BONUS = 1;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getFuelUseMod().modifyMult(id, FUEL_MULT);
		stats.getMaxBurnLevel().modifyFlat(id, BURN_LEVEL_BONUS);

	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round((1f - FUEL_MULT) * 100f) + "%";
		if (index == 1) return "" + BURN_LEVEL_BONUS;
		return null;
	}


}
