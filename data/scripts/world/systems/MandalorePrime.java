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

public class MandalorePrime {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Mandalore");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(17765.8f, 32115.1f);
		
		SectorEntityToken mandalore_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI mandalore_star = system.initStar("mandalore_prime", // unique id for this star 
										    "star_orange",  // id in planets.json
										    700f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(245, 250, 255)); // light color in entire system, affects all entities				
		
		PlanetAPI mandalore1 = system.addPlanet("mandalore", mandalore_star, "Mandalore", "terran", 19, 92, 5000, 366);
		mandalore1.getSpec().setPlanetColor(new Color(205,205,205,205));
		mandalore1.setCustomDescriptionId("planet_mandalore");
		mandalore1.applySpecChanges();
		
		//SectorEntityToken mandaloreStation = system.addCustomEntity("mandalore_starforge", "Mandal Hypernautics", "sw_station_golan_iii", "sw_mandalorian");
		//mandaloreStation.setCircularOrbitPointingDown(system.getEntityById("mandalore"), 0, 250, 30);		
		//mandaloreStation.setInteractionImage("illustrations", "orbital");
		//mandaloreStation.setCustomDescriptionId("station_mandalore");
		
		MarketAPI mandalore_market = SW_AddMarket.SW_AddMarket("sw_mandalorian", (SectorEntityToken)mandalore1, null, "Mandalore", 9, new ArrayList<String>(Arrays.asList("population_9", "habitable", "mild_climate", "terran", "regional_capital", "urbanized_polity", "trade_center")), new ArrayList<String>(Arrays.asList("population", "megaport", "highcommand", "commerce", "lightindustry", "orbitalworks", "waystation", "heavybatteries", "sw_golan_iii_mandal", "planetaryshield")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item1");
		mandalore_market.getIndustry("highcommand").setAICoreId("alpha_core");
		mandalore_market.getIndustry("lightindustry").setAICoreId("alpha_core");
		mandalore_market.getIndustry("commerce").setAICoreId("alpha_core");
		mandalore_market.getIndustry("waystation").setAICoreId("alpha_core");
		mandalore_market.getIndustry("sw_golan_iii_mandal").setAICoreId("alpha_core");
		mandalore_market.getIndustry("population").setAICoreId("beta_core");
		mandalore_market.getIndustry("megaport").setAICoreId("beta_core");
		mandalore_market.getIndustry("heavybatteries").setAICoreId("beta_core");
		mandalore_market.getIndustry("planetaryshield").setAICoreId("beta_core");
		mandalore_market.getIndustry("orbitalworks").setAICoreId("alpha_core");
		mandalore_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
		
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
			mandalore_star, 
			StarAge.AVERAGE, //This setting determines what kind of potential entities are added.
			8, 9, //Min-Max entities to add, here we'll just add 1 entity!
			2000, //Radius to start adding at. Make sure it's greater than your star's actual radius! You can have planets inside a star otherwise (maybe cool???) 
			1, //Name offset - next planet will be <system name> <roman numeral of this parameter + 1> if using system-based names.
			false);
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("mandalore_inner_jump", "Mandalore Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(mandalore1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(mandalore1);
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
