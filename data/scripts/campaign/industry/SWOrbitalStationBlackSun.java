package data.scripts.campaign.industry;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.impl.OrbitalStation;

public class SWOrbitalStationBlackSun extends OrbitalStation
{
    public boolean isAvailableToBuild() {
        boolean canBuild = super.isAvailableToBuild();
        final SectorAPI sector = Global.getSector();
        final FactionAPI player = sector.getFaction("player");
        final FactionAPI blacksun = sector.getFaction("sw_black_sun");
        if (!this.market.hasIndustry("sw_golan_iii_blacksun") && this.market.getPlanetEntity() != null && !(player.getRelationshipLevel(blacksun).isAtWorst(RepLevel.FRIENDLY) || Global.getSector().getPlayerFaction().knowsIndustry(this.getId()))) {
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