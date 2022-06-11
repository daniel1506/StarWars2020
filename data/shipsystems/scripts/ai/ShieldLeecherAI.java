package data.shipsystems.scripts.ai;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.util.IntervalUtil;

import org.lazywizard.lazylib.combat.CombatUtils;


public class ShieldLeecherAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
	
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
	private CombatUtils combat;
	protected int enemyShips;
	public void init(CombatUtils combat)
	{
		this.combat=combat;
	}
	
	public void init(ShipAPI ship, ShipSystemAPI system, ShipwideAIFlags flags, CombatEngineAPI engine) {
		this.ship = ship;
		this.flags = flags;
		this.engine = engine;
		this.system = system;
	}
	
	//private float sinceLast = 0f;
		
	@SuppressWarnings("unchecked")	
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
		tracker.advance(amount);
		
		//sinceLast += amount;
		
		if (tracker.intervalElapsed()) {
			if (system.getCooldownRemaining() > 0) return;
			if (system.isOutOfAmmo()) return;
			if (system.isActive()) return;
			
			List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 1000);
			for (ShipAPI s : ships) {		
				if (s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {
					if (s.getShield() != null || s.getPhaseCloak() != null) {
						enemyShips++;
					}						
				}				
			}
			if (enemyShips >= 3) {
				ship.useSystem();
				enemyShips = 0;
			}
			else if (ship.getFluxLevel() >= 0.65f) {
				ship.useSystem();
				enemyShips = 0;
			}
		}		
	}

}
