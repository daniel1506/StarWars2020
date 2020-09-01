package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class KyberCrystalFocus extends BaseHullMod {

	private static final float DAMAGE_BONUS_PERCENT = 10f;
	private static final float RANGE_BONUS = 20f;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS_PERCENT);
		stats.getEnergyWeaponRangeBonus().modifyPercent(id, RANGE_BONUS);		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) DAMAGE_BONUS_PERCENT + "%";
		if (index == 1) return "" + (int) RANGE_BONUS + "%";
		return null;
	}

}
