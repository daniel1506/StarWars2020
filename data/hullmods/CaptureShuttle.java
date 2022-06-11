package data.hullmods;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.String;
import java.lang.Math;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import com.fs.starfarer.api.combat.StatBonus;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import org.apache.log4j.Logger;

public class CaptureShuttle extends BaseHullMod {
	
	public Logger log = Logger.getLogger(this.getClass());
	private String id;
	private int number = 0;
	public static final float COMBAT_SALVAGE = 80f;
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 1f);
		mag.put(HullSize.DESTROYER, 2f);
		mag.put(HullSize.CRUISER, 3f);
		mag.put(HullSize.CAPITAL_SHIP, 4f);
	}
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, java.lang.String id) {
		this.id=id;
		//if (!isInPlayerFleet(ship)) return;
		//if (stats.getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).getModifiedValue() <= 0.8f) {
		//	stats.getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).modifyFlat(id, COMBAT_SALVAGE * 0.01f);
		//}
	}
	
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (Global.getCombatEngine().isSimulation()) return;
		if (!isInPlayerFleet(ship)) return;
		if (!ship.isAlive()) return;
		
		if (Global.getCombatEngine().isCombatOver()) {
			number = 0;
		}

		CombatFleetManagerAPI fleet = new Global().getCombatEngine().getFleetManager(FleetSide.ENEMY);
		List<DeployedFleetMemberAPI> ships = fleet.getDeployedCopyDFM();
		for (DeployedFleetMemberAPI s : ships) {
			if (!s.isFighterWing() && s.getShip().isAlive()) {
				if (number >= ((Float) mag.get(ship.getHullSize())).intValue()) {
					return;
				}
				else {
					if (s.getShip().getMutableStats().getBreakProb().getModifiedValue() > 0.0f) {
						if (Math.random() >= 0.2f) {
							s.getShip().getMutableStats().getBreakProb().modifyMult(id, 0f);
							s.getShip().getMutableStats().getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).modifyFlat(id, 1000f);
							//log.info("Captured!");
						}
						number++;
					}
				}
			}
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) SHIELD_BONUS + "%";
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 4) return "" + (int) COMBAT_SALVAGE + "%";
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.hasLaunchBays();
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no launch bay!";
	}
}
