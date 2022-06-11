package data.shipsystems.scripts.ai;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.combat.ai.OmniShieldControlAI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;


public class WFTractorBeamAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
	
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
			if (target == null) return;
			
			if (target.getOriginalOwner() != ship.getOriginalOwner() && target.isAlive()) {
				float dist = Misc.getDistance(ship.getLocation(), target.getLocation());
				if (dist <= (600 + ship.getCollisionRadius())) {
					if (target.getHullSize() == HullSize.FRIGATE) {
						if (target.getMutableStats().getMaxSpeed().getModifiedValue() > (target.getMutableStats().getMaxSpeed().getBaseValue() / 2f)) {
							ship.useSystem();
							return;
						}
					}
					else if (target.getHullSize() == HullSize.DESTROYER) {
						if (target.getMutableStats().getMaxSpeed().getModifiedValue() > (target.getMutableStats().getMaxSpeed().getBaseValue() / 10f * 6f)) {
							ship.useSystem();
							return;
						}
					}	
				}
			}
		}
	}

}