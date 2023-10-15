package data.hullmods.commissionedcrews;

import java.util.HashMap;
import java.util.Map;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

public class SWCCRepublic extends BaseHullMod {
	public static float SENSOR_MULT = 20f;
	public static float FIGHTER_RANGE_MULT = 20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSensorStrength().modifyMult(id, 1f + SENSOR_MULT / 100f);
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			if (ship.hasLaunchBays()) {
				stats.getFighterWingRange().modifyMult(id, 1f + FIGHTER_RANGE_MULT / 100f);
			}
		}
    }

    
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) SENSOR_MULT + "%";
        if (index == 1) return "" + (int) FIGHTER_RANGE_MULT + "%";
        //if (index == 2) return "" + ((Float) coom.get(HullSize.CRUISER)).intValue();
        //if (index == 3) return "" + ((Float) coom.get(HullSize.CAPITAL_SHIP)).intValue();
        //if (index == 4) return "" + (int) ARMOR_BONUS;
        return null;
    }

	//Oh these are cool colors below introduced in 0.95a, to match with your tech type and stuff. Just nice to have!

    @Override
    public Color getBorderColor() {
        return new Color(50,200,255,0);
    }

    @Override
    public Color getNameColor() {
        return new Color(50,200,255,255);
    }
}
