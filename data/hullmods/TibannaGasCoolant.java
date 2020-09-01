package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class TibannaGasCoolant extends BaseHullMod {

	private static final float FLUX_REDUCTION = 50f;
	private static final int FLUX_DISSIPATION_BONUS = 500;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -FLUX_REDUCTION);
		stats.getFluxDissipation().modifyFlat(id, FLUX_DISSIPATION_BONUS);

	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) FLUX_REDUCTION + "%";
		if (index == 1) return "" + FLUX_DISSIPATION_BONUS;
		return null;
	}


}
