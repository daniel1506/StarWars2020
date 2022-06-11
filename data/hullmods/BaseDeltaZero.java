package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

public class BaseDeltaZero extends BaseHullMod {

	public static final float GROUND_BONUS = 100f;
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FIGHTER, 0f);
		mag.put(HullSize.FRIGATE, 0f);
		mag.put(HullSize.DESTROYER, 50f);
		mag.put(HullSize.CRUISER, 100f);
		mag.put(HullSize.CAPITAL_SHIP, 150f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getMod(Stats.FLEET_GROUND_SUPPORT).modifyFlat(id, (Float) mag.get(hullSize));
		stats.getDynamic().getMod(Stats.FLEET_BOMBARD_COST_REDUCTION).modifyFlat(id, GROUND_BONUS);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 3) return "" + (int) GROUND_BONUS;	
		return null;
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ((ship.getHullSize() == HullSize.CAPITAL_SHIP || ship.getHullSize() == HullSize.CRUISER ||
				ship.getHullSize() == HullSize.DESTROYER) && !ship.getVariant().hasHullMod(HullMods.CIVGRADE));
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getHullSize() != HullSize.CAPITAL_SHIP && ship.getHullSize() != HullSize.CRUISER && ship.getHullSize() != HullSize.DESTROYER) {
			return "Can only be installed on destroyers, cruisers and capital ships";
		}
		if (ship.getVariant().hasHullMod(HullMods.CIVGRADE)) {
			return "Can not be installed on civilian-grade hulls";
		}
		return null;
	}
}




