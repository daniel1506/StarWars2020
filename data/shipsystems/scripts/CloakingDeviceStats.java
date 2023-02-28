package data.shipsystems.scripts;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.PhaseCloakSystemAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class CloakingDeviceStats extends BaseShipSystemScript {
	
public static Color JITTER_COLOR = new Color(255,175,255,255);
	public static float JITTER_FADE_TIME = 0.5f;
	
	public static float SHIP_ALPHA_MULT = 0.25f;
	public static float VULNERABLE_FRACTION = 0f;
	public static float INCOMING_DAMAGE_MULT = 0.25f;
	
	
	public static float MAX_TIME_MULT = 3f;
	
	public static boolean FLUX_LEVEL_AFFECTS_SPEED = true;
	public static float MIN_SPEED_MULT = 0.33f;
	public static float BASE_FLUX_LEVEL_FOR_MIN_SPEED = 0.5f;
	
	private boolean alreadyExecuted = false;
	protected Object STATUSKEY1 = new Object();
	protected Object STATUSKEY2 = new Object();
	protected Object STATUSKEY3 = new Object();
	protected Object STATUSKEY4 = new Object();
	
	
	public static float getMaxTimeMult(MutableShipStatsAPI stats) {
		return 1f + (MAX_TIME_MULT - 1f) * stats.getDynamic().getValue(Stats.PHASE_TIME_BONUS_MULT);
	}
	
	protected boolean isDisruptable(ShipSystemAPI cloak) {
		return cloak.getSpecAPI().hasTag(Tags.DISRUPTABLE);
	}
	
	protected float getDisruptionLevel(ShipAPI ship) {
		//return disruptionLevel;
		//if (true) return 0f;
		if (FLUX_LEVEL_AFFECTS_SPEED) {
			float threshold = ship.getMutableStats().getDynamic().getMod(
					Stats.PHASE_CLOAK_FLUX_LEVEL_FOR_MIN_SPEED_MOD).computeEffective(BASE_FLUX_LEVEL_FOR_MIN_SPEED);
			if (threshold <= 0) return 1f;
			float level = ship.getHardFluxLevel() / threshold;
			if (level > 1f) level = 1f;
			return level;
		}
		return 0f;
	}
	
	protected void maintainStatus(ShipAPI playerShip, State state, float effectLevel) {
		float level = effectLevel;
		float f = VULNERABLE_FRACTION;
		
		ShipSystemAPI cloak = playerShip.getSystem();
		if (cloak == null) return;
		
		if (level > f) {
//			Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY1,
//					cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "can not be hit", false);
			Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY2,
					cloak.getSpecAPI().getIconSpriteName(), cloak.getDisplayName(), "time flow altered", false);
		} else {
		}
		
		if (FLUX_LEVEL_AFFECTS_SPEED) {
			if (level > f) {
				if (getDisruptionLevel(playerShip) <= 0f) {
					Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY3,
							cloak.getSpecAPI().getIconSpriteName(), "phase coils stable", "top speed at 100%", false);
				} else {
					//String disruptPercent = "" + (int)Math.round((1f - disruptionLevel) * 100f) + "%";
					//String speedMultStr = Strings.X + Misc.getRoundedValue(getSpeedMult());
					String speedPercentStr = (int) Math.round(getSpeedMult(playerShip, effectLevel) * 100f) + "%";
					Global.getCombatEngine().maintainStatusForPlayerShip(STATUSKEY3,
							cloak.getSpecAPI().getIconSpriteName(),
							//"phase coils at " + disruptPercent, 
							"phase coil stress", 
							"top speed at " + speedPercentStr, true);
				}
			}
		}
	}
	
	public float getSpeedMult(ShipAPI ship, float effectLevel) {
		if (getDisruptionLevel(ship) <= 0f) return 1f;
		return MIN_SPEED_MULT + (1f - MIN_SPEED_MULT) * (1f - getDisruptionLevel(ship) * effectLevel); 
	}
	
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
			player = ship == Global.getCombatEngine().getPlayerShip();
			id = id + "_" + ship.getId();
		} else {
			return;
		}
		
		
		if (player) {
			maintainStatus(ship, state, effectLevel);		
		}
		
		if (Global.getCombatEngine().isPaused()) {
			return;
		}
		
		if (ship.getPhaseCloak() != null) {
			ship.getPhaseCloak().deactivate();
			ship.setDefenseDisabled(true);			
		}
		else if (ship.getShield() != null) {
			ship.setDefenseDisabled(true);
		}
		
		ShipSystemAPI cloak = ship.getSystem();
		if (cloak == null) return;
		
