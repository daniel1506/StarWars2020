package data.scripts.combat;

import org.lwjgl.util.vector.Vector2f;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.util.MagicFakeBeam;

/**
 * IMPORTANT: will be multiple instances of this, as this doubles as the every frame effect and the on fire effect (same instance)
 * But also as the visual for each individual shot (created via onFire, using the non-default constructor)
 */
public class SWConquerorFireEffect implements OnFireEffectPlugin, OnHitEffectPlugin, EveryFrameWeaponEffectPlugin {

	protected CombatEntityAPI chargeGlowEntity;
	//protected SWIonPulseGlow chargeGlowPlugin;
	public static final Color BEAM_COLOR = new Color(0,255,0,10);
	private List<WeaponSlotAPI> dWeapons = new ArrayList<WeaponSlotAPI>();
	//public SWConquerorFireEffect() {
	//}
	
	//protected IntervalUtil interval = new IntervalUtil(0.1f, 0.2f);
	public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
		//interval.advance(amount);
		ShipAPI ship = weapon.getShip();
		if (dWeapons.isEmpty()) {
			for (WeaponSlotAPI w : ship.getHullSpec().getAllWeaponSlotsCopy()) {
				if(w.isDecorative()) {
					dWeapons.add(w);
				}
			}
		}
		
		boolean charging = weapon.getChargeLevel() > 0 && weapon.getCooldownRemaining() <= 0;
		if (charging || weapon.isFiring()) {
			for (WeaponSlotAPI d : dWeapons) {
				float dist = Misc.getDistance(d.computePosition((CombatEntityAPI) ship), weapon.getFirePoint(0));
				MagicFakeBeam.spawnFakeBeam (Global.getCombatEngine(), d.computePosition((CombatEntityAPI) ship), dist, Misc.getAngleInDegrees(d.computePosition((CombatEntityAPI) ship), weapon.getFirePoint(0)), 
				10f, 0.1f, 0.1f, 0f, BEAM_COLOR, BEAM_COLOR, 0f, DamageType.ENERGY, 0f, ship);
			}		
		}
	}
	
	
	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

	}
	
	public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
		
	}
	
	
}




