package data.shipsystems.scripts;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI.ShieldSpecAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import org.lazywizard.lazylib.combat.CombatUtils;
import org.apache.log4j.Logger;

public class MediumTractorBeamStats extends BaseShipSystemScript {
	
	public Logger log = Logger.getLogger(this.getClass());
	private CombatUtils combat;
	public void init(CombatUtils combat)
	{
		this.combat=combat;
	}
	
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,255,0,200);
	public static final Color JITTER_COLOR = new Color(0,255,0,150);
	public static final Color TEXT_COLOR = new Color(0,230,0,150);	
	boolean locked;
	public List<ShipAPI> targetedFighters = new ArrayList<ShipAPI>();
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		float jitterLevel = effectLevel;
		float maxRangeBonus = 5f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 600);
		if (!locked) {
			for (ShipAPI s : ships) {
				if (targetedFighters.isEmpty() || targetedFighters.size() < 7) {
					if (s.getHullSize() == HullSize.FIGHTER && s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {
						s.getVelocity().set(0f,0f);
						s.setDefenseDisabled(true);
						s.getFluxTracker().forceOverload(0f);
						targetedFighters.add(s);										
						s.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
						s.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
						s.getFluxTracker().showOverloadFloatyIfNeeded("Tractor Beamed!", TEXT_COLOR, 4f, true);						
						if (ship == Global.getCombatEngine().getPlayerShip() && !locked) {
							Global.getSoundPlayer().playSound("sw_system_tractor_beam", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
						}
						locked = true;
					}	
				}							
			}
			if (!locked) {
				if (ship == Global.getCombatEngine().getPlayerShip()) {
					Global.getSoundPlayer().playSound("gun_out_of_ammo", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
				}				
				ship.getSystem().deactivate();
				ship.getSystem().setCooldownRemaining(1f);
				return;
			}
		}
		if (locked) {
			for (ShipAPI t : targetedFighters) {
				if (t.isAlive()) {
					t.getVelocity().set(0f,0f);
					t.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
					t.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
				}
			}
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		if (!targetedFighters.isEmpty()) {
			for (ShipAPI t : targetedFighters) {
				t.setDefenseDisabled(false);
				t.getFluxTracker().stopOverload();
			}
			targetedFighters.clear();
			locked = false;
		}
	}
	
	public StatusData getStatusData(final int index, final State state, final float effectLevel) {
        if (index == 0 && locked) {
            return new StatusData("Tractor Beam locked " + targetedFighters.size() + " enemy fighters", false);
        }
		else if (index == 0 && !locked) {
			return new StatusData("No valid target available", false);
		}
        return null;
    }
	
}
