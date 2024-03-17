package data.scripts.combat.ai;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAIConfig;
import com.fs.starfarer.api.combat.ShipAIPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;

public class WingRTBAI implements ShipAIPlugin {
   private ShipAPI ship;
   private WeaponAPI dock;
   private CombatEngineAPI engine;
   private ShipwideAIFlags flags;
   private ShipAIConfig config;

   public WingRTBAI(ShipAPI _ship, WeaponAPI _dock, ShipwideAIFlags _flags, CombatEngineAPI _engine, ShipAIConfig _config) {
      this.ship = _ship;
      this.dock = _dock;
      this.flags = _flags;
      this.engine = _engine;
      this.config = _config;
   }

   public void setDoNotFireDelay(float amount) {
   }

   public void forceCircumstanceEvaluation() {
   }

   public void advance(float amount) {
      float distance = MathUtils.getDistance(this.ship.getLocation(), this.dock.getLocation());
      boolean docked = distance <= 60.0F;
      if (!docked) {
         float facing = this.ship.getFacing();
         facing = MathUtils.getShortestRotation(facing, VectorUtils.getAngle(this.ship.getLocation(), this.dock.getLocation()));
         float _distance = distance;
         if (distance > 1000.0F) {
            _distance = 1000.0F;
         }

         if (distance < 100.0F) {
            _distance = 100.0F;
         }

         float multiplier = (1450.0F - _distance) / 450.0F;
         float turnrate = this.ship.getMaxTurnRate() * multiplier;
         this.ship.setAngularVelocity(Math.min(turnrate, Math.max(-turnrate, facing * 5.0F)));
         this.ship.giveCommand(ShipCommand.ACCELERATE, (Object)null, 0);
      }
   }

   public boolean needsRefit() {
      return false;
   }

   public ShipwideAIFlags getAIFlags() {
      return this.flags;
   }

   public void cancelCurrentManeuver() {
   }

   public ShipAIConfig getConfig() {
      return this.config;
   }
}