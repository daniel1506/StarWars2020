package data.scripts.campaign.industry;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.econ.impl.OrbitalStation;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

public class SWOrbitalStationShipyard extends OrbitalStation
{
	public static float ORBITAL_WORKS_QUALITY_BONUS = 0.2f;
	
	public static float DEFENSE_BONUS_BASE = 0.5f;
	public static float DEFENSE_BONUS_BATTLESTATION = 1f; 
	public static float DEFENSE_BONUS_FORTRESS = 2f;
	
	public static float IMPROVE_STABILITY_BONUS = 1f; 
	
	private static Map mag = new HashMap();
	private static Map mag_ships = new HashMap();
	static {
		mag.put("sw_light_shipyard", 0.2f);
		mag.put("sw_heavy_shipyard", 0.3f);
		mag.put("sw_capital_shipyard", 0.4f);
		mag_ships.put("sw_light_shipyard", 2);
		mag_ships.put("sw_heavy_shipyard", 1);
		mag_ships.put("sw_capital_shipyard", 0);
	}
	
	public void apply() {
		super.apply(true);
		
		int size = market.getSize();
		int stationSize = 3;
		
		boolean battlestation = getSpec().hasTag(Industries.TAG_BATTLESTATION);
		boolean starfortress = getSpec().hasTag(Industries.TAG_STARFORTRESS);
		if (battlestation) {
			stationSize = 5;
		} else if (starfortress) {
			stationSize = 7;
		}
	
		
		//boolean works = Industries.ORBITALWORKS.equals(getId());
		int shipBonus = 0;
		float qualityBonus = (Float) mag.get(getId());		
		//if (works) {
			//shipBonus = 2;
		//	qualityBonus = ORBITAL_WORKS_QUALITY_BONUS;
		//}
		
		modifyStabilityWithBaseMod();
		applyIncomeAndUpkeep(stationSize);
		
		demand(Commodities.CREW, stationSize);
		//demand(Commodities.SUPPLIES, stationSize);
		demand(Commodities.METALS, size);
		demand(Commodities.RARE_METALS, size - 2);
		
		//supply(Commodities.HEAVY_MACHINERY, size - 2);
		//supply(Commodities.SUPPLIES, size - 2);
		supply(Commodities.HAND_WEAPONS, size - 2);
		supply(Commodities.SHIPS, size - (Integer) mag_ships.get(getId()));
		//supply(Commodities.SHIPS, size - 2);
		if (shipBonus > 0) {
			supply(1, Commodities.SHIPS, shipBonus, "Orbital works");
		}
		
		float bonus = DEFENSE_BONUS_BASE;
		if (battlestation) bonus = DEFENSE_BONUS_BATTLESTATION;
		else if (starfortress) bonus = DEFENSE_BONUS_FORTRESS;
		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD)
						.modifyMult(getModId(), 1f + bonus, getNameForModifier());
		
		Pair<String, Integer> deficit = getMaxDeficit(Commodities.METALS, Commodities.RARE_METALS);
		int maxDeficit = size - 3; // to allow *some* production so economy doesn't get into an unrecoverable state
		if ((Integer) deficit.two > maxDeficit) deficit.two =  maxDeficit;
		
		applyDeficitToProduction(2, deficit,
					//Commodities.HEAVY_MACHINERY,
					Commodities.SUPPLIES,
					Commodities.HAND_WEAPONS,
					Commodities.SHIPS);
		
		if (qualityBonus > 0) {
			market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(getModId(1), qualityBonus, "Orbital works");
		}
				
		matchCommanderToAICore(aiCoreId);
		
		if (!isFunctional()) {
			supply.clear();
			unapply();
		} else {
			applyCRToStation();
		}

	}
	
	@Override
	public void unapply() {
		super.unapply();
		
		unmodifyStabilityWithBaseMod();
		
		matchCommanderToAICore(null);

		market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(getModId());
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(getModId(0));
		market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(getModId(1));
	}
	
	public boolean isDemandLegal(CommodityOnMarketAPI com) {
		return true;
	}

	public boolean isSupplyLegal(CommodityOnMarketAPI com) {
		return true;
	}

	@Override
	protected boolean canImproveToIncreaseProduction() {
		return true;
	}
	
	@Override
	public boolean wantsToUseSpecialItem(SpecialItemData data) {
		if (special != null && Items.CORRUPTED_NANOFORGE.equals(special.getId()) &&
				data != null && Items.PRISTINE_NANOFORGE.equals(data.getId())) {
			return true;
		}
		return super.wantsToUseSpecialItem(data);
	}
	
    public boolean isAvailableToBuild() {
        boolean canBuild = super.isAvailableToBuild();
        final SectorAPI sector = Global.getSector();
        //final FactionAPI player = sector.getFaction("player");
        //final FactionAPI zann = sector.getFaction("sw_zann");
        if (!this.market.hasIndustry("sw_light_shipyard") && this.market.getPlanetEntity() != null && !Global.getSector().getPlayerFaction().knowsIndustry(this.getId())) {
            canBuild = false;
        }
        return canBuild;
    }
    
    public String getUnavailableReason() {
        return "Unknown Error";
    }
    
    public boolean showWhenUnavailable() {
        return false;
    }
}