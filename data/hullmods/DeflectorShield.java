package data.hullmods;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.util.IntervalUtil;

//import org.apache.log4j.Logger;

public class DeflectorShield extends BaseHullMod {

	//public Logger log = Logger.getLogger(this.getClass());
				
	public static final int HARD_FLUX_DISSIPATION_PERCENT = 20;
	private static List<String> deflectorShieldList = new ArrayList<String>();
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FIGHTER, 1.2f);
		mag.put(HullSize.FRIGATE, 1.25f);
		mag.put(HullSize.DESTROYER, 1.5f);
		mag.put(HullSize.CRUISER, 1.75f);
		mag.put(HullSize.CAPITAL_SHIP, 2f);
		deflectorShieldList.add("sw_deflectorshield");
		deflectorShieldList.add("sw_deflectorshield_rebel");
		deflectorShieldList.add("sw_deflectorshield_cis");
		deflectorShieldList.add("sw_deflectorshield_zann");	
		deflectorShieldList.add("sw_deflectorshield_chiss");		
	}
	
	public class DeflectorListenerScript implements AdvanceableListener {
		private ShipAPI ship;
		private FluxTrackerAPI flux;
		private float time;
		private boolean shieldRecharging = false;
		private IntervalUtil tracker = new IntervalUtil(20f, 20f);
		
		public DeflectorListenerScript(ShipAPI ship) {
			this.ship = ship;
			this.flux = ship.getFluxTracker();
		}
		
		public void advance(float amount) {
			if (ship.getFluxTracker().isOverloaded() && !shieldRecharging && !ship.isDefenseDisabled() && ship.isAlive()) {
				time = ship.getFluxTracker().getOverloadTimeRemaining() * ((Float) mag.get(ship.getHullSize()));
				tracker.setInterval(time, time);
				if (ship.getShield() != null) {
					ship.setDefenseDisabled(true);
					ship.getFluxTracker().stopOverload();		
					shieldRecharging = true;			
				}
				else if (ship.getPhaseCloak() != null) {
					if (deflectorShieldList.contains(ship.getPhaseCloak().getId())) {				
						ship.getPhaseCloak().deactivate();
						ship.setDefenseDisabled(true);
						ship.getFluxTracker().stopOverload();
						shieldRecharging = true;
					}				
				}		
			}
			if (shieldRecharging) {
				tracker.advance(amount);
				if (tracker.intervalElapsed() || !ship.isAlive()) {				
					ship.setDefenseDisabled(false);
					time = 0f;		
					shieldRecharging = false;
				} else {
					if (ship == Global.getCombatEngine().getPlayerShip()) {
						Global.getCombatEngine().maintainStatusForPlayerShip("SWDeflectorSystem1", "graphics/icons/hullsys/damper_field.png", "SHIELD OVERLOADED", "RECHARGE TIME: " + String.format("%.1f", (time - tracker.getElapsed())) + " SEC", true);
					}
				}			
			}
		}
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getShieldDamageTakenMult().modifyMult(id, 1f - (Float) mag.get(hullSize) * 0.01f);
		if (hullSize != HullSize.FIGHTER) {
			stats.getHardFluxDissipationFraction().modifyFlat(id, (float) HARD_FLUX_DISSIPATION_PERCENT * 0.01f);
		}
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			DeflectorListenerScript DL = new DeflectorListenerScript(ship);
			if (!ship.hasListener(DL)) {				
				ship.addListener(DL);
			}			
		}
	}
	
	//public void advanceInCombat(ShipAPI ship, float amount) {								
	//}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) (((Float) mag.get(HullSize.FRIGATE) - 1f) * 100f) + "%";
		if (index == 1) return "" + (int) (((Float) mag.get(HullSize.DESTROYER) - 1f) * 100f) + "%";
		if (index == 2) return "" + (int) (((Float) mag.get(HullSize.CRUISER) - 1f) * 100f) + "%";
		if (index == 3) return "" + (int) (((Float) mag.get(HullSize.CAPITAL_SHIP) - 1f) * 100f) + "%";
		if (index == 4) return "" + HARD_FLUX_DISSIPATION_PERCENT + "%";				
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && (ship.getShield() != null || (ship.getPhaseCloak() != null && deflectorShieldList.contains(ship.getPhaseCloak().getId())));
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shield";
	}
}
