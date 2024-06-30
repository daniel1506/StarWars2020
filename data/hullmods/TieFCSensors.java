package data.hullmods;

import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import org.lazywizard.lazylib.combat.CombatUtils;

public class TieFCSensors extends BaseHullMod {

	private static final float SENSOR_RANGE = 3000f;
	private static final float AUTOFIRE_BONUS = 0.1f;
	
	private List<ShipAPI> shipList = new ArrayList<ShipAPI>();
	
	private String statsId = null;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSightRadiusMod().modifyFlat(id, SENSOR_RANGE);
		this.statsId = id;
	}
	
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (!ship.isAlive() && this.shipList.isEmpty() || this.statsId == null) {
			return;
		}
		if (!ship.isAlive()) {
			for (ShipAPI s : this.shipList) {
				s.getMutableStats().getAutofireAimAccuracy().unmodify(this.statsId);
			}
			this.shipList.clear();
		}	
		for (ShipAPI s : CombatUtils.getShipsWithinRange(ship.getLocation(), 2000f)) {
			if (s.getOwner() == ship.getOwner() && s.isAlive() && !this.shipList.contains(s)) {
				s.getMutableStats().getAutofireAimAccuracy().modifyFlat(this.statsId, AUTOFIRE_BONUS);
				this.shipList.add(s);
			}
		}
		List<ShipAPI> removeList = new ArrayList<ShipAPI>();
		for (ShipAPI s : this.shipList) {
			if (!s.isAlive()) {
				removeList.add(s);
			}
		}
		this.shipList.removeAll(removeList);
	}		
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) SENSOR_RANGE;
		if (index == 1) return "" + (int) (AUTOFIRE_BONUS * 10f) + "%";
		return null;
	}

}
