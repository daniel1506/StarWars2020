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
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import org.lazywizard.lazylib.combat.CombatUtils;

public class BattleMeditationStats extends BaseShipSystemScript {
	
	private CombatUtils combat;
	public void init(CombatUtils combat)
	{
		this.combat=combat;
	}
	
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,50,255,75);
	public static final Color JITTER_COLOR = new Color(0,50,255,10);
	public static final Color TEXT_COLOR = new Color(0,50,255,150);	
	boolean buffed;
	public List<ShipAPI> targetedFighters = new ArrayList<ShipAPI>();
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		float jitterLevel = effectLevel;
		float maxRangeBonus = 5f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 500);
		if (!buffed) {
			for (ShipAPI s : ships) {		
				if (s.getHullSize() == HullSize.FIGHTER && s.getOriginalOwner() == ship.getOriginalOwner() && s.isAlive()) {
					MutableShipStatsAPI fStats = s.getMutableStats();
					if (fStats.getOverloadTimeMod().getBonusMult() >= 1f) {
						fStats.getDamageToFighters().modifyMult(id, 1.25f);
						fStats.getOverloadTimeMod().modifyMult(id, 0.5f);
						fStats.getShieldDamageTakenMult().modifyMult(id, 0.5f);
						targetedFighters.add(s);										
						s.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
						s.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
						s.getFluxTracker().showOverloadFloatyIfNeeded("Battle Meditation", TEXT_COLOR, 4f, true);
						buffed = true;
					}
				}				
			}
		}
		if (buffed) {
			for (ShipAPI t : targetedFighters) {
				t.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
				t.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);				
			}
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		if (!targetedFighters.isEmpty()) {
			for (ShipAPI t : targetedFighters) {
				MutableShipStatsAPI fStats = t.getMutableStats();
				fStats.getDamageToFighters().unmodify(id);
				fStats.getOverloadTimeMod().unmodify(id);
				fStats.getShieldDamageTakenMult().unmodify(id);
			}
			targetedFighters.clear();
			buffed = false;
		}			
	}
	
}
