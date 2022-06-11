package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class TibannaGasCoolant extends BaseHullMod {

	private static final float FLUX_REDUCTION = 25f;
	//private static final int FLUX_DISSIPATION_BONUS = 500;
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 200f);
		mag.put(HullSize.DESTROYER, 300f);
		mag.put(HullSize.CRUISER, 400f);
		mag.put(HullSize.CAPITAL_SHIP, 500f);
	}

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponFluxCostMod().modifyPercent(id, -FLUX_REDUCTION);
		stats.getFluxDissipation().modifyFlat(id, (Float) mag.get(hullSize));
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) FLUX_REDUCTION + "%";
		//if (index == 1) return "" + FLUX_DISSIPATION_BONUS;
		if (index == 1) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 4) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		return null;
	}


}
