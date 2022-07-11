package data.hullmods;

import org.lwjgl.util.vector.Vector2f;
import java.util.ArrayList;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.BattleCreationContext;

public class InternalDockyards extends BaseHullMod {

	public static final float REPAIR_RATE_BONUS = 25f;
	public static final float CR_RECOVERY_BONUS = 25f;
	private List<ShipAPI> shipList = new ArrayList<ShipAPI>();
	private boolean escortSpawned = false;
	private BattleCreationContext context;
	//public static final float RECOVERY_BONUS = 75f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {		
		
		//stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
		//stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
//		stats.getSuppliesToRecover().modifyPercent(id, LOGISTICS_PENALTY);
//		stats.getSuppliesPerMonth().modifyPercent(id, LOGISTICS_PENALTY);
	}
	
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (!ship.isAlive()) return;
		
		if (!shipList.isEmpty() && context != null) {
			if (Global.getCombatEngine().getContext() != context) {
				shipList.clear();
				//escortSpawned = false;
			}		
		}
		
		if (!shipList.contains(ship)) {
			context = Global.getCombatEngine().getContext();
			CombatFleetManagerAPI FleetManager = new Global().getCombatEngine().getFleetManager(ship.getOriginalOwner());
			//DeployedFleetMemberAPI target = FleetManager.getDeployedFleetMember(ship);
			if (isInPlayerFleet(ship)) {
				Vector2f jumpPoint1 = new Vector2f();
				Vector2f jumpPoint2 = new Vector2f();
				jumpPoint1.set(ship.getLocation().getX() + ship.getShieldRadiusEvenIfNoShield() + 50f, ship.getLocation().getY() + ship.getShieldRadiusEvenIfNoShield() + 50f);
				jumpPoint2.set(ship.getLocation().getX() - ship.getShieldRadiusEvenIfNoShield() - 50f, ship.getLocation().getY() + ship.getShieldRadiusEvenIfNoShield() + 50f);
				ShipAPI escort1 = FleetManager.spawnShipOrWing("sw_resurgent_reinforcement_firstorder", jumpPoint1, 90f, 1.0f);
				ShipAPI escort2 = FleetManager.spawnShipOrWing("sw_resurgent_reinforcement_firstorder", jumpPoint2, 90f, 1.0f);
			}
			else {
				Vector2f jumpPoint1 = new Vector2f();
				Vector2f jumpPoint2 = new Vector2f();
				jumpPoint1.set(ship.getLocation().getX() - ship.getShieldRadiusEvenIfNoShield() - 50f, ship.getLocation().getY() - ship.getShieldRadiusEvenIfNoShield() + 50f);
				jumpPoint2.set(ship.getLocation().getX() + ship.getShieldRadiusEvenIfNoShield() + 50f, ship.getLocation().getY() - ship.getShieldRadiusEvenIfNoShield() - 50f);
				ShipAPI escort1 = FleetManager.spawnShipOrWing("sw_resurgent_reinforcement_firstorder", jumpPoint1, 270f, 1.5f);
				ShipAPI escort2 = FleetManager.spawnShipOrWing("sw_resurgent_reinforcement_firstorder", jumpPoint2, 270f, 1.5f);
			}
			shipList.add(ship);
			//escortSpawned = true;
		}	
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) REPAIR_BONUS + "%";
		//if (index == 0) return "" + (int) REPAIR_RATE_BONUS + "%";
		//if (index == 1) return "" + (int) CR_RECOVERY_BONUS + "%";
		return null;
	}


}
