package data.shipsystems.scripts.ai;

import java.util.List;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.combat.ai.OmniShieldControlAI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;


public class HeavyTractorBeamAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
	protected ShipAPI shipTarget;
	
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);

	
	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
		this.ship = ship;
		this.flags = flags;
		this.engine = engine;
		this.system = system;
	}
	
	private float sinceLast = 0f;
		
	@SuppressWarnings("unchecked")	
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {		
		tracker.advance(amount);
		
		sinceLast += amount;
		
		if (tracker.intervalElapsed()) {
			if (system.getCooldownRemaining() > 0) return;
			if (system.isOutOfAmmo()) return;
			if (system.isActive()) return;
			if (ship == Global.getCombatEngine().getPlayerShip()) return;
						
			
			if ((target == null) || target.getOwner() == ship.getOwner()) {		
				Object test = ship.getAIFlags().getCustom(AIFlags.MANEUVER_TARGET);
				if (test instanceof ShipAPI) {
					shipTarget = (ShipAPI) test;
				}
				else {
					shipTarget = Misc.findClosestShipEnemyOf(ship, ship.getLocation(), HullSize.FRIGATE, (1500f + ship.getCollisionRadius()), true);
				}				
			}
			else if (target != null && target.isAlive()) {
				shipTarget = target;
			}
			
			if (shipTarget != null && shipTarget.getHullSize() != HullSize.FIGHTER) {
				if (shipTarget.getMutableStats().getMaxSpeed().getModifiedValue() != 0f) {					
					List<WeaponAPI> weapons = ship.getAllWeapons();
					Vector2f gunpoint = ship.getLocation();
					for (WeaponAPI w: weapons) {
						if (w.getType() == WeaponType.DECORATIVE) {
							gunpoint = w.getLocation();
							break;
						}
					}
					if (Misc.getAngleDiff(ship.getFacing(), Misc.getAngleInDegrees(gunpoint, shipTarget.getLocation())) <= 90f) {
						float dist = Misc.getDistance(ship.getLocation(), shipTarget.getLocation());
						if (dist <= (1500f + ship.getCollisionRadius() + shipTarget.getCollisionRadius())) {
							if (shipTarget.getAIFlags().hasFlag(AIFlags.BACKING_OFF)) {
								ship.useSystem();
								return;
							}
						}
					}
				}					
			}
			return;
		}
	}

}