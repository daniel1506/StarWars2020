package data.shipsystems.scripts;

import java.util.List;
import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;

public class ProbeDroidStats extends BaseShipSystemScript {

	private CombatEngineAPI engine = Global.getCombatEngine();
	protected BattleObjectiveAPI objective;
	protected int objectivecaptured = 0;
	protected boolean alreadyExecuted;
	
	protected void playSoundEffect(ShipAPI ship) {
		if (ship == Global.getCombatEngine().getPlayerShip()) {
			Global.getSoundPlayer().playSound("sw_system_probe_droid", 1.0f, 1.0f, ship.getLocation(), ship.getVelocity());
		};
		return;
	}
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		ShipAPI ship = null;
		ship = (ShipAPI) stats.getEntity();
		if (!alreadyExecuted) {
			playSoundEffect(ship);
			List<BattleObjectiveAPI> objectives = engine.getObjectives();
			for (BattleObjectiveAPI o : objectives) {				
				if (o.getOwner() == 100) {
					o.setOwner(ship.getOriginalOwner());
					objective = o;
					objectivecaptured = 1;
					break;
				}
				else if (o.getOwner() != ship.getOriginalOwner() && o.getOwner() != 100) {
					Random rand = new Random();
					int successrate = rand.nextInt(9);
					if (successrate > 3) {
						o.setOwner(ship.getOriginalOwner());						
						objective = o;
						objectivecaptured = 1;
					}
					else objectivecaptured = 2;
					break;
				}	
			}				
			alreadyExecuted = true;
		}
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		alreadyExecuted = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		if (index == 0) {
			if (objectivecaptured == 1) {
				return new StatusData(objective.getDisplayName() + " successfully captured", false);
			}
			else if (objectivecaptured == 2) {
				return new StatusData("Probe droid failed to capture enemy objective", false);
			}
			else {
				return new StatusData("No valid objective available", false);
			}
		}		
		return null;
	}
}
