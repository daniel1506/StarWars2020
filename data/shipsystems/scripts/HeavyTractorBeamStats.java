package data.shipsystems.scripts;

import java.awt.Color;
import java.util.List;
import java.lang.Math;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.util.MagicFakeBeam;


public class HeavyTractorBeamStats extends BaseShipSystemScript {
	
	public static final Color BEAM_COLOR = new Color(102,102,255,100);
	public static final Color BEAM_CORE_COLOR = new Color(153,153,255,100);
	public static final Color TEXT_COLOR = new Color(0,230,0,150);
	protected ShipAPI targetedShip = null;
	
	protected ShipAPI findTarget(ShipAPI ship) {
		ShipAPI target = ship.getShipTarget();
		if (ship != Global.getCombatEngine().getPlayerShip()) {
			if (target == null || target.getOwner() == ship.getOwner()) {		
				Object test = ship.getAIFlags().getCustom(AIFlags.MANEUVER_TARGET);
				if (test instanceof ShipAPI) {
					target = (ShipAPI) test;
				}
				else {
					target = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), HullSize.FRIGATE, (1500f + ship.getCollisionRadius()), true);
				}				
			}
		}
		if (target != null) {
			if (target.getOriginalOwner() == ship.getOriginalOwner() || !target.isAlive()) return null;
			if (target.getHullSize() == HullSize.FIGHTER) return null;
		}
		if (target != null) {
			float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
			if (dist > (1500f + ship.getCollisionRadius() + target.getCollisionRadius())) return null;
			List<WeaponAPI> weapons = ship.getAllWeapons();
			Vector2f gunpoint = ship.getLocation();
			for (WeaponAPI w: weapons) {
				if (w.getType() == WeaponType.DECORATIVE) {
					gunpoint = w.getLocation();
					break;
				}
			}
			if (Misc.getAngleDiff(ship.getFacing(), Misc.getAngleInDegrees(gunpoint, target.getLocation())) > 90f) return null;
			if (target.getMutableStats().getMaxSpeed().getModifiedValue() == 0f) return null;
		}
		return target;
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		ShipAPI ship = null;		
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		ShipAPI target = findTarget(ship);
		if ((targetedShip == null) && target != null) {					
			target.getMutableStats().getMaxSpeed().modifyMult(id, 0f);
			target.getFluxTracker().showOverloadFloatyIfNeeded("Tractor Beamed!", TEXT_COLOR, 4f, true);
			targetedShip = target;
			if (ship == Global.getCombatEngine().getPlayerShip()) {
				Global.getSoundPlayer().playSound("sw_system_tractor_beam", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
			}
		}
		if (targetedShip != null && targetedShip.isAlive()) {				
			float dist = Misc.getDistance(ship.getLocation(), targetedShip.getLocation());
			List<WeaponAPI> weapons = ship.getAllWeapons();
			Vector2f gunpoint = ship.getLocation();
			for (WeaponAPI w: weapons) {
				if (w.getType() == WeaponType.DECORATIVE) {
					gunpoint = w.getLocation();
					break;
				}
			}
			MagicFakeBeam.spawnFakeBeam (Global.getCombatEngine(), gunpoint, dist, Misc.getAngleInDegrees(gunpoint, targetedShip.getLocation()), 
				15f, 0.1f, 0.1f, 0f, BEAM_COLOR, BEAM_CORE_COLOR, 0f, DamageType.ENERGY, 0f, ship);
			if (dist > (400f + ship.getCollisionRadius() + targetedShip.getCollisionRadius())) {
				float x = ship.getLocation().x - targetedShip.getLocation().x;
				float y = ship.getLocation().y - targetedShip.getLocation().y;
				float sum = Math.abs(x) + Math.abs(y);
				targetedShip.getVelocity().set(100f * x / sum, 100f * y / sum);
			}
			else {
				targetedShip.getVelocity().set(0f,0f);
			}
		}
		else if (targetedShip != null && !targetedShip.isAlive()) {
			ship.getSystem().deactivate();
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		if (targetedShip != null) {
			ShipAPI ship = null;
			if (stats.getEntity() instanceof ShipAPI) {
				ship = (ShipAPI) stats.getEntity();
				if (ship == Global.getCombatEngine().getPlayerShip()) {
					Global.getSoundPlayer().playSound("sw_system_tractor_beam_deactivate", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
				}
			}
			targetedShip.getMutableStats().getMaxSpeed().unmodify(id);
			targetedShip = null;
		}			
	}
	
	@Override
	public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
		if (system.isOutOfAmmo()) return null;
		if (system.getState() != SystemState.IDLE) return null;
		
		ShipAPI target = findTarget(ship);
		ShipAPI shipTarget = ship.getShipTarget();
		if (target != null && target != ship) {
			return "READY";			
		}
		if ((target == null) && shipTarget != null) {
			if (shipTarget.getHullSize() == HullSize.FIGHTER || shipTarget.getMutableStats().getMaxSpeed().getModifiedValue() == 0f) {
				return "INVALID TARGET";
			}
			if (Misc.getDistance(ship.getLocation(), shipTarget.getLocation()) > (1500f + ship.getCollisionRadius())) {
				return "OUT OF RANGE";
			}
			return "BLIND ANGLE";
		}
		return "NO TARGET";
	}
	
	@Override
	public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
		//if (true) return true;
		ShipAPI target = findTarget(ship);
		return target != null && target != ship;
	}
	
}