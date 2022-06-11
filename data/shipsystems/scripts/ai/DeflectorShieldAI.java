package data.shipsystems.scripts.ai;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
//import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.util.IntervalUtil;

//import org.apache.log4j.Logger;


public class DeflectorShieldAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
	private ShipSystemAPI shield;
	
	//public Logger log = Logger.getLogger(this.getClass());
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
	
	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
		this.ship = ship;
		this.flags = flags;
		this.engine = engine;
		this.system = system;
		this.shield = ship.getPhaseCloak();
	}
	
	private float sinceLast = 0f;
		
	@SuppressWarnings("unchecked")	
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
		tracker.advance(amount);
		
		sinceLast += amount;
		
		if (tracker.intervalElapsed()) {
			//if (ship.getPhaseCloak().getCooldownRemaining() > 0) return;
			//if (system.isOutOfAmmo()) return;
			//if (system.isActive()) return;			
			
			if (ship.getHardFluxLevel() >= 0.7f) {
				if (ship.getHullLevel() >= 0.7f) {
					shield.deactivate();
				}
				else if (ship.getHullLevel() >= 0.5f) {
					if (flags.hasFlag(AIFlags.SAFE_FROM_DANGER_TIME)) {
						shield.deactivate();
					}
				}				
				else {
					if (!flags.hasFlag(AIFlags.SAFE_VENT)) {
						flags.setFlag(AIFlags.SAFE_VENT, 3f);
						flags.setFlag(AIFlags.OK_TO_CANCEL_SYSTEM_USE_TO_VENT , 3f);						
					}
					//if (!flags.hasFlag(AIFlags.BACK_OFF) && !flags.hasFlag(AIFlags.BACKING_OFF)) {
					//	flags.setFlag(AIFlags.BACK_OFF, 3f);
					//}
				}
			}
			if (ship.getHullLevel() <= 0.3f) {
				if (!ship.getFluxTracker().isOverloadedOrVenting()) {
					if (!shield.isActive() && !shield.isChargeup() && !shield.isCoolingDown()) {
						if (!ship.isDefenseDisabled()) {
							shield.forceState(SystemState.IN, 0f);
						}						
					}
				}
			}
			if (ship.getHardFluxLevel() < 0.7f && (ship.getHullLevel()) > 0.3f) {
				if (!flags.hasFlag(AIFlags.SAFE_FROM_DANGER_TIME) && !ship.getFluxTracker().isOverloadedOrVenting()) {
					if (!shield.isActive() && !shield.isChargeup() && !shield.isCoolingDown()) {
						if (!ship.isDefenseDisabled()) {
							shield.forceState(SystemState.IN, 0f);	
						}	
					}
				}
			}
		}
	}

}