package data.hullmods;

import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import org.lazywizard.lazylib.combat.CombatUtils;

public class HangerSystem extends BaseHullMod {

	private int hangerReserve = 30;
	
	// private List<ShipAPI> shipList = new ArrayList<ShipAPI>();
	
	//private String statsId = null;
	
	//public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
	//	this.statsId = id;
	//}
	
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (!ship.isAlive() || hangerReserve == 0) {
			return;
		}
		for (FighterLaunchBayAPI l : ship.getLaunchBaysCopy()) {
			if (hangerReserve == 0) {
				if (l.getTimeUntilNextReplacement() < 60f) {
					l.setExtraDuration(60f);
				}
				break;
			}
			if (l.getWing() == null) {
				continue;
			}
			int size = l.getWing().getSpec().getNumFighters();
			int currentSize = l.getWing().getWingMembers().size();
			if (currentSize < size) {
				hangerReserve = hangerReserve - (size - currentSize);
				l.setFastReplacements(size - currentSize);			
			}
		}
	}

	
	// public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
	// 	if (index == 0) return "" + (int) SENSOR_RANGE;
	// 	if (index == 1) return "" + (int) (AUTOFIRE_BONUS * 10f) + "%";
	// 	return null;
	// }
	
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getLaunchBaysCopy() != null && ship.getLaunchBaysCopy().size() > 0;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no hanger";
	}

}
