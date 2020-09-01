package data.scripts.world;

import java.util.Iterator;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import java.util.ArrayList;
import com.fs.starfarer.api.campaign.SectorEntityToken;

public class SW_AddMarket
{
    public static MarketAPI SW_AddMarket(final String factionID, final SectorEntityToken primaryEntity, final ArrayList<SectorEntityToken> connectedEntities, final String name, final int size, final ArrayList<String> marketConditions, final ArrayList<String> Industries, final ArrayList<String> submarkets, final float tariff) {
        final EconomyAPI globalEconomy = Global.getSector().getEconomy();
        final String marketID;
        final String planetID = marketID = primaryEntity.getId();
        final MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, size);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        newMarket.getTariff().modifyFlat("generator", tariff);
        if (null != submarkets) {
            for (final String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }
        if (null != marketConditions) {
            for (final String condition : marketConditions) {
                newMarket.addCondition(condition);
            }
        }
        if (null != Industries) {
            for (final String industry : Industries) {
                newMarket.addIndustry(industry);
            }
        }
        if (null != connectedEntities) {
            for (final SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }
        globalEconomy.addMarket(newMarket, true);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);
        if (null != connectedEntities) {
            for (final SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }
        return newMarket;
    }
}