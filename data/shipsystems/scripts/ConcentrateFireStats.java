package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SoundAPI;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class ConcentrateFireStats extends BaseShipSystemScript {

	public static final float DAMAGE_BONUS_PERCENT = 30f;
	public static final float ROF_BONUS = 1.25f;
	protected boolean alreadyExecuted;
	
	protected void playSoundEffect(ShipAPI ship) {
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getSoundPlayer().playSound("sw_system_concentrate_fire", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
		};
		return;
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		ship = (ShipAPI) stats.getEntity();
		if(!alreadyExecuted) {
			playSoundEffect(ship);
			alreadyExecuted = true;
		}
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);		
		stats.getEnergyRoFMult().modifyMult(id, ROF_BONUS);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponDamageMult().unmodify(id);
		stats.getEnergyRoFMult().unmodify(id);
		alreadyExecuted = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		if (index == 0) {
			return new StatusData("+" + (int) bonusPercent + "% energy weapon damage", false);
		}
		else if (index == 1) {
			return new StatusData("energy weapon rate of fire +" + (int) Math.round((ROF_BONUS - 1f) * 100f) + "%", false);
		}
		return null;
	}
}
