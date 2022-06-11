package data.shipsystems.scripts;

import java.awt.Color;
//import java.util.HashMap;
//import java.util.Map;
import java.util.ArrayList;
//import java.util.EnumSet;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI.SystemState;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.Misc;

import data.scripts.util.MagicFakeBeam;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lazywizard.lazylib.MathUtils;

public class ShieldLeecherStats extends BaseShipSystemScript {
	
	public static final int MAX_PARTICLES_PER_FRAME = 30;
    public static final Color PARTICLE_COLOR = new Color(102,102,255,200);
    public static final float PARTICLE_OPACITY = 0.85f;
    public static final float PARTICLE_RADIUS = 300.0f;
    public static final float PARTICLE_SIZE = 6.0f;	
	
	private CombatUtils combat;
	public void init(CombatUtils combat)
	{
		this.combat=combat;
	}
	
	//public static final Object KEY_JITTER = new Object();
	//public static final Color JITTER_UNDER_COLOR = new Color(255,255,100,75);
	//public static final Color JITTER_COLOR = new Color(0,50,255,10);
	public static final Color BEAM_COLOR = new Color(0,150,255,10);
	public static final Color TEXT_COLOR = new Color(102,102,255,200);
	private boolean alreadyExecuted;
	protected float shieldLeeched;
	//protected List<ShipAPI> targetedFighters = new ArrayList<ShipAPI>();
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		//float jitterLevel = effectLevel;
		//float maxRangeBonus = 5f;
		//float jitterRangeBonus = jitterLevel * maxRangeBonus;
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		if (!alreadyExecuted) {
			if (ship == Global.getCombatEngine().getPlayerShip()) {
				Global.getSoundPlayer().playSound("sw_system_leech_shield", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
			}
			alreadyExecuted = true;
		}
		if (!ship.getPhaseCloak().isActive()) {
			ship.getPhaseCloak().forceState(SystemState.IN, 0f);
		}
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 1000);
		for (ShipAPI s : ships) {
			if (s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {
				if (s.getShield() == null && s.getPhaseCloak() == null) continue;
				if (s.getHullSize() == HullSize.FIGHTER && !s.isDefenseDisabled()) {
					s.setDefenseDisabled(true);
					//targetedFighters.add(s);
					stats.getFluxCapacity().modifyFlat(id, s.getMaxFlux());
					shieldLeeched = shieldLeeched + s.getMaxFlux();
					s.getFluxTracker().showOverloadFloatyIfNeeded("Shield Leeched!", TEXT_COLOR, 4f, true);
				}
				else if (s.getHullSize() != HullSize.FIGHTER) {
					if (!s.getFluxTracker().isOverloadedOrVenting() ) {
						s.getFluxTracker().increaseFlux(s.getMaxFlux() / 600f, false);
						ship.getFluxTracker().decreaseFlux(s.getMaxFlux() / 600f);
						float dist = Misc.getDistance(ship.getLocation(), s.getLocation());
						MagicFakeBeam.spawnFakeBeam (Global.getCombatEngine(), ship.getLocation(), dist, Misc.getAngleInDegrees(ship.getLocation(), s.getLocation()), 
						7f, 0.1f, 0.1f, 0f, BEAM_COLOR, BEAM_COLOR, 0f, DamageType.ENERGY, 0f, ship);
					}
					//s.getFluxTracker().showOverloadFloatyIfNeeded("Shield Leeched!", TEXT_COLOR, 4f, true);
				}
			}
		}
		for (int numParticlesThisFrame = Math.round(effectLevel * 30.0f), x = 0; x < numParticlesThisFrame; ++x) {
			final Vector2f particlePos = MathUtils.getRandomPointOnCircumference(ship.getLocation(), ship.getShieldRadiusEvenIfNoShield() * 1.35f);
			final Vector2f particleVel = Vector2f.sub(ship.getLocation(), particlePos, (Vector2f)null);
			Global.getCombatEngine().addSmokeParticle(particlePos, particleVel, 6.0f, 0.85f, 1.0f, PARTICLE_COLOR);
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		alreadyExecuted = false;
	}
	
	public StatusData getStatusData(final int index, final State state, final float effectLevel) {
        if (index == 0) {
            return new StatusData("Leeching enemy shields", false);
        }
		else if (index == 1 && shieldLeeched != 0f) {
			return new StatusData("Shield Capacity increased by " + (int) shieldLeeched, false);
		}
        return null;
    }
	
	@Override
	public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
		if (system.isOutOfAmmo()) return null;
		if (system.getState() != SystemState.IDLE) return null;
		
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 1000);
		for (ShipAPI s : ships) {
			if (s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {
				if (s.getShield() != null || s.getPhaseCloak() != null) {
					return "READY";
				}
			}
		}
		return "NO VALID TARGET";
	}
	
	@Override
	public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 1000);
		for (ShipAPI s : ships) {
			if (s.getOriginalOwner() != ship.getOriginalOwner()) {
				if (s.getShield() != null || s.getPhaseCloak() != null) {
					return true;
				}
			}
		}
		return false;
	}
}
