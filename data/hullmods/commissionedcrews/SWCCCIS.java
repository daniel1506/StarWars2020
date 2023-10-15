package data.hullmods.commissionedcrews;

import java.util.HashMap;
import java.util.Map;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import java.awt.Color;

public class SWCCCIS extends BaseHullMod {
	public static float SUPPLIES_MULT = 20f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {	
        stats.getPeakCRDuration().modifyFlat(id, 60f);
		stats.getSuppliesPerMonth().modifyMult(id, 1f - SUPPLIES_MULT / 100f);
    }

    
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return "" + (int) SUPPLIES_MULT + "%";
        if (index == 1) return "" + 60;
        //if (index == 2) return "" + ((Float) coom.get(HullSize.CRUISER)).intValue();
        //if (index == 3) return "" + ((Float) coom.get(HullSize.CAPITAL_SHIP)).intValue();
        //if (index == 4) return "" + (int) ARMOR_BONUS;
        return null;
    }

	//Oh these are cool colors below introduced in 0.95a, to match with your tech type and stuff. Just nice to have!

    @Override
    public Color getBorderColor() {
        return new Color(0,102,255,0);
    }

    @Override
    public Color getNameColor() {
        return new Color(0,102,255,255);
    }
}
