package data.shipsystems.scripts;

import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

//import org.apache.log4j.Logger;

public class DeflectorShieldStats extends BaseShipSystemScript {
	
	//public Logger log = Logger.getLogger(this.getClass());
	protected boolean listenerAdded;
	public static final Color TEXT_COLOR = new Color(255,255,255,100);
	
	public class DamageListenerScript implements DamageTakenModifier {		
		private ShipAPI ship;
		private MutableShipStatsAPI stats;
		private FluxTrackerAPI flux;
		private CombatEngineAPI engine;
		public DamageListenerScript(ShipAPI ship) {
			this.ship = ship;
			this.stats = ship.getMutableStats();
			this.flux = ship.getFluxTracker();
			this.engine = Global.getCombatEngine();
		} 

		public String modifyDamageTaken(java.lang.Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			if (!ship.getPhaseCloak().isActive()) {
				return "Off";
			}
			else if (target instanceof ShipAPI && (ShipAPI) target == ship) {
				float damageAmount = damage.getDamage() * damage.getType().getShieldMult() * stats.getShieldDamageTakenMult().getModifiedValue() * ship.getHullSpec().getShieldSpec().getFluxPerDamageAbsorbed();				
				if (damage.isDps()) {
					if (param instanceof BeamAPI) {
						BeamAPI beam = (BeamAPI) param;
						engine.addFloatingDamageText(point, damageAmount * damage.getDpsDuration(), TEXT_COLOR, target, beam.getSource());
						if ((flux.getCurrFlux() + (damageAmount * damage.getDpsDuration())) >= flux.getMaxFlux()) {
							flux.increaseFlux(damageAmount * damage.getDpsDuration(), false);
							flux.forceOverload(0f);
						}
						else {
							flux.increaseFlux(damageAmount * damage.getDpsDuration(), false);
						}
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
					flux.increaseFlux(damageAmount, true);
					if (param instanceof DamagingProjectileAPI) {
						DamagingProjectileAPI proj = (DamagingProjectileAPI) param;
						engine.addFloatingDamageText(point, damageAmount, TEXT_COLOR, target, proj.getSource());
					}
					else if (param instanceof BeamAPI) {
						BeamAPI beam = (BeamAPI) param;
						engine.addFloatingDamageText(point, damageAmount, TEXT_COLOR, target, beam.getSource());
						flux.increaseFlux(damageAmount, false);
					}
				}
				return "On";
			}
			return "Bug";
		}
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		stats.getProjectileDamageTakenMult().modifyMult(id, 0.0f);
		stats.getBeamDamageTakenMult().modifyMult(id, 0.0f);
		stats.getMissileDamageTakenMult().modifyMult(id, 0.0f);
		stats.getEmpDamageTakenMult().modifyMult(id, 0.25f);		
				
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		if(!listenerAdded) {
			DamageListenerScript DL = new DamageListenerScript(ship);
			ship.addListener(DL);
			listenerAdded = true;
		}
	
		FluxTrackerAPI flux = (FluxTrackerAPI) ship.getFluxTracker();
		flux.decreaseFlux(ship.getHullSpec().getFluxDissipation() * stats.getHardFluxDissipationFraction().getModifiedValue() * 0.02f);
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		//ShipAPI ship = null;
		//if (stats.getEntity() instanceof ShipAPI) {
		//	ship = (ShipAPI) stats.getEntity();			
		//}
		//if (ship.getFluxTracker().isOverloaded()) {
		//	ship.getFluxTracker().decreaseFlux((ship.getMaxFlux() / 5)) ;
		//}
		stats.getProjectileDamageTakenMult().unmodify(id);
		stats.getBeamDamageTakenMult().unmodify(id);
		stats.getMissileDamageTakenMult().unmodify(id);		
		stats.getEmpDamageTakenMult().unmodify(id);
	}
	
	@Override
	public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
		if (system.isOutOfAmmo()) return null;
		if (system.getState() != SystemState.IDLE) return null;
		
		if (ship.isDefenseDisabled()) {
			return "RECHARGING";
		}			
		return "READY";
	}
	
	@Override
	public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
		return !ship.isDefenseDisabled();
	}

}
