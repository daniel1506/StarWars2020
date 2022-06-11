package data.scripts.combat;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.BaseCombatLayeredRenderingPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.EmpArcEntityAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;


/**
 * IMPORTANT: will be multiple instances of this, as this doubles as the every frame effect and the on fire effect (same instance)
 * But also as the visual for each individual shot (created via onFire, using the non-default constructor)
 */
public class SWIonPulseGlow extends BaseCombatLayeredRenderingPlugin implements OnHitEffectPlugin {

	
	public static int MAX_ARC_RANGE = 175;
	//public static int ARCS_ON_HIT = 15;
	
	//public static Color UNDERCOLOR = RiftCascadeEffect.EXPLOSION_UNDERCOLOR;
	//public static Color RIFT_COLOR = RiftCascadeEffect.STANDARD_RIFT_COLOR;
	
	
	protected WeaponAPI weapon;
	protected DamagingProjectileAPI proj;
	protected IntervalUtil interval = new IntervalUtil(0.1f, 0.2f);
	protected IntervalUtil arcInterval = new IntervalUtil(0.17f, 0.23f);
	protected float delay = 1f;
	
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

	}
	
	public SWIonPulseGlow(WeaponAPI weapon) {
		super();
		this.weapon = weapon;
		arcInterval = new IntervalUtil(0.17f, 0.23f);
		delay = 0.5f;
	}
	
	
	public void attachToProjectile(DamagingProjectileAPI proj) {
		this.proj = proj;
	}
	
	public void advance(float amount) {
		if (Global.getCombatEngine().isPaused()) return;
		if (proj != null) {
			entity.getLocation().set(proj.getLocation());
		} else {
			entity.getLocation().set(weapon.getFirePoint(0));
		}
		super.advance(amount);
		
		//boolean keepSpawningParticles = isWeaponCharging(weapon) || 
		//			(proj != null && !isProjectileExpired(proj) && !proj.isFading());
		//if (keepSpawningParticles) {
		//	interval.advance(amount);
		//	if (interval.intervalElapsed()) {
		//		addChargingParticles(weapon);
		//	}
		//}
		
		if (proj != null && !isProjectileExpired(proj) && !proj.isFading()) {
			delay -= amount;
			if (delay <= 0) {
				arcInterval.advance(amount);
				if (arcInterval.intervalElapsed()) {
					spawnArc();
				}
			}
		}
		//if (proj != null) {
		//	Global.getSoundPlayer().playLoop("realitydisruptor_loop", proj, 1f, 1f * proj.getBrightness(),
		//									 proj.getLocation(), proj.getVelocity());
		//}
		
	}
	
	@Override
	public boolean isExpired() {
		boolean keepSpawningParticles = isWeaponCharging(weapon) || 
					(proj != null && !isProjectileExpired(proj) && !proj.isFading());
		return super.isExpired() && (!keepSpawningParticles || (!weapon.getShip().isAlive() && proj == null));
	}

	
	public float getRenderRadius() {
		return 500f;
	}
	
	
	@Override		
	public void spawnArc() {
		CombatEngineAPI engine = Global.getCombatEngine();
		
		float emp = proj.getEmpAmount();
		float dam = proj.getDamageAmount();
	
		CombatEntityAPI target = findTarget(proj, weapon, engine);
		float thickness = 20f;
		float coreWidthMult = 0.67f;
		Color color = weapon.getSpec().getGlowColor();
		//color = new Color(255,100,100,255);
		if (target != null) {
			EmpArcEntityAPI arc = engine.spawnEmpArc(proj.getSource(), proj.getLocation(), null,
					   target,
					   DamageType.KINETIC, 
					   dam,
					   emp, // emp 
					   100000f, // max range 
					   "sw_ion_pulse_emp_impact",
					   thickness, // thickness
					   color,
					   new Color(255,255,255,255)
					   );
			arc.setCoreWidthOverride(thickness * coreWidthMult);
			if (target instanceof ShipAPI) {
				ShipAPI targetShip = (ShipAPI) target;
				if (!targetShip.getFluxTracker().isOverloaded()) {
					if (targetShip.getHullSize() == HullSize.CAPITAL_SHIP && (targetShip.getMaxFlux() > 23000f)) {
						targetShip.getFluxTracker().increaseFlux(500f, true) ;
					}
					else {
						targetShip.getFluxTracker().forceOverload(0f);
					}
				}
			}
			
			//spawnEMPParticles(EMPArcHitType.SOURCE, proj.getLocation(), null);
			//spawnEMPParticles(EMPArcHitType.DEST, arc.getTargetLocation(), target);
			
		} else {
			Vector2f from = new Vector2f(proj.getLocation());
			Vector2f to = pickNoTargetDest(proj, weapon, engine);
			EmpArcEntityAPI arc = engine.spawnEmpArcVisual(from, null, to, null, thickness, color, Color.white);
			arc.setCoreWidthOverride(thickness * coreWidthMult);
			//Global.getSoundPlayer().playSound("realitydisruptor_emp_impact", 1f, 1f, to, new Vector2f());
			
			//spawnEMPParticles(EMPArcHitType.SOURCE, from, null);
			//spawnEMPParticles(EMPArcHitType.DEST_NO_TARGET, to, null);
		}
	}
	
	

	public Vector2f pickNoTargetDest(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		float range = 50f;
		Vector2f from = projectile.getLocation();
		Vector2f dir = Misc.getUnitVectorAtDegreeAngle((float) Math.random() * 360f);
		dir.scale(range);
		Vector2f.add(from, dir, dir);
		dir = Misc.getPointWithinRadius(dir, range * 0.25f);
		return dir;
	}
	
	public CombatEntityAPI findTarget(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		float range = MAX_ARC_RANGE;
		Vector2f from = projectile.getLocation();
		
		Iterator<Object> iter = Global.getCombatEngine().getAllObjectGrid().getCheckIterator(from,
																			range * 2f, range * 2f);
		int owner = weapon.getShip().getOwner();
		CombatEntityAPI best = null;
		float minScore = Float.MAX_VALUE;
		while (iter.hasNext()) {
			Object o = iter.next();
			if (!(o instanceof MissileAPI) &&
					//!(o instanceof CombatAsteroidAPI) &&
					!(o instanceof ShipAPI)) continue;
			CombatEntityAPI other = (CombatEntityAPI) o;
			if (other.getOwner() == owner) continue;
			
			if (other instanceof ShipAPI) {
				ShipAPI otherShip = (ShipAPI) other;
				if (otherShip.isHulk()) continue;
				if (otherShip.isPhased()) continue;
			}
			if (other.getCollisionClass() == CollisionClass.NONE) continue;

			float radius = Misc.getTargetingRadius(from, other, false);
			float dist = Misc.getDistance(from, other.getLocation()) - radius - 50f;
			if (dist > range) continue;
			
			float score = dist;
			
			if (score < minScore) {
				minScore = score;
				best = other;
			}
		}
		return best;
	}		
	

	public static boolean isProjectileExpired(DamagingProjectileAPI proj) {
		return proj.isExpired() || proj.didDamage() || !Global.getCombatEngine().isEntityInPlay(proj);
	}
	
	public static boolean isWeaponCharging(WeaponAPI weapon) {
		return weapon.getChargeLevel() > 0 && weapon.getCooldownRemaining() <= 0;
	}
}






