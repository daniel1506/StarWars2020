package data.hullmods;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.util.IntervalUtil;

public class DeflectorShield extends BaseHullMod {

	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
	private boolean shieldRecharging;
	private float sinceLast = 0f;
	private float time;
	public static final int HARD_FLUX_DISSIPATION_PERCENT = 20;
	//public static final float SHIELD_BONUS = 20f;
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
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getShieldDamageTakenMult().modifyMult(id, 1f - (Float) mag.get(hullSize) * 0.01f);
		if (hullSize != HullSize.FIGHTER) {
			stats.getHardFluxDissipationFraction().modifyFlat(id, (float) HARD_FLUX_DISSIPATION_PERCENT * 0.01f);
		}		
	}
	
	public void advanceInCombat(ShipAPI ship, float amount) {
		//if (!ship.isAlive()) return;
		tracker.advance(amount);		
		
		if (ship.getFluxTracker().isOverloaded() && !shieldRecharging && !ship.isDefenseDisabled() && ship.isAlive()) {
			time = ship.getFluxTracker().getOverloadTimeRemaining() * ((Float) mag.get(ship.getHullSize()));
			if (ship.getShield() != null) {
				ship.setDefenseDisabled(true);
				ship.getFluxTracker().stopOverload();
				shieldRecharging = true;				
			}
			else if (ship.getPhaseCloak() != null) {
				if (deflectorShieldList.contains(ship.getPhaseCloak().getId())) {				
					//ship.getPhaseCloak().setCooldown(time);
					ship.getPhaseCloak().deactivate();
					//ship.getPhaseCloak().setCooldownRemaining(time);
					ship.setDefenseDisabled(true);
					ship.getFluxTracker().stopOverload();
					shieldRecharging = true;
				}				
			}		
			//if (ship.getFluxLevel() > 0.8f) {
			//	ship.getFluxTracker().decreaseFlux(ship.getMaxFlux() / 5f);
			//}
		}
		if (shieldRecharging) {
			sinceLast += amount;
			if (sinceLast >= time) {
				ship.setDefenseDisabled(false);
				sinceLast = 0f;
				time = 0f;				
				shieldRecharging = false;
			}			
		}
	}
	
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
