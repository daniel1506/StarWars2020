package data.shipsystems.scripts.ai;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.util.IntervalUtil;

import org.lazywizard.lazylib.combat.CombatUtils;
import org.apache.log4j.Logger;


public class RequestEscortAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
	
	public Logger log = Logger.getLogger(this.getClass());
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);
	private CombatUtils combat;
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
	
	private float sinceLast = 0f;
		
	@SuppressWarnings("unchecked")	
	public void advance(float amount, Vector2f missileDangerDir, Vector2f collisionDangerDir, ShipAPI target) {
		if (!engine.isInCampaign() && !engine.isInCampaignSim() && !engine.isMission()) {
			return;
		}
		
		tracker.advance(amount);
		
		sinceLast += amount;
		
		if (tracker.intervalElapsed()) {
			if (system.getCooldownRemaining() > 0) return;
			if (system.isOutOfAmmo()) return;
			if (system.isActive()) return;
			
			int enemyFighters = 0;
			List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 500);
			for (ShipAPI s : ships) {		
				if (s.getHullSize() == HullSize.FIGHTER && s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {					
					enemyFighters++;																
				}				
			}
			if (enemyFighters >= 3 && sinceLast >= 1f) {
				ship.useSystem();
				sinceLast = 0f;
				return;
			}
			if (sinceLast >= 1.5f) {
				sinceLast = 0f;
			}
		}		
	}

}
