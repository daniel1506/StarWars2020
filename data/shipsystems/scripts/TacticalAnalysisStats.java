package data.shipsystems.scripts;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SoundAPI;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

import java.util.Random;

public class TacticalAnalysisStats extends BaseShipSystemScript {

	public static final Object KEY_JITTER = new Object();
	public static final Color JITTER_UNDER_COLOR = new Color(0,70,255,200);
	public static final Color JITTER_COLOR = new Color(0,102,255,100);
	
	public static final float DAMAGE_BONUS_PERCENT = 20f;
	public static final float SPEED_BONUS = 0.25f;
	public static final float SHIELD_BONUS = 0.4f;
	protected boolean alreadyExecuted;
	protected int effect;
	
	protected void playSoundEffect(ShipAPI ship) {
		Random rand = new Random();
		effect = rand.nextInt(9);
		if (ship == Global.getCombatEngine().getPlayerShip() && effect <= 2) {
			Global.getSoundPlayer().playSound("sw_system_tactical_analysis1", 1.0f, 0.8f, ship.getLocation(), ship.getVelocity());
		}
		else if (ship == Global.getCombatEngine().getPlayerShip() && effect <= 5 && effect >= 3) {
			Global.getSoundPlayer().playSound("sw_system_tactical_analysis2", 1.0f, 0.8f, ship.getLocation(), ship.getVelocity());
		}
		else if (ship == Global.getCombatEngine().getPlayerShip() && effect <= 8 && effect >= 6) {
			Global.getSoundPlayer().playSound("sw_system_tactical_analysis3", 1.0f, 0.8f, ship.getLocation(), ship.getVelocity());
		}
		else if (ship == Global.getCombatEngine().getPlayerShip() && effect == 9) {
			Global.getSoundPlayer().playSound("sw_system_tactical_analysis4", 1.0f, 0.8f, ship.getLocation(), ship.getVelocity());
		}
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float jitterLevel = effectLevel;
		float maxRangeBonus = 5f;
		float jitterRangeBonus = jitterLevel * maxRangeBonus;
		
		ShipAPI ship = null;
		ship = (ShipAPI) stats.getEntity();
		if (!alreadyExecuted) {
			playSoundEffect(ship);
			alreadyExecuted = true;
		}
		if (effect <= 2 || effect == 9) {
			float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
			stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);
		}
		if ((effect <= 5 && effect >= 3) || effect == 9) {
			stats.getShieldDamageTakenMult().modifyMult(id, 1f - SHIELD_BONUS * effectLevel);
			if (stats.getProjectileDamageTakenMult().getModifiedValue() == 0f) {
				ship.setJitterUnder(KEY_JITTER, JITTER_COLOR, jitterLevel, 5, 0f, jitterRangeBonus);
				ship.setJitter(KEY_JITTER, JITTER_UNDER_COLOR, jitterLevel, 2, 0f, 0 + jitterRangeBonus * 1f);
			}			
		}			
		if ((effect <= 8 && effect >= 6) || effect == 9) {
			stats.getMaxSpeed().modifyMult(id, 1f + SPEED_BONUS * effectLevel);
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponDamageMult().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getMaxSpeed().unmodify(id);
		alreadyExecuted = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float damageBonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		float damageTakenPercent = SHIELD_BONUS * effectLevel;
		float speedBonusPercent = SPEED_BONUS * effectLevel;
		if (effect <= 2) {
			if (index == 0) {
				return new StatusData("+" + (int) damageBonusPercent + "% energy weapon damage", false);
			}
		}
		else if (effect <= 5 && effect >= 3) {
			if (index == 0) {
				return new StatusData("shield absorbs " + damageTakenPercent + "x damage", false);
			}
		}
		else if (effect <= 8 && effect >= 6) {
			if (index == 0) {
				return new StatusData("+" + speedBonusPercent * 100f + "% top speed", false);
			}
		}
		else if (effect == 9) {
			if (index == 0) {
				return new StatusData("+" + (int) damageBonusPercent + "% energy weapon damage", false);
			} else if (index == 1) {
				return new StatusData("shield absorbs " + damageTakenPercent + "x damage", false);
			} else if (index == 2) {
				return new StatusData("+" + speedBonusPercent * 100f + "% top speed", false);
			}
		}
		return null;
	}
}
