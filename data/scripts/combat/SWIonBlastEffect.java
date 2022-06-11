package data.scripts.combat;

import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

/**
 * IMPORTANT: will be multiple instances of this, as this doubles as the every frame effect and the on fire effect (same instance)
 * But also as the visual for each individual shot (created via onFire, using the non-default constructor)
 */
public class SWIonBlastEffect implements OnFireEffectPlugin, OnHitEffectPlugin, EveryFrameWeaponEffectPlugin {

	//protected CombatEntityAPI chargeGlowEntity;
	//protected SWIonBlastGlow chargeGlowPlugin;
	protected WeaponAPI weapon;
	protected DamagingProjectileAPI proj;
	protected IntervalUtil interval = new IntervalUtil(0.1f, 0.2f);
	protected IntervalUtil arcInterval = new IntervalUtil(0.17f, 0.23f);
	protected float delay = 1f;
	public SWIonBlastEffect() {
		//super();
		arcInterval = new IntervalUtil(0.17f, 0.23f);
		delay = 0.5f;
	}
	
	public void attachToProjectile(DamagingProjectileAPI proj) {
		this.proj = proj;
	}
	
	//protected IntervalUtil interval = new IntervalUtil(0.1f, 0.2f);
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
		this.weapon = weapon;
		//interval.advance(amount);
		if (Global.getCombatEngine().isPaused()) return;
		//if (proj != null) {
		//	entity.getLocation().set(proj.getLocation());
		//} else {
		//	entity.getLocation().set(weapon.getFirePoint(0));
		//}
		//super.advance(amount);
		if (proj != null && !isProjectileExpired(proj) && !proj.isFading()) {
			delay -= amount;
			if (delay <= 0) {
				arcInterval.advance(amount);
				if (arcInterval.intervalElapsed()) {
					spawnArc();
				}
			}
		}
		//boolean charging = weapon.getChargeLevel() > 0 && weapon.getCooldownRemaining() <= 0;
		//if (charging && chargeGlowEntity == null) {
		//	chargeGlowPlugin = new SWIonPulseGlow(weapon);
		//	chargeGlowEntity = Global.getCombatEngine().addLayeredRenderingPlugin(chargeGlowPlugin);	
		//} else if (!charging && chargeGlowEntity != null) {
		//	chargeGlowEntity = null;
		//	chargeGlowPlugin = null;
		//}
	}
	
	
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

	}
	
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		//if (chargeGlowPlugin != null) {
		//	chargeGlowPlugin.attachToProjectile(projectile);
		//	chargeGlowPlugin = null;
		//	chargeGlowEntity = null;
			
		//	MissileAPI missile = (MissileAPI) projectile;
		//	missile.setMine(true);
		//	missile.setNoMineFFConcerns(true);
		//	missile.setMineExplosionRange(SWIonPulseGlow.MAX_ARC_RANGE + 50f);
		//	missile.setMinePrimed(true);
		//	missile.setUntilMineExplosion(0f);
		//}
		
	}
	
	@Override		
	public void spawnArc() {
		CombatEngineAPI engine = Global.getCombatEngine();
		
		//float emp = 0f;//proj.getEmpAmount();
		//float dam = proj.getDamageAmount();
	
		//CombatEntityAPI target = findTarget(proj, weapon, engine);
		float thickness = 20f;
		float coreWidthMult = 0.67f;
		Color color = weapon.getSpec().getGlowColor();
		//color = new Color(255,100,100,255);
		//if (target != null) {
		//	EmpArcEntityAPI arc = engine.spawnEmpArc(proj.getSource(), proj.getLocation(), null,
		//			   target,
		//			   DamageType.KINETIC, 
			//		   dam,
			//		   emp, // emp 
			//		   100000f, // max range 
			//		   "sw_ion_pulse_emp_impact",
			//		   thickness, // thickness
			//		   color,
			//		   new Color(255,255,255,255)
			//		   );
			//arc.setCoreWidthOverride(thickness * coreWidthMult);
			//if (target instanceof ShipAPI) {
			//	ShipAPI targetShip = (ShipAPI) target;
			//	if (!targetShip.getFluxTracker().isOverloaded()) {
			//		if (targetShip.getHullSize() == HullSize.CAPITAL_SHIP && (targetShip.getMaxFlux() > 23000f)) {
			//			targetShip.getFluxTracker().increaseFlux(500f, true) ;
			//		}
			//		else {
			//			targetShip.getFluxTracker().forceOverload(0f);
			//		}
			//	}
			//}
			
			//spawnEMPParticles(EMPArcHitType.SOURCE, proj.getLocation(), null);
			//spawnEMPParticles(EMPArcHitType.DEST, arc.getTargetLocation(), target);
			
		//} else {
			
			//Global.getSoundPlayer().playSound("realitydisruptor_emp_impact", 1f, 1f, to, new Vector2f());
			
			//spawnEMPParticles(EMPArcHitType.SOURCE, from, null);
			//spawnEMPParticles(EMPArcHitType.DEST_NO_TARGET, to, null);
		//}
		Vector2f from = new Vector2f(proj.getLocation());
		Vector2f to = pickNoTargetDest(proj, weapon, engine);
		EmpArcEntityAPI arc = engine.spawnEmpArcVisual(from, null, to, null, thickness, color, Color.white);
		arc.setCoreWidthOverride(thickness * coreWidthMult);
	}
	
	public Vector2f pickNoTargetDest(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		float range = 10f;
		Vector2f from = projectile.getLocation();
		Vector2f dir = Misc.getUnitVectorAtDegreeAngle((float) Math.random() * 360f);
		dir.scale(range);
		Vector2f.add(from, dir, dir);
		dir = Misc.getPointWithinRadius(dir, range * 0.25f);
		return dir;
	}
	
	public static boolean isProjectileExpired(DamagingProjectileAPI proj) {
		return proj.isExpired() || proj.didDamage() || !Global.getCombatEngine().isEntityInPlay(proj);
	}
	
	public static boolean isWeaponCharging(WeaponAPI weapon) {
		return weapon.getChargeLevel() > 0 && weapon.getCooldownRemaining() <= 0;
	}
	
}




