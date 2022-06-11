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


public class FriendlyHuntStats extends BaseShipSystemScript {
	
	private CombatUtils combat;
	public void init(CombatUtils combat)
	{
		this.combat=combat;
	}
	
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,50,255,75);
	public static final Color JITTER_COLOR = new Color(0,50,255,10);
	public static final Color TEXT_COLOR = new Color(0,50,255,150);	
	boolean flanked;
	public List<ShipAPI> targetedFighters = new ArrayList<ShipAPI>();
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		float jitterLevel = effectLevel;
		float maxRangeBonus = 5f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		boolean refitted = false;
		List<ShipAPI> ships = combat.getShipsWithinRange(ship.getLocation(), 500);
		if (!flanked) {
			for (ShipAPI s : ships) {		
				if (s.getHullSize() == HullSize.FIGHTER && s.getOriginalOwner() != ship.getOriginalOwner() && s.isAlive()) {
					MutableShipStatsAPI fStats = s.getMutableStats();
					if (fStats.getDamageToCapital().getModifiedValue() >= 1f && fStats.getDamageToFrigates().getModifiedValue() >= 1f) {
						fStats.getDamageToCapital().modifyMult(id, 0.75f);
						fStats.getDamageToCruisers().modifyMult(id, 0.75f);
						fStats.getDamageToDestroyers().modifyMult(id, 0.75f);
						fStats.getDamageToFrigates().modifyMult(id, 0.75f);
						fStats.getEnergyDamageTakenMult().modifyMult(id, 1.1f);
						targetedFighters.add(s);										
						s.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
						s.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
						s.getFluxTracker().showOverloadFloatyIfNeeded("Get Flanked!", TEXT_COLOR, 4f, true);
						flanked = true;
					}
				}				
			}
		}
		if (flanked) {
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
				fStats.getDamageToCapital().unmodify(id);
				fStats.getDamageToCruisers().unmodify(id);
				fStats.getDamageToDestroyers().unmodify(id);
				fStats.getDamageToFrigates().unmodify(id);
				fStats.getEnergyDamageTakenMult().unmodify(id);
			}
			targetedFighters.clear();
			flanked = false;
		}			
	}
	
}
