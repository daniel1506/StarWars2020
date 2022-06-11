package data.hullmods;

import java.util.Iterator;
import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class TargetingComputer extends BaseHullMod {

	public static final float DAMAGE_BONUS = 100f;
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getRecoilPerShotMultSmallWeaponsOnly().modifyMult(id, 0f);
		//stats.getRecoilDecayMult().modifyMult(id, 10f);
		//stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);		
		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
		stats.getDamageToFighters().modifyPercent(id, DAMAGE_BONUS);
		//stats.getProjectileSpeedMult().modifyMult(id, 100f);
		//stats.getWeaponTurnRateBonus().modifyMult(id, 2f);
		//stats.getDamageToFighters().modifyPercent(id, 50f);
		//stats.getProjectileSpeedMult().modifyPercent(id, 50f);
		//stats.getAutofireAimAccuracy().modifyFlat(id, 1f);
	}

	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(DAMAGE_BONUS) + "%";
		return null;
	}


}
