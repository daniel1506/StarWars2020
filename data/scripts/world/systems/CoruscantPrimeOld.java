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

public class CoruscantPrimeOld {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Coruscant Prime");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(-6833f, 9454f);
		
		SectorEntityToken coruscant_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI coruscant_star = system.initStar("coruscant_prime", // unique id for this star 
										    "star_white",  // id in planets.json
										    700f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(245, 250, 255)); // light color in entire system, affects all entities				
		
		PlanetAPI coruscant1 = system.addPlanet("coruscant", coruscant_star, "Coruscant", "ecumenopolis", 10, 122, 3500, 110);
		coruscant1.getSpec().setPlanetColor(new Color(205,205,205,205));
		coruscant1.setCustomDescriptionId("planet_coruscant");
		coruscant1.applySpecChanges();
		
		SectorEntityToken coruscantStation = system.addCustomEntity("coruscant_starforge", "Coruscant One", "sw_station_golan_iii", "sw_empire");
		coruscantStation.setCircularOrbitPointingDown(system.getEntityById("coruscant"), 0, 300, 30);		
		coruscantStation.setInteractionImage("illustrations", "orbital");
		coruscantStation.setCustomDescriptionId("station_coruscant");
		
		SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)coruscant1, new ArrayList<String>(Arrays.asList((SectorEntityToken)coruscantStation)), "Coruscant", 9, new ArrayList<String>(Arrays.asList("sw_ecumenopolis", "pollution", "habitable", "population_9", "mild_climate", "trade_center", "regional_capital")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "megaport", "commerce", "highcommand", "population", "heavybatteries", "sw_golan_iii_imperial", "planetaryshield")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.1f);
		
	// Kuat System 
		PlanetAPI coruscant2 = system.addPlanet("kuat", coruscant_star, "Kuat", "terran", 20, 150f, 6000, 322f);
		//coruscant2.getSpec().setPlanetColor(new Color(205,205,205,205));
		coruscant2.setCustomDescriptionId("planet_kuat");
		//coruscant2.applySpecChanges();
		
		SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)coruscant2, null, "Kuat", 8, new ArrayList<String>(Arrays.asList("habitable", "population_8", "ore_rich", "rare_ore_rich", "volatiles_plentiful")), new ArrayList<String>(Arrays.asList("spaceport", "refining", "mining", "militarybase", "population", "heavybatteries", "lightindustry")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);		
					
			// kuat Relay, L5 (behind)
			SectorEntityToken relay = system.addCustomEntity("kuat_relay", // unique id
					 "Kuat Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "sw_empire"); // faction
			relay.setCircularOrbitPointingDown( system.getEntityById("coruscant_prime"), 320, 4750, 220);
		
		SectorEntityToken kuatStation = system.addCustomEntity("kuat_starforge", "Kuat Shipyards", "station_hightech2", "sw_empire");
		kuatStation.setCircularOrbitPointingDown(system.getEntityById("kuat"), 0, 700, 30);	
		kuatStation.setInteractionImage("illustrations", "orbital");
		kuatStation.setCustomDescriptionId("station_kuat");
				
		MarketAPI kuat_market = SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)kuatStation, null, "Kuat Shipyard", 8, new ArrayList<String>(Arrays.asList("no_atmosphere", "population_8", "outpost", "ai_core_admin")), new ArrayList<String>(Arrays.asList("lightindustry", "fuelprod", "spaceport", "waystation", "militarybase", "population", "heavybatteries", "orbitalworks", "sw_shipyards_imperial")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);				

		SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item1");
		kuat_market.getIndustry("militarybase").setAICoreId("beta_core");
		kuat_market.getIndustry("fuelprod").setAICoreId("beta_core");
		kuat_market.getIndustry("sw_shipyards_imperial").setAICoreId("alpha_core");
		kuat_market.getIndustry("orbitalworks").setAICoreId("beta_core");
		kuat_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
		
		PlanetAPI coruscant3 = system.addPlanet("byss", coruscant_star, "Byss", "terran", 50, 216f, 9000, 207f);
		coruscant3.getSpec().setPlanetColor(new Color(0,205,0,255));
		coruscant3.applySpecChanges();
		coruscant3.setCustomDescriptionId("planet_byss");
//		coruscant3.setInteractionImage("illustrations", "mine");
		
		SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)coruscant3, null, "Byss", 7, new ArrayList<String>(Arrays.asList("farmland_rich", "habitable", "population_7", "ore_rich", "rare_ore_rich", "organics_plentiful", "headquarters")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "spaceport", "fuelprod", "refining", "mining", "militarybase", "population", "heavybatteries", "sw_shieldgate_imperial", "farming")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		SectorEntityToken byss_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_empire"); 
		byss_loc.setCircularOrbitPointingDown( coruscant_star, 180-60, 7300, 340);		
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("coruscant_inner_jump", "Coruscant Prime Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(coruscant1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(coruscant1);
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