//		if (isDisruptable(cloak)) {
//			advanceDisrupt(Global.getCombatEngine().getElapsedInLastFrame(),
//					ship, cloak, stats, id, state, effectLevel);
//		}
		
		//if (FLUX_LEVEL_AFFECTS_SPEED) {
		//	if (state == State.ACTIVE || state == State.OUT || state == State.IN) {
		//		float mult = getSpeedMult(ship, effectLevel);
		//		if (mult < 1f) {
		//			stats.getMaxSpeed().modifyMult(id + "_2", mult);
		//		} else {
		//			stats.getMaxSpeed().unmodifyMult(id + "_2");
		//		}
		//		((PhaseCloakSystemAPI)cloak).setMinCoilJitterLevel(getDisruptionLevel(ship));
		//	}
		//}
		
		if (state == State.COOLDOWN || state == State.IDLE) {
			unapply(stats, id);
			return;
		}
		
		//float speedPercentMod = stats.getDynamic().getMod(Stats.PHASE_CLOAK_SPEED_MOD).computeEffective(0f);
		//float accelPercentMod = stats.getDynamic().getMod(Stats.PHASE_CLOAK_ACCEL_MOD).computeEffective(0f);
		//stats.getMaxSpeed().modifyPercent(id, speedPercentMod * effectLevel);
		//stats.getAcceleration().modifyPercent(id, accelPercentMod * effectLevel);
		//stats.getDeceleration().modifyPercent(id, accelPercentMod * effectLevel);
		
		//float speedMultMod = stats.getDynamic().getMod(Stats.PHASE_CLOAK_SPEED_MOD).getMult();
		//float accelMultMod = stats.getDynamic().getMod(Stats.PHASE_CLOAK_ACCEL_MOD).getMult();
		//stats.getMaxSpeed().modifyMult(id, speedMultMod * effectLevel);
		//stats.getAcceleration().modifyMult(id, accelMultMod * effectLevel);
		//stats.getDeceleration().modifyMult(id, accelMultMod * effectLevel);
		
		float level = effectLevel;
		

		
		float jitterLevel = 0f;
		float jitterRangeBonus = 0f;
		float levelForAlpha = level;	
		
		if (state == State.IN || state == State.ACTIVE) {
			if (!alreadyExecuted && player) {
				Global.getSoundPlayer().playSound("sw_system_cloaking_device", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
				alreadyExecuted = true;
			}
			ship.setPhased(true);
			levelForAlpha = level;
		} else if (state == State.OUT) {
			if (alreadyExecuted && player) {
				Global.getSoundPlayer().playSound("sw_system_cloaking_device_deactivate", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
				alreadyExecuted = false;
			}
			if (level > 0.5f) {
				ship.setPhased(true);
			} else {
				ship.setPhased(false);
			}
			levelForAlpha = level;
		}
		
//		ship.setJitter(JITTER_COLOR, jitterLevel, 1, 0, 0 + jitterRangeBonus);
//		ship.setJitterUnder(JITTER_COLOR, jitterLevel, 11, 0f, 7f + jitterRangeBonus);
		//ship.getEngineController().fadeToOtherColor(this, spec.getEffectColor1(), new Color(0,0,0,0), jitterLevel, 1f);
		//ship.getEngineController().extendFlame(this, -0.25f, -0.25f, -0.25f);
		
		ship.setExtraAlphaMult(1f - (1f - SHIP_ALPHA_MULT) * levelForAlpha);
		ship.setApplyExtraAlphaToEngines(true);
		
		
		//float shipTimeMult = 1f + (MAX_TIME_MULT - 1f) * levelForAlpha;
		float extra = 0f;
//		if (isDisruptable(cloak)) {
//			extra = disruptionLevel;
//		}
		//float shipTimeMult = 1f + (getMaxTimeMult(stats) - 1f) * levelForAlpha * (1f - extra);
		//stats.getTimeMult().modifyMult(id, shipTimeMult);
		//if (player) {
		//	Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / shipTimeMult);
		//} else {
		//	Global.getCombatEngine().getTimeMult().unmodify(id);
		//}
		
	}


	public void unapply(MutableShipStatsAPI stats, String id) {
		
		ShipAPI ship = null;
		//boolean player = false;
		if (stats.getEntity() instanceof ShipAPI) {
			ship = (ShipAPI) stats.getEntity();
			//player = ship == Global.getCombatEngine().getPlayerShip();
			//id = id + "_" + ship.getId();
		} else {
			return;
		}
		
		//Global.getCombatEngine().getTimeMult().unmodify(id);
		//stats.getTimeMult().unmodify(id);
		
		//stats.getMaxSpeed().unmodify(id);
		//stats.getMaxSpeed().unmodifyMult(id + "_2");
		//stats.getAcceleration().unmodify(id);
		//stats.getDeceleration().unmodify(id);
		
		ship.setPhased(false);
		ship.setExtraAlphaMult(1f);
		
		ShipSystemAPI cloak = ship.getSystem();
		if (cloak != null) {
			((PhaseCloakSystemAPI)cloak).setMinCoilJitterLevel(0f);
		}
		ship.setDefenseDisabled(false);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		return null;
	}

	
}