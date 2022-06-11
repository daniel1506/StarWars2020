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

public class KaminoPrimeOld {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Kamino Prime");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(15000.0f, -40000.0f);
		
		SectorEntityToken kamino_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.AVERAGE); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI kamino_star = system.initStar("kamino_prime", // unique id for this star 
										    "star_red_giant",  // id in planets.json
										    600f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(255, 220, 190)); // light color in entire system, affects all entities				
	
	// Kamino System
		PlanetAPI kamino1 = system.addPlanet("kamino", kamino_star, "Kamino", "water", 10, 193, 3500, 463);
		kamino1.getSpec().setPlanetColor(new Color(205,205,205,205));
		kamino1.setCustomDescriptionId("planet_kamino");
		kamino1.applySpecChanges();
		
		SectorEntityToken kaminoStation = system.addCustomEntity("kamino_starforge", "Kamino Station", "sw_station_golan_iii", "sw_republic");
		kaminoStation.setCircularOrbitPointingDown(system.getEntityById("kamino"), 0, 300, 30);		
		kaminoStation.setInteractionImage("illustrations", "orbital");
		//kamino1Station.setCustomDescriptionId("station_kamino");
		
		SW_AddMarket.SW_AddMarket("sw_republic", (SectorEntityToken)kamino1, new ArrayList<String>(Arrays.asList((SectorEntityToken)kaminoStation)), "Kamino", 7, new ArrayList<String>(Arrays.asList("habitable", "population_7", "mild_climate", "water_surface", "headquarters", "regional_capital")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "spaceport", "aquaculture", "highcommand", "population", "heavybatteries", "sw_golan_iii_republic")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.2f);
		
	// Fondor System 
		PlanetAPI kamino2 = system.addPlanet("fondor", kamino_star, "Fondor", "ecumenopolis", 20, 91, 6000, 412);
		//kamino2.getSpec().setPlanetColor(new Color(102, 0, 255,150));
		kamino2.getSpec().setAtmosphereColor(new Color(255, 153, 153,150));
		//kamino2.getSpec().setCloudColor(new Color(220,250,240,200));
		//kamino2.getSpec().setGlowColor(new Color(0,255,205,62));
		//kamino2.getSpec().setUseReverseLightForGlow(true);
		kamino2.applySpecChanges();
		
		SW_AddMarket.SW_AddMarket("sw_republic", (SectorEntityToken)kamino2, null, "Fondor", 9, new ArrayList<String>(Arrays.asList("sw_ecumenopolis", "habitable", "population_9", "mild_climate", "trade_center")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "commerce", "megaport", "population", "heavybatteries")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market")), 0.1f);
					
			// Fondor Relay, L5 (behind)
			SectorEntityToken relay = system.addCustomEntity("fondor_relay", // unique id
					 "Fondor Relay", // name - if null, defaultName from custom_entities.json will be used
					 "comm_relay", // type of object, defined in custom_entities.json
					 "sw_republic"); // faction
			relay.setCircularOrbitPointingDown(system.getEntityById("kamino_prime"), 320, 4500, 220);
		
		SectorEntityToken fondorStation = system.addCustomEntity("fondor_starforge", "Fondor Shipyards", "station_hightech2", "sw_republic");
		fondorStation.setCircularOrbitPointingDown(system.getEntityById("fondor"), 0, 500, 30);		
		fondorStation.setCustomDescriptionId("station_fondor");
				
		MarketAPI fondor_market = SW_AddMarket.SW_AddMarket("sw_republic", (SectorEntityToken)fondorStation, null, "Fondor Shipyard", 8, new ArrayList<String>(Arrays.asList("no_atmosphere", "population_8", "outpost", "ai_core_admin")), new ArrayList<String>(Arrays.asList("lightindustry", "fuelprod", "spaceport", "waystation", "militarybase", "population", "heavybatteries", "orbitalworks", "sw_shipyards_republic")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);				

		SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item3");
		fondor_market.getIndustry("militarybase").setAICoreId("beta_core");
		fondor_market.getIndustry("fuelprod").setAICoreId("beta_core");
		fondor_market.getIndustry("sw_shipyards_republic").setAICoreId("alpha_core");
		fondor_market.getIndustry("orbitalworks").setAICoreId("beta_core");
		fondor_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
	
	// Kashyyyk System
		PlanetAPI kamino3 = system.addPlanet("kashyyyk", kamino_star, "Kashyyyk", "jungle", 50, 128f, 9000, 381f);
		kamino3.getSpec().setPlanetColor(new Color(0, 204, 102, 255));
		kamino3.applySpecChanges();
		kamino3.setCustomDescriptionId("planet_kashyyyk");
		
		SW_AddMarket.SW_AddMarket("sw_republic", (SectorEntityToken)kamino3, null, "Kashyyyk", 6, new ArrayList<String>(Arrays.asList("farmland_rich", "habitable", "population_6", "ore_rich", "rare_ore_rich", "organics_plentiful", "volatiles_plentiful")), new ArrayList<String>(Arrays.asList("spaceport", "refining", "mining", "militarybase", "population", "heavybatteries", "sw_golan_ii_republic", "farming")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		SectorEntityToken kashyyyk_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_republic"); 
		kashyyyk_loc.setCircularOrbitPointingDown(kamino_star, 180-60, 7000, 340);		
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("kamino_inner_jump", "Kamino Prime Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(kamino1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(kamino1);
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
