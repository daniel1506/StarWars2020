package data.scripts.world.systems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;
import com.fs.starfarer.api.util.Misc;

import data.scripts.world.SW_AddMarket;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ChandrilaPrimeOld {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Chandrila Prime");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background3.jpg");
		system.getLocation().set(2733.2f, 11344.8f);
		
		SectorEntityToken chandrila_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/eos_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI chandrila_star = system.initStar("chandrila_prime", // unique id for this star 
										    "star_yellow",  // id in planets.json
										    600f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(255, 220, 190)); // light color in entire system, affects all entities				
		
		PlanetAPI chandrila1 = system.addPlanet("chandrila", chandrila_star, "Chandrila", "terran", 10, 135, 3500, 368);
		chandrila1.getSpec().setPlanetColor(new Color(205,205,205,205));
		chandrila1.setCustomDescriptionId("planet_chandrila");
		chandrila1.applySpecChanges();
		
		SectorEntityToken chandrilaStation = system.addCustomEntity("chandrila_starforge", "chandrila Orbital Station", "station_side07", "sw_rebel");
		chandrilaStation.setCircularOrbitPointingDown(system.getEntityById("chandrila"), 0, 250, 30);		
		chandrilaStation.setInteractionImage("illustrations", "orbital");
		chandrilaStation.setCustomDescriptionId("station_chandrila");
		
		SW_AddMarket.SW_AddMarket("sw_rebel", (SectorEntityToken)chandrila1, new ArrayList<String>(Arrays.asList((SectorEntityToken)chandrilaStation)), "Chandrila", 9, new ArrayList<String>(Arrays.asList("farmland_rich", "habitable", "population_9", "mild_climate", "trade_center", "regional_capital")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "spaceport", "commerce", "farming", "highcommand", "population", "heavybatteries", "sw_golan_iii_rebel", "planetaryshield")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.1f);
		
	// Mon Cala System 
		PlanetAPI chandrila2 = system.addPlanet("mon_cala", chandrila_star, "Mon Cala", "water", 20, 110, 6000, 398);
		chandrila2.getSpec().setIconColor(new Color(50,50,255,255));
		chandrila1.setCustomDescriptionId("planet_mon_cala");
		chandrila2.applySpecChanges();
					
			//Relay, L5 (behind)
			SectorEntityToken relay = system.addCustomEntity("mon_cala_relay", // unique id
					 "Mon Cala Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "sw_rebel"); // faction
			relay.setCircularOrbitPointingDown( system.getEntityById("chandrila_prime"), 320, 4750, 220);
		
		SectorEntityToken moncalaStation = system.addCustomEntity("mon_cala_starforge", "Mon Calamari Shipyards", "station_hightech2", "sw_rebel");
		moncalaStation.setCircularOrbitPointingDown(system.getEntityById("mon_cala"), 0, 700, 30);		
		moncalaStation.setCustomDescriptionId("station_mon_cala");
		
		SW_AddMarket.SW_AddMarket("sw_rebel", (SectorEntityToken)chandrila2, null, "Mon Cala", 8, new ArrayList<String>(Arrays.asList("water", "habitable", "population_8", "mild_climate", "water_surface")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "aquaculture", "megaport", "population", "heavybatteries")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market")), 0.1f);
		
		MarketAPI mon_cala_market = SW_AddMarket.SW_AddMarket("sw_rebel", (SectorEntityToken)moncalaStation, null, "Mon Calamari Shipyard", 7, new ArrayList<String>(Arrays.asList("no_atmosphere", "population_7", "outpost", "ai_core_admin")), new ArrayList<String>(Arrays.asList("lightindustry", "fuelprod", "spaceport", "waystation", "militarybase", "population", "heavybatteries", "orbitalworks", "sw_shipyards_rebel")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);				

		SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item2");
		mon_cala_market.getIndustry("militarybase").setAICoreId("beta_core");
		mon_cala_market.getIndustry("fuelprod").setAICoreId("beta_core");
		mon_cala_market.getIndustry("sw_shipyards_rebel").setAICoreId("alpha_core");
		mon_cala_market.getIndustry("orbitalworks").setAICoreId("beta_core");
		mon_cala_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
		
		PlanetAPI chandrila3 = system.addPlanet("hoth", chandrila_star, "Hoth", "frozen3", 50, 72f, 9000, 549f);
		chandrila3.getSpec().setPlanetColor(new Color(100,175,250,255));
		chandrila3.applySpecChanges();
		chandrila3.setCustomDescriptionId("planet_hoth");
//		coruscant3.setInteractionImage("illustrations", "mine");
		
		SW_AddMarket.SW_AddMarket("sw_rebel", (SectorEntityToken)chandrila3, null, "Hoth", 5, new ArrayList<String>(Arrays.asList("ice", "cold", "population_5", "ore_rich", "rare_ore_rich", "organics_plentiful", "volatiles_plentiful", "headquarters")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "spaceport", "fuelprod", "refining", "mining", "militarybase", "population", "heavybatteries", "sw_golan_ii_rebel", "techmining")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		SectorEntityToken hoth_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_rebel"); 
		hoth_loc.setCircularOrbitPointingDown( chandrila_star, 180-60, 7300, 340);		
		
		// jump point
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("chandrila_inner_jump", "Chandrila Prime Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(chandrila2, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(chandrila3);
		jumpPoint.setStandardWormholeToHyperspaceVisual();
		system.addEntity(jumpPoint);
		
		// generates hyperspace destinations for in-system jump points
		system.autogenerateHyperspaceJumpPoints(true, true);
		this.cleanup(system);
	}
	
	void cleanup(final StarSystemAPI system) {
        final HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin)Misc.getHyperspaceTerrain().getPlugin();
        final NebulaEditor editor = new NebulaEditor((BaseTiledTerrain)plugin);
        final float minRadius = plugin.getTileSize() * 2.0f;
        final float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0f, radius + minRadius * 0.5f, 0.0f, 360.0f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0f, radius + minRadius, 0.0f, 360.0f, 0.25f);
    }
}
