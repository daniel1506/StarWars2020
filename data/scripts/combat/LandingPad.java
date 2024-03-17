package data.scripts.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.ShipAIConfig;
import com.fs.starfarer.api.combat.ShipAIPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import java.util.Iterator;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;

import org.lwjgl.util.vector.Vector2f;

import data.scripts.combat.ai.WingRTBAI;

public class LandingPad implements EveryFrameWeaponEffectPlugin {
   //private final String hull_id = "uaf_wing_rafflesya"; // hull_id of the fighter
   private final String customData_key = "wasWingDocked";
   private final String customDataAI_key = "WingDockingAI";
   private boolean dockedFromSequence = false;
   private boolean dockedFromLanding = false;
   private IntervalUtil _rearmTimer = new IntervalUtil(19.0F, 21.0F);
   //private final IntervalUtil healingTimer = new IntervalUtil(1.45F, 1.5F);
   private boolean isDocking = false;
   private boolean stillAlive = true;

   private boolean shouldRearm(ShipAPI ship) {
      List<WeaponAPI> weapons = ship.getAllWeapons();
      CombatEngineAPI eng = Global.getCombatEngine();
      int weaponCount = 0;
      int totalWeaponWithNoAmmunition = 0;
	  
	  for (WeaponAPI weapon : weapons) {
		if (weapon.usesAmmo()) {
            weaponCount++;
            if (weapon.getAmmo() == 0) {
               totalWeaponWithNoAmmunition++;
            }
        }
	  }

      return weaponCount != 0 && weaponCount == totalWeaponWithNoAmmunition && ship.isAlive() && (ship.isFighter() || ship.isDrone()) && !ship.isHulk() && !ship.isShuttlePod() && eng.isEntityInPlay(ship) && !ship.isRetreating();
   }

   private void swapSprite(WeaponAPI weap, boolean dockStatus) {
   }

   //private void setStatus(ShipAPI ship, String message) {
   //   ship.setCustomData("uafRafflesyaControlStatus", message);
   //}

   public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
      Vector2f point = weapon.getLocation();
      ShipAPI parent = weapon.getShip();
      List<FighterWingAPI> fighters = parent.getAllWings();
      this.stillAlive = false;
      Iterator var7 = fighters.iterator();

