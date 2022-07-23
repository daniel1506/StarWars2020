package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

public class SFoilsStats extends BaseShipSystemScript {
	
	private boolean alreadyExecuted = false;

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {		
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 50f);
			stats.getAcceleration().modifyPercent(id, 200f * effectLevel);
			stats.getDeceleration().modifyPercent(id, 200f * effectLevel);
			stats.getTurnAcceleration().modifyFlat(id, 30f * effectLevel);
			stats.getTurnAcceleration().modifyPercent(id, 200f * effectLevel);
			stats.getMaxTurnRate().modifyFlat(id, 15f);
			stats.getMaxTurnRate().modifyPercent(id, 100f);	
		}
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship == Global.getCombatEngine().getPlayerShip()) {
				if (!alreadyExecuted) {
					Global.getSoundPlayer().playSound("sw_system_s_foils", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
					alreadyExecuted = true;
				}
			} else {
				if (!ship.getAIFlags().hasFlag(AIFlags.BACKING_OFF) && (ship.getAIFlags().hasFlag(AIFlags.IN_ATTACK_RUN) || ship.areAnyEnemiesInRange())) {
					ship.getSystem().deactivate();
				}
			}
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship == Global.getCombatEngine().getPlayerShip() && alreadyExecuted && ship.isAlive()) {
				Global.getSoundPlayer().playSound("sw_system_s_foils_deactivate", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());				
			}
			alreadyExecuted = false;
		}
		stats.getMaxSpeed().unmodify(id);
		stats.getMaxTurnRate().unmodify(id);
		stats.getTurnAcceleration().unmodify(id);
		stats.getAcceleration().unmodify(id);
		stats.getDeceleration().unmodify(id);				
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("improved maneuverability", false);
		} else if (index == 1) {
			return new StatusData("+50 top speed", false);
		}
		return null;
	}
}
