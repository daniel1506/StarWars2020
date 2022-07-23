package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.Misc;
//import data.scripts.hullmods.TEM_LatticeShield;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Level;
import org.dark.shaders.ShaderModPlugin;
import org.dark.shaders.distortion.DistortionAPI;
import org.dark.shaders.distortion.DistortionShader;
import org.dark.shaders.distortion.RippleDistortion;
import org.dark.shaders.distortion.WaveDistortion;
import org.dark.shaders.util.ShaderLib;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

public class SWDeflectorDistortionsPlugin extends BaseEveryFrameCombatPlugin {

    private static final String DATA_KEY = "GLib_DeflectorDistortions";

    //private static final String SETTINGS_FILE = "GRAPHICS_OPTIONS.ini";

    private static final Vector2f ZERO = new Vector2f();

    private static boolean enabled = true;
    private static boolean mjolnirEnabled = true;
    private static boolean shieldEnabled = true;
	
	private static List<String> deflectorShieldList = new ArrayList<String>();

	static {
		deflectorShieldList.add("sw_deflectorshield");
		deflectorShieldList.add("sw_deflectorshield_rebel");
		deflectorShieldList.add("sw_deflectorshield_cis");
		deflectorShieldList.add("sw_deflectorshield_zann");	
		deflectorShieldList.add("sw_deflectorshield_chiss");
	}

    //static {
        //try {
        //    loadSettings();
        //} catch (IOException | JSONException e) {
        //    Global.getLogger(DistortionsPlugin.class).log(Level.ERROR, "Failed to load performance settings: "
        //            + e.getMessage());
        //    enabled = false;
        //}
    //}

    //private static void loadSettings() throws IOException, JSONException {
    //    JSONObject settings = Global.getSettings().loadJSON(SETTINGS_FILE);
    //    shieldEnabled = settings.getBoolean("enableShieldRipples");
    //    mjolnirEnabled = settings.getBoolean("enableMjolnirRipples");
    //    enabled = shieldEnabled || mjolnirEnabled;
    //}