      while(true) {
         while(true) {
            FighterWingAPI fighter;
            ShipAPI leader;
            do {
               do {
                  do {
                     if (!var7.hasNext()) {
                     //   if (!this.stillAlive) {
                     //      this.setStatus(parent, "Lost Contact..");
                     //   }
                        return;
                     }

                     fighter = (FighterWingAPI)var7.next();
                     leader = fighter.getLeader();
                  } while(leader == null);

                  if (leader.isHulk()) {
                     if (MathUtils.getDistance(leader, parent) <= parent.getCollisionRadius()) {
                        leader.setCollisionClass(CollisionClass.NONE);
                     } else {
                        leader.setCollisionClass(CollisionClass.SHIP);
                     }
                  }
               } while(!leader.getHullSpec().getHullId().equals("uaf_wing_rafflesya")); //equal hull_id of the fighter
            } while(!leader.isAlive());

            this.stillAlive = true;
            List<WeaponAPI> weapons = leader.getAllWeapons();
            //boolean isDetaching = parent.getSystem().isActive();
			boolean isDetaching = !parent.isPullBackFighters();
            boolean isReturning = parent.isPullBackFighters();
            boolean isRearming = this.shouldRearm(leader);
            boolean isInDockingProcedure = leader.getCustomData().containsKey("WingAIBackup");
            if (isDetaching) {
               leader.getWing().stopReturning(leader);
            }

            if (isDetaching && !isReturning && !this.isDocking && !isRearming || !parent.isAlive()) {
               if (isInDockingProcedure) {
                  ShipAIPlugin normalAI = (ShipAIPlugin)leader.getCustomData().get("WingAIBackup");
                  leader.setShipAI(normalAI);
                  leader.getCustomData().remove("WingAIBackup");
                  //Global.getSoundPlayer().playSound("uaf_rafflesya_takeoff_sfx", 1.0F, 1.0F, leader.getLocation(), new Vector2f(0.0F, 0.0F));
               }

               leader.getCustomData().remove("rearm_timer");
               leader.getCustomData().put("seWingDocked", false);
               this.swapSprite(weapon, false);
               //this.setStatus(parent, "Launching..");
            } else if (!MathUtils.isWithinRange(point, leader.getLocation(), 60.0F)) {
               //logging.log.info("Switching AI");
               this.isDocking = true;
               if (!leader.getCustomData().containsKey("WingAIBackup")) {
                  ShipAIPlugin dockAI = new WingRTBAI(leader, weapon, leader.getAIFlags(), Global.getCombatEngine(), (ShipAIConfig)null);
                  leader.setCustomData("WingAIBackup", leader.getShipAI());
                  leader.setShipAI(dockAI);
               }

               //this.setStatus(parent, "Initiating Docking..");
            } else {
               fighter.stopReturning(leader);
               //if (this.isDocking) {
               //   parent.getSystem().deactivate();
               //}

               Vector2f loc = leader.getLocation();
               loc.x = point.x;
               loc.y = point.y;
               if (leader.getFacing() == weapon.getCurrAngle()) {
                  leader.setFacing(weapon.getCurrAngle());
               }

               float diff_mult = 1.0F;
               float mtr = leader.getMaxTurnRate() / 27.0F;
               float diff = MathUtils.getShortestRotation(leader.getFacing(), weapon.getCurrAngle());
               if (diff < 0.0F) {
                  diff_mult = -1.0F;
               }

               float rotby = Math.min(Math.abs(diff), mtr);
               leader.setFacing(leader.getFacing() + rotby * diff_mult);
               leader.getCustomData().put("seWingDocked", true);
               if (!leader.getCustomData().containsKey("rearm_timer")) {
                  leader.setCustomData("rearm_timer", new IntervalUtil(19.0F, 21.0F));
                  //Global.getSoundPlayer().playSound("uaf_rafflesya_landing_sfx", 1.0F, 1.0F, leader.getLocation(), new Vector2f(0.0F, 0.0F));
               }

               this.swapSprite(weapon, true);
               //this.setStatus(parent, "Standby..");
               if (isRearming) {
                  IntervalUtil rearmTimer = (IntervalUtil)leader.getCustomData().get("rearm_timer");
                  rearmTimer.advance(amount);
                  if (rearmTimer.intervalElapsed()) {
                     //logging.log.info("Still rearming");
                     //boolean playSound = false;

                     //WeaponAPI _weap;
					 for (WeaponAPI _weap : weapons) {
						 if (_weap.usesAmmo()) {
							 _weap.setAmmo(_weap.getMaxAmmo());
						 }
					 }						 
                     //for(Iterator var22 = weapons.iterator(); var22.hasNext(); _weap.setAmmo(_weap.getMaxAmmo())) {
                     //   _weap = (WeaponAPI)var22.next();
                        //if (_weap.getAmmo() < _weap.getMaxAmmo()) {
                        //   playSound = true;
                        //}
                     //}

                     //leader.setHitpoints(leader.getMaxHitpoints());
                     //if (playSound) {
                     //   Global.getSoundPlayer().playSound("uaf_rafflesya_rearmed", 1.0F, 1.0F, leader.getLocation(), leader.getVelocity());
                     //}
                  }

                  //this.setStatus(parent, "Rearming..");
               }

               //this.healingTimer.advance(amount);
               //if (this.healingTimer.intervalElapsed()) {
               //   Float curr_hp = leader.getHitpoints();
               //   Float max_hp = leader.getMaxHitpoints();
               //   if (curr_hp < max_hp) {
               //      Float regen_hp = 2.0F;
               //      if (curr_hp + regen_hp > max_hp) {
               //         leader.setHitpoints(max_hp);
               //      } else {
               //         leader.setHitpoints(curr_hp + regen_hp);
               //      }
               //  }
               //}

               this.isDocking = false;
            }
         }
      }
   }
}