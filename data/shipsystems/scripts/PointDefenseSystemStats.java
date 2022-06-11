package data.shipsystems.scripts;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SoundAPI;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class PointDefenseSystemStats extends BaseShipSystemScript {

	public static final float DAMAGE_BONUS = 100f;
	public static final float ROF_BONUS = 1.5f;
	protected List <WeaponAPI> weaponList = new ArrayList<WeaponAPI>();;
	protected boolean alreadyExecuted;
	
	protected void playSoundEffect(ShipAPI ship) {
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getSoundPlayer().playSound("sw_system_point_defense", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
		};
		return;
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		ship = (ShipAPI) stats.getEntity();
		if(!alreadyExecuted) {
			playSoundEffect(ship);
			List weapons = ship.getAllWeapons();
			Iterator iter = weapons.iterator();
			while (iter.hasNext()) {
				WeaponAPI weapon = (WeaponAPI)iter.next();
				if (!weapon.hasAIHint(AIHints.PD)) {
					if (weapon.getType() == WeaponType.ENERGY) {
						weapon.setPD(true);
						weaponList.add(weapon);
					}					
				}
				//boolean sizeMatches = weapon.getSize() == WeaponSize.SMALL;
			}
			alreadyExecuted = true;
		}
		//stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
		stats.getEnergyRoFMult().modifyMult(id, ROF_BONUS);
		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
		stats.getDamageToFighters().modifyPercent(id, DAMAGE_BONUS);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		//stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).unmodify(id);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).unmodify(id);
		stats.getEnergyRoFMult().unmodify(id);
		stats.getDamageToMissiles().unmodify(id);
		stats.getDamageToFighters().unmodify(id);
		for (WeaponAPI w : weaponList) {
			w.setPD(false);
		}
		weaponList.clear();
		alreadyExecuted = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		//float bonusPercent = DAMAGE_BONUS;
		if (index == 0) {
			return new StatusData("Switched all lasers to point defense", false);
		}
		else if (index == 1) {
			return new StatusData("+" + (int) DAMAGE_BONUS + "% damage to missiles", false);
		}
		else if (index == 2) {
			return new StatusData("+" + (int) DAMAGE_BONUS + "% damage to fighters", false);
		}
		return null;
	}
}
