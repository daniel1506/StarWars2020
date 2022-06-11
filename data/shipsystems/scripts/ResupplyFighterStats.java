package data.shipsystems.scripts;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
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


public class ResupplyFighterStats extends BaseShipSystemScript {
	
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,255,50,125);
	public static final Color JITTER_COLOR = new Color(0,255,50,75);
	public static final Color TEXT_COLOR = new Color(0,255,0,200);
	protected boolean TextPlayed;
	private CombatUtils combat;
	public void init(CombatUtils combat)
	{
		this.combat=combat;
	}
	
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
			for (ShipAPI s : ships) {		
				if (s.getHullSize() == HullSize.FIGHTER && s.getOriginalOwner() == ship.getOriginalOwner() && s.isAlive()) {
					List weapons = s.getAllWeapons();
					for (WeaponAPI w : weapons){
						if (!w.usesAmmo()) {
							continue;
						}						
						if (w.getAmmo() != w.getMaxAmmo()) {
							w.resetAmmo();
							refitted = true;
							s.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
							s.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
						}													
					}
					if (refitted && !TextPlayed) {
						s.getFluxTracker().showOverloadFloatyIfNeeded("Resupplyed!", TEXT_COLOR, 4f, true);
						TextPlayed = true;
					}					
					if (refitted) {						
						return;
					}
				}				
			}		
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		return;
	}
	
}
