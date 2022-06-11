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
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin.MagneticFieldParams;
import com.fs.starfarer.api.util.Misc;

import data.scripts.world.SW_AddMarket;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ConcordDawnPrime {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Concord Dawn");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(17082.5f, 30748.5f);
		
		SectorEntityToken concordDawn_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI concordDawn_star = system.initStar("concordDawn_prime", // unique id for this star 
										    "star_orange",  // id in planets.json
										    600f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(245, 250, 255)); // light color in entire system, affects all entities				
		
		PlanetAPI concordDawn1 = system.addPlanet("concordDawn", concordDawn_star, "Concord Dawn", "jungle", 30, 110, 5000, 305);
		concordDawn1.getSpec().setPlanetColor(new Color(205,205,205,205));
		concordDawn1.setCustomDescriptionId("planet_concordDawn");
		concordDawn1.applySpecChanges();
		
		//SectorEntityToken rylothStation = system.addCustomEntity("ryloth_starforge", "Golan III Space Defense SpaceGun", "sw_station_golan_iii", "sw_zann");
		//rylothStation.setCircularOrbitPointingDown(system.getEntityById("ryloth"), 0, 250, 30);		
		//rylothStation.setInteractionImage("illustrations", "orbital");
		//rylothStation.setCustomDescriptionId("station_coruscant");
		
		MarketAPI concordDawn_market = SW_AddMarket.SW_AddMarket("sw_mandalorian", (SectorEntityToken)concordDawn1, null, "Concord Dawn", 8, new ArrayList<String>(Arrays.asList("farmland_rich", "jungle", "habitable", "population_7", "mild_climate", "ore_rich", "rare_ore_rich")), new ArrayList<String>(Arrays.asList("population", "spaceport", "mining", "refining", "militarybase", "farming", "heavybatteries", "sw_golan_iii_mandal", "patrolhq")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.2f);
		
	// Kuat System 
		//PlanetAPI ryloth2 = system.addPlanet("kuat", coruscant_star, "Kuat", "terran", 20, 150f, 6000, 322f);
		//coruscant2.getSpec().setPlanetColor(new Color(205,205,205,205));
		//coruscant2.setCustomDescriptionId("planet_kuat");
		//coruscant2.applySpecChanges();
		
		//SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)coruscant2, null, "Kuat", 8, new ArrayList<String>(Arrays.asList("habitable", "population_8", "ore_rich", "rare_ore_rich", "volatiles_plentiful")), new ArrayList<String>(Arrays.asList("spaceport", "refining", "mining", "militarybase", "population", "heavybatteries", "lightindustry")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);		
					
			// kuat Relay, L5 (behind)
		//	SectorEntityToken relay = system.addCustomEntity("kuat_relay", // unique id
		//			 "Kuat Relay", // name - if null, defaultName from custom_entities.json will be used
		//			 "comm_relay", // type of object, defined in custom_entities.json
		//			 "sw_empire"); // faction
		//	relay.setCircularOrbitPointingDown( system.getEntityById("coruscant_prime"), 320, 4750, 220);
		
		//SectorEntityToken kuatStation = system.addCustomEntity("kuat_starforge", "Kuat Shipyards", "station_hightech2", "sw_empire");
		//kuatStation.setCircularOrbitPointingDown(system.getEntityById("kuat"), 0, 700, 30);		
		//kuatStation.setCustomDescriptionId("station_kuat");
				
		//MarketAPI kuat_market = SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)kuatStation, null, "Kuat Shipyard", 8, new ArrayList<String>(Arrays.asList("no_atmosphere", "population_8", "outpost", "ai_core_admin")), new ArrayList<String>(Arrays.asList("lightindustry", "fuelprod", "spaceport", "waystation", "militarybase", "population", "heavybatteries", "orbitalworks", "sw_shipyards_imperial")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);				

		//SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item1");
		concordDawn_market.getIndustry("militarybase").setAICoreId("alpha_core");
		concordDawn_market.getIndustry("farming").setAICoreId("alpha_core");
		concordDawn_market.getIndustry("mining").setAICoreId("alpha_core");
		concordDawn_market.getIndustry("refining").setAICoreId("alpha_core");
		concordDawn_market.getIndustry("patrolhq").setAICoreId("alpha_core");
		concordDawn_market.getIndustry("sw_golan_iii_mandal").setAICoreId("alpha_core");
		concordDawn_market.getIndustry("population").setAICoreId("beta_core");
		concordDawn_market.getIndustry("spaceport").setAICoreId("beta_core");
		concordDawn_market.getIndustry("heavybatteries").setAICoreId("beta_core");
		//kuat_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
		
		//PlanetAPI coruscant3 = system.addPlanet("byss", coruscant_star, "Byss", "terran", 50, 216f, 9000, 207f);
		//coruscant3.getSpec().setPlanetColor(new Color(0,205,0,255));
		//coruscant3.applySpecChanges();
		//coruscant3.setCustomDescriptionId("planet_byss");
		//coruscant3.setInteractionImage("illustrations", "mine");
		
		//SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)coruscant3, null, "Byss", 7, new ArrayList<String>(Arrays.asList("farmland_rich", "habitable", "population_7", "ore_rich", "rare_ore_rich", "organics_plentiful", "headquarters")), new ArrayList<String>(Arrays.asList("lightindustry", "heavyindustry", "spaceport", "fuelprod", "refining", "mining", "militarybase", "population", "heavybatteries", "sw_shieldgate_imperial", "farming")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);		
				
		//SectorEntityToken ryloth_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_zann"); 
		//ryloth_loc.setCircularOrbitPointingDown(ryloth_star, 180-60, 7300, 340);		
		
		float outermostOrbitDistance = StarSystemGenerator.addOrbitingEntities(
			system, 
			concordDawn_star, 
			StarAge.AVERAGE, //This setting determines what kind of potential entities are added.
			2, 3, //Min-Max entities to add, here we'll just add 1 entity!
			2000, //Radius to start adding at. Make sure it's greater than your star's actual radius! You can have planets inside a star otherwise (maybe cool???) 
			1, //Name offset - next planet will be <system name> <roman numeral of this parameter + 1> if using system-based names.
			false);
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("concordDawn_inner_jump", "Concord Dawn Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(concordDawn1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(concordDawn1);
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
