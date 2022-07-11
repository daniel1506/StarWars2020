package data.scripts.campaign.industry;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.impl.OrbitalStation;

public class SWOrbitalStationZann extends OrbitalStation
{
    public boolean isAvailableToBuild() {
        boolean canBuild = super.isAvailableToBuild();
        final SectorAPI sector = Global.getSector();
        final FactionAPI player = sector.getFaction("player");
        final FactionAPI zann = sector.getFaction("sw_zann");
        if (!this.market.hasIndustry("sw_golan_ii_zann") && this.market.getPlanetEntity() != null && !player.getRelationshipLevel(zann).isAtWorst(RepLevel.WELCOMING) && !Global.getSector().getPlayerFaction().knowsIndustry(this.getId())) {
            canBuild = false;
        }
        return canBuild;
    }
    
    public String getUnavailableReason() {
        return "Reputation too low";
    }
    
    public boolean showWhenUnavailable() {
        return false;
    }
}