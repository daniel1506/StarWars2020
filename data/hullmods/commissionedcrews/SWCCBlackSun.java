package data.hullmods.commissionedcrews;

import java.util.HashMap;
import java.util.Map;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

public class SWCCBlackSun extends BaseHullMod {
    private static final Map map = new HashMap();
    static {
        map.put(HullSize.FIGHTER, 0f);
        map.put(HullSize.FRIGATE, 5f);
        map.put(HullSize.DESTROYER, 10f);
        map.put(HullSize.CRUISER, 15f);
        map.put(HullSize.CAPITAL_SHIP, 20f);
        map.put(HullSize.DEFAULT, 0f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().modifyMult(id, 1f + ((Float) map.get(hullSize)) / 100f);
    }

    
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + ((Float) map.get(HullSize.FRIGATE)).intValue();
        if (index == 1) return "" + ((Float) map.get(HullSize.DESTROYER)).intValue();
        if (index == 2) return "" + ((Float) map.get(HullSize.CRUISER)).intValue();
        if (index == 3) return "" + ((Float) map.get(HullSize.CAPITAL_SHIP)).intValue();
        //if (index == 4) return "" + (int) ARMOR_BONUS;
        return null;
    }

	//Oh these are cool colors below introduced in 0.95a, to match with your tech type and stuff. Just nice to have!

    @Override
    public Color getBorderColor() {
        return new Color(100,100,100,0);
    }

    @Override
    public Color getNameColor() {
        return new Color(100,100,100,255);
    }
}
