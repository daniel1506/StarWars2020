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
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import org.apache.log4j.Logger;

public class ThermalShieldStats extends BaseShipSystemScript {
	
	public Logger log = Logger.getLogger(this.getClass());
	
	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,205,0,75);
	public static final Color JITTER_COLOR = new Color(0,205,0,10);
	public static final Color TEXT_COLOR = new Color(0,205,0,150);
	protected boolean alreadyExecuted;
	
	protected void playSoundEffect(ShipAPI ship) {
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getSoundPlayer().playSound("sw_system_thermal_shield", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
		};
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {			
		
		float jitterLevel = effectLevel;
		float maxRangeBonus = 5f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		ShipAPI ship = null;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();			
		}
		if (!alreadyExecuted) {
			playSoundEffect(ship);
			alreadyExecuted = true;
		}
		stats.getProjectileDamageTakenMult().modifyMult(id, 0.0f);
		stats.getBeamDamageTakenMult().modifyMult(id, 0.0f);
		stats.getMissileDamageTakenMult().modifyMult(id, 0.0f);
		stats.getEmpDamageTakenMult().modifyMult(id, 0.0f);
		ship.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
		ship.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
		if (ship != Global.getCombatEngine().getPlayerShip() && ship.getFluxLevel() > 0.7f) {
			ship.getAIFlags().setFlag(AIFlags.BACK_OFF, 9f) ;
		}
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getProjectileDamageTakenMult().unmodify(id);
		stats.getBeamDamageTakenMult().unmodify(id);
		stats.getMissileDamageTakenMult().unmodify(id);		
		stats.getEmpDamageTakenMult().unmodify(id);
		alreadyExecuted = false;
	}
	
}
