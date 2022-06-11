package data.scripts.combat;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

public class SWBuzzDroidOnHitEffect implements OnHitEffectPlugin {


	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
					  Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		if 	(target instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) target;
			if (!shieldHit) {
			
				float emp = projectile.getEmpAmount();
				float dam = 0;
			
				if (ship.getHullSize() == HullSize.FIGHTER) {
					dam = 1000f;
					//damageResult.setDamageToShields(1000f);
					engine.spawnEmpArc(projectile.getSource(), point, target, target,
									DamageType.ENERGY, 
									dam,
									emp, // emp 
									50f, // max range 
									"tachyon_lance_emp_impact",
									10f, // thickness
									new Color(25,100,155,255),
									new Color(255,255,255,255)
									);
				}
			
				//engine.spawnProjectile(null, null, "plasma", point, 0, new Vector2f(0, 0));
			}
			else if (shieldHit) {
				if (ship.getHullSize() == HullSize.FIGHTER) {
					ship.getFluxTracker().forceOverload(0f);
				}
			}
		}	
	}
}
