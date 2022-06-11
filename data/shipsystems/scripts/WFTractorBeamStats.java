package data.shipsystems.scripts;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;


public class WFTractorBeamStats extends BaseShipSystemScript {
	
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,255,0,75);
	public static final Color JITTER_COLOR = new Color(0,255,0,10);
	public static final Color TEXT_COLOR = new Color(0,230,0,150);	
	protected ShipAPI targetedShip = null;
	
	protected ShipAPI findTarget(ShipAPI ship) {
		ShipAPI target = ship.getShipTarget();
		if (target != null) {
			if (target.getOriginalOwner() == ship.getOriginalOwner() || !target.isAlive()) target = null;
		}
		if (target != null) {
			float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
			float radSum = ship.getCollisionRadius() + target.getCollisionRadius();
			if (dist > 900 + radSum) target = null;			
		} 		
		return target;
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		float jitterLevel = effectLevel;
		float maxRangeBonus = 5f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		ShipAPI ship = null;		
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		ShipAPI target = findTarget(ship);
		if (targetedShip == null && target != null) {
			if (target.getHullSize() == HullSize.FRIGATE) {
				if (target.getMutableStats().getMaxSpeed().getModifiedValue() > (target.getMutableStats().getMaxSpeed().getBaseValue() / 2f)) {					
					stats.getShieldDamageTakenMult().modifyMult(id, 0.65f);		
					stats.getEnergyWeaponDamageMult().modifyMult(id, 0.80f);
					stats.getMaxSpeed().modifyMult(id, 0.85f);
					target.getMutableStats().getMaxSpeed().modifyMult(id, 0.75f);
					target.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
					target.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
					target.getFluxTracker().showOverloadFloatyIfNeeded("Tractor Beamed!", TEXT_COLOR, 4f, true);
					targetedShip = target;
				}
			}
			else if (target.getHullSize() == HullSize.DESTROYER) {
				if (target.getMutableStats().getMaxSpeed().getModifiedValue() > ((target.getMutableStats().getMaxSpeed().getBaseValue() / 10f) * 6f)) {
					stats.getShieldDamageTakenMult().modifyMult(id, 0.65f);		
					stats.getEnergyWeaponDamageMult().modifyMult(id, 0.80f);
					stats.getMaxSpeed().modifyMult(id, 0.85f);
					target.getMutableStats().getMaxSpeed().modifyMult(id, 0.85f);
					target.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
					target.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
					target.getFluxTracker().showOverloadFloatyIfNeeded("Tractor Beamed!", TEXT_COLOR, 4f, true);
					targetedShip = target;
				}
			}			
		}
		if (targetedShip != null) {
			if (targetedShip == target && targetedShip.isAlive()) {
				targetedShip.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
				targetedShip.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);				
			}
			if ((target != null && targetedShip != target) || !targetedShip.isAlive()) {
				ship.getSystem().deactivate();
			}
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		if (targetedShip != null) {
			targetedShip.getMutableStats().getMaxSpeed().unmodify(id);
			targetedShip = null;
			stats.getShieldDamageTakenMult().unmodify(id);
			stats.getEnergyWeaponDamageMult().unmodify(id);
			stats.getMaxSpeed().unmodify(id);
		}				
	}
	
}