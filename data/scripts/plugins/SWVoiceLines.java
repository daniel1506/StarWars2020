package data.scripts.plugins;

import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatTaskManagerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.combat.MutableStat;

//import org.apache.log4j.Logger;

public class SWVoiceLines extends BaseEveryFrameCombatPlugin{
	
	private CombatEngineAPI engine;
	private FluxTrackerAPI tracker;
	private SoundPlayerAPI sound = Global.getSoundPlayer();
		
	boolean ShieldsBurnout = false;
	boolean FlamedOut = false;
	boolean ShieldsBurnoutSoundPlayed = false;
	boolean FlamedOutSoundPlayed = false;
	boolean FullRetreatSoundPlayed = false;
	boolean EnemyFullRetreatSoundPlayed = false;
	boolean isRegrouping = true;
	
	public void init(CombatEngineAPI engine)
    {
        this.engine=engine;
    }	
	
	public void advance(float amount, List events){
		
		if (engine == null) return;
        if (engine.isPaused()) return;
        if (Global.getCurrentState() != GameState.COMBAT) return;
		ShipAPI playerShip = engine.getPlayerShip();
		if (playerShip.getVariant().hasHullMod("strikeCraft")) return;
		//FluxTrackerAPI flux = playerShip.getFluxTracker();
		
		if (playerShip.getEngineController().isFlamedOut() == true && playerShip.isAlive())
		{
			if(FlamedOutSoundPlayed == false) 
			{
				sound.playSound("sw_imperial_flameout",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
				FlamedOutSoundPlayed = true;
			}		
		}
		else if (playerShip.getEngineController().isFlamedOut() == false)
		{
			FlamedOutSoundPlayed = false;
		}
		if (playerShip.getFluxTracker().isOverloaded() == true && playerShip.isAlive())
		{
			if(ShieldsBurnoutSoundPlayed == false) 
			{
				sound.playSound("sw_imperial_shields_burnout",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
				ShieldsBurnoutSoundPlayed = true;
			}
		}
		else if (playerShip.getVariant().hasHullMod("sw_deflector_shield") && playerShip.isDefenseDisabled()) {
			if(ShieldsBurnoutSoundPlayed == false) 
			{
				sound.playSound("sw_imperial_shields_burnout",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
				ShieldsBurnoutSoundPlayed = true;
			}
		}
		else if (playerShip.getFluxTracker().isOverloaded() == false && !playerShip.isDefenseDisabled())
		{			
			ShieldsBurnoutSoundPlayed = false;
		}	
		if (engine.getFleetManager(playerShip.getOriginalOwner()).getTaskManager(false).isInFullRetreat() && !FullRetreatSoundPlayed) {
			sound.playSound("sw_retreat",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
			FullRetreatSoundPlayed = true;
		}
		if (engine.isEnemyInFullRetreat() && !EnemyFullRetreatSoundPlayed) {
			sound.playSound("sw_enemy_retreat",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
			EnemyFullRetreatSoundPlayed = true;
		}
		if (!playerShip.isPullBackFighters() && isRegrouping != playerShip.isPullBackFighters()) {
			if (engine.isUIAutopilotOn()) {
				sound.playSound("sw_wings_attack",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
			}
			isRegrouping = false;
		}
		else if (playerShip.isPullBackFighters() && isRegrouping != playerShip.isPullBackFighters()) {
			if (engine.isUIAutopilotOn()) {
				sound.playSound("sw_wings_regroup",1f,1f,playerShip.getLocation(),playerShip.getVelocity());
			}
			isRegrouping = true;
		}
	}
}
