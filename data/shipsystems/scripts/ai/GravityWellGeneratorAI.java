package data.shipsystems.scripts.ai;

import java.util.List;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipSystemAIScript;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import org.lazywizard.lazylib.combat.CombatUtils;


public class GravityWellGeneratorAI implements ShipSystemAIScript {

	private ShipAPI ship;
	private CombatEngineAPI engine;
	private ShipwideAIFlags flags;
	private ShipSystemAPI system;
	private CombatUtils combat;
	private IntervalUtil tracker = new IntervalUtil(0.5f, 1f);	
	
	public boolean started = false;
	public boolean isActive = false;
	public List<String> Teleport_System = new ArrayList<String>();		
	
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
		tracker.advance(amount);
		
		sinceLast += amount;
		
		if (!started) {
			Teleport_System.add("phaseteleporter");
			Teleport_System.add("displacer");
			Teleport_System.add("deracinator");
			Teleport_System.add("deracinator_small");
			Teleport_System.add("br_impulseskimmer");
			Teleport_System.add("fds_quantum_generator");
			Teleport_System.add("ora_teleporter");
			Teleport_System.add("SCY_experimentalTeleporter");
			Teleport_System.add("SCY_teleport");
			Teleport_System.add("ms_displacer");
			Teleport_System.add("ms_rakporter");
			Teleport_System.add("diableavionics_flicker");
			Teleport_System.add("diableavionics_heavyflicker");
			Teleport_System.add("diableavionics_drift");	
			Teleport_System.add("AL_displacerSec");
			Teleport_System.add("AL_eddydisplacer");					
			started = true;
		}
		
		if (tracker.intervalElapsed()) {
			if (system.getCooldownRemaining() > 0) return;
			if (system.isOutOfAmmo()) return;
			if (system.isActive()) return;
			if (ship.getHardFluxLevel() > 0.7f) return;
			
			int enemyMissiles = 0;
			List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 1900);
			for (ShipAPI s : ships) {		
				if (s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {
					if (s.getSystem() != null) {
						if (Teleport_System.contains(s.getSystem().getId())) {					
							ship.useSystem();
							isActive = true;
							return;
						}	
					}															
				}				
			}			
			List<MissileAPI> missiles = combat.getMissilesWithinRange(ship.getLocation(), 1300f);
			for (MissileAPI m : missiles) {
				if (m.getSource().getOriginalOwner() != ship.getOriginalOwner()) {
						enemyMissiles++;
				}					
			}
			if (enemyMissiles >= 5) {
				ship.useSystem();
				isActive = true;
				enemyMissiles = 0;
				return;
			}
			else {
				enemyMissiles = 0;
			}			
		}		
	}
}