package data.hullmods.commissionedcrews;

import java.util.HashMap;
import java.util.Map;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

public class SWCCRebel extends BaseHullMod {
    private static final Map map = new HashMap();
    static {
        map.put(HullSize.FIGHTER, 50f);
        map.put(HullSize.FRIGATE, 250f);
        map.put(HullSize.DESTROYER, 500f);
        map.put(HullSize.CRUISER, 750f);
        map.put(HullSize.CAPITAL_SHIP, 1000f);
        map.put(HullSize.DEFAULT, 50f);
    }
	public static float SHIELD_UPKEEP_MULT = 25f;
	public static float OVERLOAD_TIME_MULT = 5f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getShieldUpkeepMult().modifyMult(id, 1f - SHIELD_UPKEEP_MULT / 100f);
		stats.getOverloadTimeMod().modifyMult(id, 1f - OVERLOAD_TIME_MULT / 100f);
    }
    
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) SHIELD_UPKEEP_MULT;
        if (index == 1) return "" + (int) OVERLOAD_TIME_MULT;
        //if (index == 2) return "" + ((Float) map.get(HullSize.CRUISER)).intValue();
        //if (index == 3) return "" + ((Float) map.get(HullSize.CAPITAL_SHIP)).intValue();
        //if (index == 4) return "" + (int) ARMOR_BONUS;
        return null;
    }

	//Oh these are cool colors below introduced in 0.95a, to match with your tech type and stuff. Just nice to have!

    @Override
    public Color getBorderColor() {
        return new Color(255,0,50,0);
    }

    @Override
    public Color getNameColor() {
        return new Color(255,0,50,255);
    }
}
