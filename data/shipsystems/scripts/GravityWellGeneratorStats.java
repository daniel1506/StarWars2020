package data.shipsystems.scripts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.lwjgl.util.vector.Vector2f;

//import org.dark.shaders.distortion.DistortionAPI;
//import org.dark.shaders.distortion.DistortionShader;
//import org.dark.shaders.distortion.WaveDistortion;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
//import org.apache.log4j.Logger;

public class GravityWellGeneratorStats extends BaseShipSystemScript {
	
	//public Logger log = Logger.getLogger(this.getClass());
	public boolean started = false;	
	public static final Color TEXT_COLOR = new Color(102,102,255,200);
	public static List<String> Teleport_System = new ArrayList<String>();
	private List<ShipAPI> Interdicted_List = new ArrayList<ShipAPI>();
	private List<MissileAPI> targetedMissiles = new ArrayList<MissileAPI>();
	public static final int MAX_PARTICLES_PER_FRAME = 30;
    public static final Color PARTICLE_COLOR = new Color(102,102,255,200);
    public static final float PARTICLE_OPACITY = 0.85f;
    public static final float PARTICLE_RADIUS = 300.0f;
    public static final float PARTICLE_SIZE = 6.0f;	
	private CombatUtils combat;
	//private WaveDistortion wave;
	public void init(CombatUtils combat)
	{
		this.combat=combat;		
	}
	static {
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
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
			
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		
		if (!started) {
			if (ship == Global.getCombatEngine().getPlayerShip()) {
				Global.getSoundPlayer().playSound("sw_system_gravity_well", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
			}		
			started = true;
		}
		
		boolean deactivate = true;
		ship.getVelocity().set(0f,0f);
		
		if (!Interdicted_List.isEmpty()) {
			Iterator<ShipAPI> ship_iterator = Interdicted_List.iterator();
			while(ship_iterator.hasNext()){
				ShipAPI i = (ShipAPI) ship_iterator.next();
				if (!i.isAlive() || Misc.getDistance(ship.getLocation(), i.getLocation()) > (ship.getCollisionRadius() + 2300f)) {
					i.setShipSystemDisabled(false);
					ship_iterator.remove();
				}
			}
		}
		
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), ship.getCollisionRadius() + 2000f);
		for (ShipAPI s : ships) {		
			if (s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive() && !Interdicted_List.contains(s)) {
				if(s.getSystem() != null) {
					if (Teleport_System.contains(s.getSystem().getId())) {
						s.setShipSystemDisabled(true);
						Interdicted_List.add(s);
						s.getFluxTracker().showOverloadFloatyIfNeeded("Interdicted!", TEXT_COLOR, 4f, true);
					}
				}				
			}								
		}
		
		//List<ShipAPI> shipsOutOfRange = combat.getShipsWithinRange(ship.getLocation(), ship.getCollisionRadius() + 3500f);
		//for (ShipAPI r : shipsOutOfRange) {
		//	if (!ships.contains(r)) {
		//		r.setShipSystemDisabled(false);
		//		Interdicted_List.remove(r);
		//	}
		//}		
		
		for (int numParticlesThisFrame = Math.round(effectLevel * 30.0f), x = 0; x < numParticlesThisFrame; ++x) {
			final Vector2f particlePos = MathUtils.getRandomPointOnCircumference(ship.getLocation(), ship.getShieldRadiusEvenIfNoShield() * 1.35f);
			final Vector2f particleVel = Vector2f.sub(ship.getLocation(), particlePos, (Vector2f)null);
			Global.getCombatEngine().addSmokeParticle(particlePos, particleVel, 6.0f, 0.85f, 1.0f, PARTICLE_COLOR);
		}
		
		List<MissileAPI> missiles = combat.getMissilesWithinRange(ship.getLocation(), ship.getCollisionRadius() + 1500f);
		for (MissileAPI m : missiles) {
			if (m.getSource().getOriginalOwner() != ship.getOriginalOwner()) {
				if (m.getCollisionClass() == CollisionClass.MISSILE_FF || m.getCollisionClass() == CollisionClass.MISSILE_NO_FF) {					
					m.setCollisionClass(CollisionClass.NONE);
					targetedMissiles.add(m);
					deactivate = false;					
				}
			}			
		}
		
		Iterator<MissileAPI> iterator = targetedMissiles.iterator();
		while (iterator.hasNext()) {
			MissileAPI t = (MissileAPI) iterator.next();
			if (t.getEngineController().isFlamedOut() || t.isFizzling() || t.isExpired()) {
				iterator.remove();
				continue;
			}
			else {
				if (t.getLocation().getX() > ship.getLocation().getX()) {
					if ((t.getFacing() + 3f) <= 360f) {
						t.setFacing(t.getFacing() + 3f);
					}
					else if ((t.getFacing() + 3f) > 360f) {
						t.setFacing(t.getFacing() + 3f -360f);
					}		
				}
				else {
					if ((t.getFacing() - 3f) >= 0f) {
						t.setFacing(t.getFacing() - 3f);
					}
					else if ((t.getFacing() - 3f) < 0f) {
						t.setFacing(t.getFacing() - 3f +360f);
					}
				}
				deactivate = false;
			}
		}
		
		if (ship != Global.getCombatEngine().getPlayerShip() || !Global.getCombatEngine().isUIAutopilotOn()) {
			if (!Interdicted_List.isEmpty()) {
				deactivate = false;			
			}
			if (deactivate == true || ship.getHardFluxLevel() > 0.7f) {
				ship.getSystem().deactivate();
			}
		}
		
	}
	
	public StatusData getStatusData(final int index, final State state, final float effectLevel) {
        if (index == 0) {
            return new StatusData("Interdicting enemy jump drives and missiles", false);
        }
        return null;
    }
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		if (!Interdicted_List.isEmpty()) {
			for (ShipAPI i : Interdicted_List) {
				i.setShipSystemDisabled(false);
			}
			Interdicted_List.clear();
		}	
		targetedMissiles.clear();
		started = false;	
		return;
	}
	
}
