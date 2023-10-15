package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class EscapePod extends BaseHullMod {

	private static final float LOSS_MULT = 0.20f;
	private static final float LOSS_MULT_SMOD = 0.50f;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		boolean sMod = isSMod(stats);
		
		stats.getCrewLossMult().modifyMult(id, sMod ? LOSS_MULT_SMOD : LOSS_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round((1f - LOSS_MULT) * 100f) + "%";
		return null;
	}
	
	public String getSModDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round((1f - LOSS_MULT_SMOD) * 100f) + "%";
		return null;
	}

}
