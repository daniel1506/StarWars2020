package data.shipsystems.scripts;

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
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.loading.ProjectileSpawnType;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import org.lazywizard.lazylib.combat.CombatUtils;
import org.apache.log4j.Logger;

public class DeflectorShieldStats extends BaseShipSystemScript {
	
	public Logger log = Logger.getLogger(this.getClass());
	private CombatUtils combat;
		public void init(CombatUtils combat)
		{
			this.combat=combat;
		}

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FIGHTER, 0.33f);
		mag.put(HullSize.FRIGATE, 0.33f);
		mag.put(HullSize.DESTROYER, 0.33f);
		mag.put(HullSize.CRUISER, 0.5f);
		mag.put(HullSize.CAPITAL_SHIP, 0.5f);
	}
	
	protected Object STATUSKEY1 = new Object();
	
	//public static final float INCOMING_DAMAGE_MULT = 0.25f;
	public static final float INCOMING_DAMAGE_CAPITAL = 0.5f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		effectLevel = 1f;
		
		float mult = (Float) mag.get(HullSize.CRUISER);
		if (stats.getVariant() != null) {
			mult = (Float) mag.get(stats.getVariant().getHullSize());
		}
		//stats.getHullDamageTakenMult().modifyMult(id, 0.01f);
		//stats.getArmorDamageTakenMult().modifyMult(id, 0.01f);
		stats.getProjectileDamageTakenMult().modifyMult(id, 0.0f);
		stats.getBeamDamageTakenMult().modifyMult(id, 0.0f);
		stats.getMissileDamageTakenMult().modifyMult(id, 0.0f);
		stats.getEmpDamageTakenMult().modifyMult(id, 0.01f);
				
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		FluxTrackerAPI flux = (FluxTrackerAPI) ship.getFluxTracker();
		flux.decreaseFlux(ship.getHullSpec().getFluxDissipation() * stats.getHardFluxDissipationFraction().getModifiedValue() * 0.03f);
		List<DamagingProjectileAPI> projectiles = combat.getProjectilesWithinRange(ship.getLocation(), ship.getShieldRadiusEvenIfNoShield());
		for (DamagingProjectileAPI p : projectiles) {
			if (p.getDamageTarget() == ship) {
				if (p.getSpawnType() == ProjectileSpawnType.BEAM) {					
					flux.increaseFlux(p.getDamageAmount() * p.getDamageType().getShieldMult() * stats.getShieldDamageTakenMult().getModifiedValue() * 0.75f, false);					
				} 
				else {
					flux.increaseFlux(p.getDamageAmount() * p.getDamageType().getShieldMult() * stats.getShieldDamageTakenMult().getModifiedValue() * 0.75f, true);
				}
			}
		}
		List<MissileAPI> missiles = combat.getMissilesWithinRange(ship.getLocation(), ship.getShieldRadiusEvenIfNoShield());
		for (MissileAPI m : missiles) {			
			//log.info("missile damage " + m.getDamageTarget());
			if (m.getDamageTarget() == ship) {				
				flux.increaseFlux(m.getDamageAmount() * m.getDamageType().getShieldMult() * stats.getShieldDamageTakenMult().getModifiedValue() * 0.75f, true);				
			}
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		//stats.getHullDamageTakenMult().unmodify(id);
		//stats.getArmorDamageTakenMult().unmodify(id);
		stats.getProjectileDamageTakenMult().unmodify(id);
		stats.getBeamDamageTakenMult().unmodify(id);
		stats.getMissileDamageTakenMult().unmodify(id);		
		stats.getEmpDamageTakenMult().unmodify(id);
	}
	
	
//	public StatusData getStatusData(int index, State state, float effectLevel) {
//		float mult = (Float) mag.get(HullSize.CRUISER);
//		if (stats.getVariant() != null) {
//			mult = (Float) mag.get(stats.getVariant().getHullSize());
//		}
//		effectLevel = 1f;
//		float percent = (1f - INCOMING_DAMAGE_MULT) * effectLevel * 100;
//		if (index == 0) {
//			return new StatusData((int) percent + "% less damage taken", false);
//		}
//		return null;
//	}
}
