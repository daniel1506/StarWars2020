package data.hullmods;

import java.awt.Color;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.IntervalUtil;

//import org.apache.log4j.Logger;

public class DovinBasal extends BaseHullMod {

	//public Logger log = Logger.getLogger(this.getClass());
	private static final Color JITTER_COLOR = new Color(255, 155, 50, 150);
	private static final Color JITTER_UNDER_COLOR = new Color(255, 155, 100, 50);
	private static final Color OVERLOAD_JITTER_COLOR = new Color(255, 205, 255, 150);
	private static final Color OVERLOAD_JITTER_UNDER_COLOR = new Color(205, 155, 255, 50);
	private static final int HARD_FLUX_DISSIPATION_PERCENT = 10;
	private static final float OVERWHALMTIME = 5f;
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FIGHTER, 0.8f);
		mag.put(HullSize.FRIGATE, 0.8f);
		mag.put(HullSize.DESTROYER, 0.7f);
		mag.put(HullSize.CRUISER, 0.6f);
		mag.put(HullSize.CAPITAL_SHIP, 0.5f);		
	}

	private IntervalUtil interval = new IntervalUtil(OVERWHALMTIME, OVERWHALMTIME);
	private boolean saturated = false;
	private String statsId;
	private DovinBasalListenerScript DBL;
	
	public class DovinBasalListenerScript implements DamageTakenModifier {
		private ShipAPI ship;
		private FluxTrackerAPI flux;
		private MutableShipStatsAPI shipStats;
		private CombatEngineAPI engine;
		private String id;
		public static final Color TEXT_COLOR = new Color(255,255,255,100);		
		
		public DovinBasalListenerScript(ShipAPI ship) {
			this.ship = ship;
			this.shipStats = ship.getMutableStats();
			this.flux = ship.getFluxTracker();
			this.engine = Global.getCombatEngine();
		}
		
		public String modifyDamageTaken(java.lang.Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			if (!ship.getShield().isOn()) {
				return "Off";
			}
			else if (target instanceof ShipAPI && (ShipAPI) target == this.ship) {	
				if (shieldHit) {
					return "shieldHit";
				}							
				if (!shieldHit) {
					interval.setElapsed(0f);
					saturated = true;
					float damageAmount = damage.getDamage() * damage.getType().getShieldMult() * shipStats.getShieldDamageTakenMult().getModifiedValue() * ship.getHullSpec().getShieldSpec().getFluxPerDamageAbsorbed();
					//log.info("d" + damage.getDamage());
					//log.info("D" + damageAmount);
					if (damage.isDps()) {
						if (param instanceof BeamAPI) {
							BeamAPI beam = (BeamAPI) param;						
							engine.addFloatingDamageText(point, damageAmount * damage.getDpsDuration(), TEXT_COLOR, target, beam.getSource());
							flux.increaseFlux(damageAmount * damage.getDpsDuration(), false);					
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
						}
					}
					return "On";
				}				
			}
			return "Bug";
		}
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (statsId != id) {
			statsId = id;
		}
		if (hullSize != HullSize.FIGHTER) {
			stats.getHardFluxDissipationFraction().modifyFlat(id, (float) HARD_FLUX_DISSIPATION_PERCENT * 0.01f);
		}
	}
	
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (ship.getHullSize() == HullSize.FIGHTER && ship.isAlive()) {
			ship.getMutableStats().getShieldDamageTakenMult().modifyMult(this.statsId, 0.1f);
			return;
		}
		interval.advance(amount);
		if (interval.intervalElapsed() && saturated) {
			saturated = false;
		}
		ship.getMutableStats().getMaxSpeed().modifyMult(this.statsId, (1f - ((Float) mag.get(ship.getHullSize()) * ship.getFluxTracker().getFluxLevel())));
		if (ship.getShield().isOn() && !saturated) {
			if (DBL == null || !ship.hasListener(DBL)) {
				DovinBasalListenerScript DBLScript = new DovinBasalListenerScript(ship);
				DBL = DBLScript;
				ship.addListener(DBL);
			}	
			ship.getMutableStats().getShieldDamageTakenMult().modifyMult(this.statsId, 0f);
			ship.getMutableStats().getProjectileDamageTakenMult().modifyMult(this.statsId, 0.0f);
			ship.getMutableStats().getBeamDamageTakenMult().modifyMult(this.statsId, 0.0f);
			ship.getMutableStats().getMissileDamageTakenMult().modifyMult(this.statsId, 0.0f);
			ship.getMutableStats().getEmpDamageTakenMult().modifyMult(this.statsId, 0.25f);
			ship.setJitter((Object) ship, JITTER_COLOR, 0.5f, 1, 5);
			ship.setJitterUnder((Object) ship, JITTER_COLOR, 1f, 25, 0f, 7f);
		}
		if (ship.getShield().isOn() && saturated) {
			ship.getMutableStats().getShieldDamageTakenMult().unmodify(this.statsId);
			ship.getMutableStats().getProjectileDamageTakenMult().modifyMult(this.statsId, 0.0f);
			ship.getMutableStats().getBeamDamageTakenMult().modifyMult(this.statsId, 0.0f);
			ship.getMutableStats().getMissileDamageTakenMult().modifyMult(this.statsId, 0.0f);
			ship.getMutableStats().getEmpDamageTakenMult().modifyMult(this.statsId, 0.25f);
			ship.setJitter((Object) ship, OVERLOAD_JITTER_COLOR, 0.5f, 1, 5);
			ship.setJitterUnder((Object) ship, OVERLOAD_JITTER_UNDER_COLOR, 1f, 25, 0f, 7f);
		} else if (!ship.getShield().isOn()) {
			ship.getMutableStats().getShieldDamageTakenMult().unmodify(this.statsId);
			ship.getMutableStats().getProjectileDamageTakenMult().unmodify(this.statsId);
			ship.getMutableStats().getBeamDamageTakenMult().unmodify(this.statsId);
			ship.getMutableStats().getMissileDamageTakenMult().unmodify(this.statsId);		
			ship.getMutableStats().getEmpDamageTakenMult().unmodify(this.statsId);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + OVERWHALMTIME;
		if (index == 1) return "" + (int) ((Float) mag.get(HullSize.FRIGATE) * 100f) + "%";
		if (index == 2) return "" + (int) ((Float) mag.get(HullSize.DESTROYER) * 100f) + "%";
		if (index == 3) return "" + (int) ((Float) mag.get(HullSize.CRUISER) * 100f) + "%";
		if (index == 4) return "" + (int) ((Float) mag.get(HullSize.CAPITAL_SHIP) * 100f) + "%";						
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shield";
	}
}
