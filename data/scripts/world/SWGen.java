package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.CoreCampaignPluginImpl;
import com.fs.starfarer.api.impl.campaign.CoreScript;
import com.fs.starfarer.api.impl.campaign.events.CoreEventProbabilityManager;
import com.fs.starfarer.api.impl.campaign.fleets.DisposableLuddicPathFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.DisposablePirateFleetManager;
import com.fs.starfarer.api.impl.campaign.fleets.EconomyFleetRouteManager;
import com.fs.starfarer.api.impl.campaign.fleets.MercFleetManagerV2;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;

import com.fs.starfarer.api.impl.campaign.shared.SharedData;

import data.hullmods.HeavyArmor;
import data.scripts.world.systems.CoruscantPrime;
import data.scripts.world.systems.ChandrilaPrime;


public class SWGen implements SectorGeneratorPlugin {

	public void generate(SectorAPI sector) {
			
		new CoruscantPrime().generate(sector);
		new ChandrilaPrime().generate(sector);
		
		LocationAPI hyper = Global.getSector().getHyperspace();
		SectorEntityToken empireLabel = hyper.addCustomEntity("sw_empire_label", null, "sw_empire_label", null);
		SectorEntityToken rebelLabel = hyper.addCustomEntity("sw_rebel_label", null, "sw_rebel_label", null);
		
		empireLabel.setFixedLocation(1400, 5000);
		rebelLabel.setFixedLocation(10000, 3500);
		
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_empire");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_rebel");
		
		
		/*SectorEntityToken deep_hyperspace_test = Global.getSector().getHyperspace().addTerrain(Terrain.NEBULA, new BaseTiledTerrain.TileParams(
				"   xx     " +
				"   xxx    " +
				"  xxx x   " +
				"  xx   x  " +
				" xxxx xxx " +
				"  xxxxxxx " +
				" xxxxxxxxx" +
				" xxxxxxxxx" +
				"  xxxxxxx " +
				" xxxxxxx  " +
				" x xxxxx  " +
				"  xxxxxx  " +
				" xxxx xxx " +
				"xxxx  xxx " +
				" xxxx     " +
				"xxxxxxxxx " +
				"  xxxxxxxx" +
				" xxxxxxxxx" +
				"  xxxxxxx " +
				"   xxx    ",
				10, 20, // size of the nebula grid, should match above string
				"terrain", "deep_hyperspace", 4, 4));
		
		deep_hyperspace_test.getLocation().set(5000,5000);*/
		
		this.setRelationships(sector);
		
	}
	
	private void setRelationships(SectorAPI sector) {
		
		
		// forget why this is necessary - workaround for some JANINO issue, I think
		Class c = HeavyArmor.class;
				
		FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
		FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
		FactionAPI pirates = sector.getFaction(Factions.PIRATES);
		FactionAPI independent = sector.getFaction(Factions.INDEPENDENT);
		FactionAPI kol = sector.getFaction(Factions.KOL);
		FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
		FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
		FactionAPI player = sector.getFaction(Factions.PLAYER);
		FactionAPI diktat = sector.getFaction(Factions.DIKTAT);
		FactionAPI persean = sector.getFaction(Factions.PERSEAN);
		FactionAPI remnant = sector.getFaction(Factions.REMNANTS);
		FactionAPI derelict = sector.getFaction(Factions.DERELICT);
		FactionAPI empire = sector.getFaction("sw_empire");
		FactionAPI rebel = sector.getFaction("sw_rebel");
		
		player.setRelationship(hegemony.getId(), 0);
		player.setRelationship(tritachyon.getId(), 0);
		player.setRelationship(persean.getId(), 0);
		player.setRelationship(pirates.getId(), -0.65f);
		
		player.setRelationship(independent.getId(), 0);
		player.setRelationship(kol.getId(), 0);
		player.setRelationship(church.getId(), 0);
		player.setRelationship(path.getId(), -0.65f);
		player.setRelationship(empire.getId(), 0);
		player.setRelationship(rebel.getId(), 0);
				
		empire.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		empire.setRelationship(tritachyon.getId(), RepLevel.COOPERATIVE);
		empire.setRelationship(path.getId(), RepLevel.HOSTILE);
		empire.setRelationship(hegemony.getId(), -0.5f);
		empire.setRelationship(persean.getId(), RepLevel.HOSTILE);
		
		rebel.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(path.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(persean.getId(), RepLevel.COOPERATIVE);
		rebel.setRelationship(hegemony.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(independent.getId(), 0.5f);
		
		this.setModRelationshipsEmpire(empire);
		this.setModRelationshipsRebel(rebel);
	}
	
	private void setModRelationshipsEmpire(final FactionAPI faction) {
        faction.setRelationship("approlight", -0.8f);
        faction.setRelationship("blackrock_driveyards", 0.4f);
        faction.setRelationship("cabal", -0.6f);
        faction.setRelationship("citadeldefenders", 0.1f);
        faction.setRelationship("darkspire", -0.1f);
        faction.setRelationship("dassault_mikoyan", 0.15f);
        faction.setRelationship("diableavionics", 0.3f);
        faction.setRelationship("exigency", -0.2f);
        faction.setRelationship("interstellarimperium", -0.3f);
        faction.setRelationship("junk_pirates", -0.2f);
        faction.setRelationship("mayorate", -0.3f);
        faction.setRelationship("metelson", 0.3f);
        faction.setRelationship("neutrinocorp", -0.1f);
        faction.setRelationship("ORA", -0.6f);
        faction.setRelationship("scavengers", 0.0f);
        faction.setRelationship("SCY", 0.3f);
        faction.setRelationship("shadow_industry", 0.5f);
        faction.setRelationship("syndicate_asp", -0.2f);
        faction.setRelationship("templars", -0.4f);
        faction.setRelationship("tiandong", 0.2f);
    }
	
	private void setModRelationshipsRebel(final FactionAPI faction) {
        faction.setRelationship("approlight", -0.8f);
        faction.setRelationship("blackrock_driveyards", 0.2f);
        faction.setRelationship("cabal", -0.6f);
        faction.setRelationship("citadeldefenders", 0.1f);
        faction.setRelationship("darkspire", -0.1f);
        faction.setRelationship("dassault_mikoyan", 0.15f);
        faction.setRelationship("diableavionics", -0.3f);
        faction.setRelationship("exigency", -0.2f);
        faction.setRelationship("interstellarimperium", -0.5f);
        faction.setRelationship("junk_pirates", -0.2f);
        faction.setRelationship("mayorate", -0.3f);
        faction.setRelationship("metelson", 0.3f);
        faction.setRelationship("neutrinocorp", -0.1f);
        faction.setRelationship("ORA", 0.6f);
        faction.setRelationship("scavengers", 0.0f);
        faction.setRelationship("SCY", -0.3f);
        faction.setRelationship("spire", 0.0f);
        faction.setRelationship("shadow_industry", -0.5f);
        faction.setRelationship("syndicate_asp", -0.2f);
        faction.setRelationship("templars", 0.3f);
        faction.setRelationship("tiandong", 0.2f);
    }
}




