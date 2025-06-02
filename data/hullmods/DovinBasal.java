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
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.util.IntervalUtil;

//import org.apache.log4j.Logger;

public class DovinBasal extends BaseHullMod {

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
	}
	
	public class DovinBasalListenerScript implements DamageTakenModifier {
		private ShipAPI ship;
		private FluxTrackerAPI flux;
		private MutableShipStatsAPI shipStats;
		private CombatEngineAPI engine;
		
		public DovinBasalListenerScript(ShipAPI ship) {
			this.ship = ship;
			this.shipStats = ship.getMutableStats();
			this.flux = ship.getFluxTracker();
			this.engine = Global.getCombatEngine();
		}
		
		public String modifyDamageTaken(java.lang.Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			if (!ship.getShield().isActive()) {
				return "Off";
			}
			else if (target instanceof ShipAPI && (ShipAPI) target == this.ship) {
				float damageAmount = damage.getDamage();				
				if (shieldHit) {
					damageAmount * damage.getType().getShieldMult() * shipStats.getShieldDamageTakenMult().getModifiedValue() * ship.getHullSpec().getShieldSpec().getFluxPerDamageAbsorbed();
				}
				if (damage.isDps()) {
					if (param instanceof BeamAPI) {
						BeamAPI beam = (BeamAPI) param;
						engine.addFloatingDamageText(point, damageAmount * damage.getDpsDuration(), TEXT_COLOR, target, beam.getSource());
						flux.increaseFlux(damageAmount * damage.getDpsDuration(), false);
						// if ((flux.getCurrFlux() + (damageAmount * damage.getDpsDuration())) >= flux.getMaxFlux()) {
						// 	flux.increaseFlux(damageAmount * damage.getDpsDuration(), false);
						// 	flux.forceOverload(0f);
						// }
						// else {
						// 	flux.increaseFlux(damageAmount * damage.getDpsDuration(), false);
						// }
					}
					else {
						flux.increaseFlux(damageAmount * damage.getDpsDuration(), true);
						if (param instanceof DamagingProjectileAPI) {
							DamagingProjectileAPI proj = (DamagingProjectileAPI) param;
							engine.addFloatingDamageText(point, damageAmount * damage.getDpsDuration(), TEXT_COLOR, target, proj.getSource());
						}
					}
				}
				else {
					flux.increaseFlux(damageAmount, !(param instanceof BeamAPI));
					if (param instanceof DamagingProjectileAPI) {
						DamagingProjectileAPI proj = (DamagingProjectileAPI) param;
						engine.addFloatingDamageText(point, damageAmount, TEXT_COLOR, target, proj.getSource());
					}
					else if (param instanceof BeamAPI) {
						BeamAPI beam = (BeamAPI) param;
						engine.addFloatingDamageText(point, damageAmount, TEXT_COLOR, target, beam.getSource());
						//flux.increaseFlux(damageAmount, false);
					}
				}
				return "On";
			}
			return "Bug";
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
