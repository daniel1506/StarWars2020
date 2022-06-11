package data.shipsystems.scripts;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.CombatTaskManagerAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.CombatAssignmentType;
import com.fs.starfarer.api.SoundAPI;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;


public class RequestEscortRebelStats extends BaseShipSystemScript {
	
	private CombatEngineAPI engine = Global.getCombatEngine();
	protected boolean alreadyExecuted;
	
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		ship = (ShipAPI) stats.getEntity();
		CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOriginalOwner());
		CombatTaskManagerAPI task = FleetManager.getTaskManager(false);
		DeployedFleetMemberAPI target = FleetManager.getDeployedFleetMember(ship);
		if(!alreadyExecuted && ship == Global.getCombatEngine().getPlayerShip()) {			
			Global.getSoundPlayer().playSound("sw_system_request_reinforcement", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());			
		}
		if (engine.getPlayerShip().getOriginalOwner() == ship.getOriginalOwner() && alreadyExecuted == false) {
			Vector2f jumpPoint1 = new Vector2f();
			Vector2f jumpPoint2 = new Vector2f();
			jumpPoint1.set(ship.getLocation().getX() + ship.getShieldRadiusEvenIfNoShield() + 70f, ship.getLocation().getY() + ship.getShieldRadiusEvenIfNoShield() + 70f);
			jumpPoint2.set(ship.getLocation().getX() - ship.getShieldRadiusEvenIfNoShield() - 70f, ship.getLocation().getY() + ship.getShieldRadiusEvenIfNoShield() + 70f);
			if (ship.getHullSize() == HullSize.CAPITAL_SHIP) {
			ShipAPI escort1 = FleetManager.spawnShipOrWing("sw_cr90_reinforcement_rebel", jumpPoint1, 90f, 1.5f);
			ShipAPI escort2 = FleetManager.spawnShipOrWing("sw_cr90_reinforcement_rebel", jumpPoint2, 90f, 1.5f);
			task.giveAssignment(FleetManager.getDeployedFleetMember(escort1), task.createAssignment(CombatAssignmentType.LIGHT_ESCORT, target, false), false);
			task.giveAssignment(FleetManager.getDeployedFleetMember(escort2), task.createAssignment(CombatAssignmentType.LIGHT_ESCORT, target, false), false);
			}
			else {
				ShipAPI escort1 = FleetManager.spawnShipOrWing("sw_hammerhead_reinforcement_rebel", jumpPoint1, 90f, 1.5f);
				task.giveAssignment(FleetManager.getDeployedFleetMember(escort1), task.createAssignment(CombatAssignmentType.LIGHT_ESCORT, target, false), false);
			}
		}
		else if (engine.getPlayerShip().getOriginalOwner() != ship.getOriginalOwner() && alreadyExecuted == false) {
			Vector2f jumpPoint1 = new Vector2f();
			Vector2f jumpPoint2 = new Vector2f();
			jumpPoint1.set(ship.getLocation().getX() - ship.getShieldRadiusEvenIfNoShield() - 70f, ship.getLocation().getY() - ship.getShieldRadiusEvenIfNoShield() + 70f);
			jumpPoint2.set(ship.getLocation().getX() + ship.getShieldRadiusEvenIfNoShield() + 70f, ship.getLocation().getY() - ship.getShieldRadiusEvenIfNoShield() - 70f);
			if (ship.getHullSize() == HullSize.CAPITAL_SHIP) {
				ShipAPI escort1 = FleetManager.spawnShipOrWing("sw_cr90_reinforcement_rebel", jumpPoint1, 270f, 1.5f);
				ShipAPI escort2 = FleetManager.spawnShipOrWing("sw_cr90_reinforcement_rebel", jumpPoint2, 270f, 1.5f);
				task.giveAssignment(FleetManager.getDeployedFleetMember(escort1), task.createAssignment(CombatAssignmentType.LIGHT_ESCORT, target, false), false);
				task.giveAssignment(FleetManager.getDeployedFleetMember(escort2), task.createAssignment(CombatAssignmentType.LIGHT_ESCORT, target, false), false);
			}
			else {
				ShipAPI escort1 = FleetManager.spawnShipOrWing("sw_hammerhead_reinforcement_rebel", jumpPoint1, 270f, 1.5f);
				task.giveAssignment(FleetManager.getDeployedFleetMember(escort1), task.createAssignment(CombatAssignmentType.LIGHT_ESCORT, target, false), false);
			}
		}		
		alreadyExecuted = true;
	}
	
	public void unapply(MutableShipStatsAPI stats, String id) {
		alreadyExecuted = false;
	}
	
}