    private CombatEngineAPI engine;

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine == null || !enabled) {
            return;
        }

        if (engine.isPaused() || !ShaderLib.areShadersAllowed() || !ShaderLib.areBuffersAllowed()) {
            return;
        }

        final LocalData localData = (LocalData) engine.getCustomData().get(DATA_KEY);
        final Map<DamagingProjectileAPI, ProjectileInfo> projectiles = localData.projectiles;

        List<DamagingProjectileAPI> activeProjectiles = engine.getProjectiles();
        int size = activeProjectiles.size();
        for (int i = 0; i < size; i++) {
            DamagingProjectileAPI projectile = (DamagingProjectileAPI) activeProjectiles.get(i);
            if (projectile.didDamage() || projectile.getElapsed() > 0.2f) {
                continue;
            }

            if (!projectiles.containsKey(projectile)) {
                if (projectile.getProjectileSpecId() != null && projectile.getProjectileSpecId().contentEquals(
                        "mjolnir_shot") && mjolnirEnabled) {
                    WaveDistortion wave = new WaveDistortion(projectile.getLocation(), ZERO);
                    wave.setIntensity(5f);
                    wave.setSize(50f);
                    wave.flip(true);
                    DistortionShader.addDistortion(wave);
                    projectiles.put(projectile, new ProjectileInfo(wave, projectile.getDamageAmount()));
                } else if (shieldEnabled) {
                    projectiles.put(projectile, new ProjectileInfo(projectile.getDamageAmount()));
                }
            }
        }

        Iterator<Map.Entry<DamagingProjectileAPI, ProjectileInfo>> iter = projectiles.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<DamagingProjectileAPI, ProjectileInfo> entry = (Map.Entry<DamagingProjectileAPI, ProjectileInfo>) iter.next();
            DamagingProjectileAPI projectile = (DamagingProjectileAPI) entry.getKey();
            ProjectileInfo info = (ProjectileInfo) entry.getValue();
            if (Math.random() > 0.8) {
                info.damage = Math.max(info.damage, projectile.getDamageAmount());
            }

            if (projectile.didDamage()) {
                CombatEntityAPI target = projectile.getDamageTarget();

                if (target instanceof ShipAPI && shieldEnabled) {
                    ShipAPI ship = (ShipAPI) target;
                    float distanceFromShieldBorder = 0f;
					float dist = Misc.getDistance(projectile.getLocation(), ship.getShieldCenterEvenIfNoShield());
                    if (ship.getPhaseCloak() != null && deflectorShieldList.contains(ship.getPhaseCloak().getId())) {
                        distanceFromShieldBorder = Math.abs((MathUtils.getDistance(projectile.getLocation(),
                                ship.getShieldCenterEvenIfNoShield()) - dist));
                    }
                    if (ship.getPhaseCloak() != null && deflectorShieldList.contains(ship.getPhaseCloak().getId())
                            && ((ship.getPhaseCloak().isOn() && (dist <= ship.getShieldRadiusEvenIfNoShield()))
                            || (ship.getFluxTracker().isOverloaded()
                            && ship.getFluxTracker().getFluxLevel() >= 0.98f
                            && distanceFromShieldBorder <= 10f))) {
                        Vector2f position = VectorUtils.getDirectionalVector(ship.getShieldCenterEvenIfNoShield(),
                                projectile.getLocation());
                        position.scale(dist);
                        Vector2f.add(position, ship.getShieldCenterEvenIfNoShield(), position);
                        float fader = 1f;
                        if (!(projectile instanceof MissileAPI) && projectile.getWeapon() != null) {
                            float lifetime = projectile.getWeapon().getRange()
                                    / projectile.getWeapon().getProjectileSpeed();
                            float fadetime = 400f / projectile.getWeapon().getProjectileSpeed();
                            fader = Math.max(0.25f, 1f - Math.max(0f, projectile.getElapsed() / lifetime - 1f)
                                    / fadetime);
                            if (fader < 0.99f) {
                                fader *= 0.5f;
                            }
                        }
                        float factor = ship.getMutableStats().getShieldDamageTakenMult().getModifiedValue();
                        createHitRipple(position, ship.getVelocity(), info.damage * fader * factor,
                                projectile.getDamageType(), VectorUtils.getFacing(
                                VectorUtils.getDirectionalVector(ship.getShieldCenterEvenIfNoShield(),
                                        projectile.getLocation())),
                                dist);
                    } //else if (ShaderModPlugin.templarsExists && TEM_LatticeShield.shieldLevel(ship) > 0f) {
                    //    float fader = 1f;
                    //    if (!(projectile instanceof MissileAPI) && projectile.getWeapon() != null) {
                    //        float lifetime = projectile.getWeapon().getRange()
                    //                / projectile.getWeapon().getProjectileSpeed();
                    //        float fadetime = 400f / projectile.getWeapon().getProjectileSpeed();
                    //        fader = Math.max(0.25f, 1f - Math.max(0f, projectile.getElapsed() / lifetime - 1f)
                    //                / fadetime);
                    //        if (fader < 0.99f) {
                    ///            fader *= 0.5f;
                    //        }
                    //    }
                    //    float factor = ship.getMutableStats().getShieldDamageTakenMult().getModifiedValue();
                    //    createHitRipple(projectile.getLocation(), ship.getVelocity(), info.damage * fader * factor,
                    //            projectile.getDamageType(),
                    //            VectorUtils.getFacing(VectorUtils.getDirectionalVector(ship.getLocation(),
                    //                    projectile.getLocation())),
                    //            ship.getCollisionRadius());
                    //}
                }

                // if (info.distortion != null) {
                    // if (projectile.getProjectileSpecId().contentEquals("mjolnir_shot")) {
                        // WaveDistortion wave = (WaveDistortion) info.distortion;
                        // if (!wave.isFading()) {
                            // wave.fadeOutIntensity(0.2f);
                        // }
                    // }
                // }

                iter.remove();
            }
			// else if (!engine.isEntityInPlay(projectile)) {
                // if (info.distortion != null) {
                    // if (projectile.getProjectileSpecId().contentEquals("mjolnir_shot")) {
                        // WaveDistortion wave = (WaveDistortion) info.distortion;
                        // wave.setLifetime(0f);
                    // }
                // }

                // iter.remove();
            // } else if (info.distortion != null) {
                // if (projectile.getProjectileSpecId().contentEquals("mjolnir_shot")) {
                    // WaveDistortion wave = (WaveDistortion) info.distortion;
                    // wave.setLocation(projectile.getLocation());
                // }
            // } else if (projectile.isFading()) {
                // if (info.distortion != null) {
                    // if (projectile.getProjectileSpecId().contentEquals("mjolnir_shot")) {
                        // WaveDistortion wave = (WaveDistortion) info.distortion;
                        // if (!wave.isFading()) {
                            // wave.fadeOutIntensity(0.2f);
                        // }
                    // }
                // }
            // }
        }
    }

    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        Global.getCombatEngine().getCustomData().put(DATA_KEY, new LocalData());
    }

    private void createHitRipple(Vector2f location, Vector2f velocity, float damage, DamageType type, float direction,
            float shieldRadius) {
        float dmg = damage;
        if (type == DamageType.FRAGMENTATION) {
            dmg *= 0.25f;
        }
        if (type == DamageType.HIGH_EXPLOSIVE) {
            dmg *= 0.5f;
        }
        if (type == DamageType.KINETIC) {
            dmg *= 2f;
        }

        if (dmg < 25f) {
            return;
        }

        float fadeTime = (float) Math.pow(dmg, 0.25) * 0.1f;
        float size = (float) Math.pow(dmg, 0.3333333) * 7.5f;

        float ratio = Math.min(size / shieldRadius, 1f);
        float arc = 90f - ratio * 14.54136f; // Don't question the magic number

        float start1 = direction - arc;
        if (start1 < 0f) {
            start1 += 360f;
        }
        float end1 = direction + arc;
        if (end1 >= 360f) {
            end1 -= 360f;
        }

        float start2 = direction + arc;
        if (start2 < 0f) {
            start2 += 360f;
        }
        float end2 = direction - arc;
        if (end2 >= 360f) {
            end2 -= 360f;
        }

        RippleDistortion ripple = new RippleDistortion(location, velocity);
        ripple.setSize(size);
        ripple.setIntensity(size * 0.3f);
        ripple.setFrameRate(60f / fadeTime);
        ripple.fadeInSize(fadeTime * 1.2f);
        ripple.fadeOutIntensity(fadeTime);
        ripple.setSize(size * 0.2f);
        ripple.setArc(start1, end1);
        DistortionShader.addDistortion(ripple);

        ripple = new RippleDistortion(location, velocity);
        ripple.setSize(size);
        ripple.setIntensity(size * 0.075f);
        ripple.setFrameRate(60f / fadeTime);
        ripple.fadeInSize(fadeTime * 1.2f);
        ripple.fadeOutIntensity(fadeTime);
        ripple.setSize(size * 0.2f);
        ripple.setArc(start2, end2);
        DistortionShader.addDistortion(ripple);
    }

    private static final class LocalData {

        final Map<DamagingProjectileAPI, ProjectileInfo> projectiles = new LinkedHashMap<DamagingProjectileAPI, ProjectileInfo>(1000);
    }

    private static class ProjectileInfo {

        float damage;
        DistortionAPI distortion;

        ProjectileInfo(DistortionAPI distortion, float damage) {
            this.distortion = distortion;
            this.damage = damage;
        }

        ProjectileInfo(float damage) {
            this.distortion = null;
            this.damage = damage;
        }
    }
}
