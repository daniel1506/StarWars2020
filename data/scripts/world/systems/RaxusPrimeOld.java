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

public class RaxusPrimeOld {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Raxus Prime");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(20000.0f, 30000.0f);
		
		SectorEntityToken raxus_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.AVERAGE); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI raxus_star = system.initStar("raxus_prime", // unique id for this star 
										    "star_yellow",  // id in planets.json
										    600f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(255, 220, 190)); // light color in entire system, affects all entities				
	
	// Raxus Secundus System
		PlanetAPI raxus1 = system.addPlanet("raxus_secundus", raxus_star, "Raxus Secundus", "terran", 10, 135, 3500, 368);
		raxus1.getSpec().setPlanetColor(new Color(205,205,205,205));
		raxus1.setCustomDescriptionId("planet_raxus_secundus");
		raxus1.applySpecChanges();
		
		SectorEntityToken raxusStation = system.addCustomEntity("raxus_secundus_starforge", "Raxus Station", "sw_station_golan_iii", "sw_cis");
		raxusStation.setCircularOrbitPointingDown(system.getEntityById("raxus_secundus"), 0, 300, 30);		
		raxusStation.setInteractionImage("illustrations", "orbital");
		//kamino1Station.setCustomDescriptionId("station_kamino");
		
		SW_AddMarket.SW_AddMarket("sw_cis", (SectorEntityToken)raxus1, new ArrayList<String>(Arrays.asList((SectorEntityToken)raxusStation)), "Raxus Secundus", 9, new ArrayList<String>(Arrays.asList("habitable", "population_9", "mild_climate", "trade_center", "regional_capital")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "commerce", "farming", "megaport", "highcommand", "population", "heavybatteries", "sw_golan_iii_cis")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.2f);
		
	// Neimoidia System 
		PlanetAPI raxus2 = system.addPlanet("neimoidia", raxus_star, "Neimoidia", "terran", 20, 108f, 6000, 221f);
		//kamino2.getSpec().setPlanetColor(new Color(102, 0, 255,150));
		//raxus2.getSpec().setAtmosphereColor(new Color(255, 153, 51,150));
		//raxus2.getSpec().setCloudColor(new Color(255, 204, 0,200));
		//kamino2.getSpec().setGlowColor(new Color(0,255,205,62));
		//kamino2.getSpec().setUseReverseLightForGlow(true);
		//raxus2.applySpecChanges();
		
		SW_AddMarket.SW_AddMarket("sw_cis", (SectorEntityToken)raxus2, null, "Neimoidia", 8, new ArrayList<String>(Arrays.asList("habitable", "population_8", "mild_climate", "outpost", "farmland_rich", "organics_plentiful")), new ArrayList<String>(Arrays.asList("lightindustry", "farming", "mining", "spaceport", "population", "heavybatteries")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market")), 0.3f);
					
			// Fondor Relay, L5 (behind)
			SectorEntityToken relay = system.addCustomEntity("raxus_relay", // unique id
					 "Neimoidia Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "sw_cis"); // faction
			relay.setCircularOrbitPointingDown(system.getEntityById("raxus_prime"), 320, 4500, 220);
		
		SectorEntityToken neimoidiaStation = system.addCustomEntity("neimoidia_starforge", "Neimoidian Shipyards", "station_hightech2", "sw_cis");
		neimoidiaStation.setCircularOrbitPointingDown(system.getEntityById("neimoidia"), 0, 500, 30);		
		neimoidiaStation.setCustomDescriptionId("station_neimoidia");
				
		MarketAPI neimoidia_market = SW_AddMarket.SW_AddMarket("sw_cis", (SectorEntityToken)neimoidiaStation, null, "Neimoidian Shipyard", 8, new ArrayList<String>(Arrays.asList("no_atmosphere", "population_8", "outpost", "ai_core_admin")), new ArrayList<String>(Arrays.asList("lightindustry", "fuelprod", "spaceport", "waystation", "militarybase", "population", "heavybatteries", "orbitalworks", "sw_shipyards_cis")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.2f);				

		SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item4");
		neimoidia_market.getIndustry("militarybase").setAICoreId("beta_core");
		neimoidia_market.getIndustry("fuelprod").setAICoreId("beta_core");
		neimoidia_market.getIndustry("sw_shipyards_cis").setAICoreId("alpha_core");
		neimoidia_market.getIndustry("orbitalworks").setAICoreId("beta_core");
		neimoidia_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
	
	// Geonosis System
		PlanetAPI raxus3 = system.addPlanet("geonosis", raxus_star, "Geonosis", "desert", 50, 114f, 9000, 256f);
		raxus3.getSpec().setAtmosphereColor(new Color(255, 153, 51,150));
		raxus3.getSpec().setCloudColor(new Color(255, 204, 0,200));
		raxus3.applySpecChanges();
		raxus3.setCustomDescriptionId("planet_geonosis");
		
		SW_AddMarket.SW_AddMarket("sw_cis", (SectorEntityToken)raxus3, null, "Geonosis", 7, new ArrayList<String>(Arrays.asList("headquarters", "habitable", "population_7", "ore_rich", "rare_ore_rich", "volatiles_plentiful", "industrial_polity")), new ArrayList<String>(Arrays.asList("spaceport", "refining", "mining", "militarybase", "population", "heavybatteries", "sw_golan_ii_cis", "heavyindustry")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		SectorEntityToken geonosis_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_cis"); 
		geonosis_loc.setCircularOrbitPointingDown(raxus_star, 180-60, 7000, 340);		
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("raxus_inner_jump", "Raxus Prime Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(raxus1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(raxus1);
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
