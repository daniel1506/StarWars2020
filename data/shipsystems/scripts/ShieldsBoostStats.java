package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SoundAPI;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class ShieldsBoostStats extends BaseShipSystemScript {
	
	public static final float DAMAGE_PERCENT = 80f;
	protected boolean alreadyExecuted;
	
	protected void playSoundEffect(ShipAPI ship) {
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getSoundPlayer().playUISound("sw_system_shields_boost", 1.0f, 0.7f);
		};
	}

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		ship = (ShipAPI) stats.getEntity();
		if(!alreadyExecuted) {
			playSoundEffect(ship);
			alreadyExecuted = true;
		}		
		stats.getShieldDamageTakenMult().modifyMult(id, 1f - .9f * effectLevel);
		
		stats.getShieldUpkeepMult().modifyMult(id, 0f);
		
		stats.getEnergyWeaponDamageMult().modifyPercent(id, -DAMAGE_PERCENT);
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getShieldAbsorptionMult().unmodify(id);
		stats.getShieldArcBonus().unmodify(id);
		stats.getShieldDamageTakenMult().unmodify(id);
		stats.getShieldTurnRateMult().unmodify(id);
		stats.getShieldUnfoldRateMult().unmodify(id);
		stats.getShieldUpkeepMult().unmodify(id);
		stats.getEnergyWeaponDamageMult().unmodify(id);
		alreadyExecuted = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			return new StatusData("shield absorbs 10x damage", false);
		}
//		else if (index == 1) {
//			return new StatusData("shield upkeep reduced to 0", false);
//		} else if (index == 2) {
//			return new StatusData("shield upkeep reduced to 0", false);
//		}
		if (index == 1) {
			return new StatusData("energy weapons output reduced by 80%", false);
		}			
		return null;
	}
}
