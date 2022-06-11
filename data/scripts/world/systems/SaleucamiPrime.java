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

public class SaleucamiPrime {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Saleucami");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(43047.9f, 24940.5f);
		
		SectorEntityToken saleucami_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI saleucami_star = system.initStar("saleucami_prime", // unique id for this star 
										    "star_white",  // id in planets.json
										    700f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(250, 250, 255)); // light color in entire system, affects all entities				
		
		PlanetAPI saleucami1 = system.addPlanet("saleucami", saleucami_star, "Saleucami", "terran", 26, 149.2f, 5000, 392);
		saleucami1.getSpec().setPlanetColor(new Color(205,205,205,205));
		saleucami1.setCustomDescriptionId("planet_saleucami");
		saleucami1.applySpecChanges();
		
		//SectorEntityToken saleucamiStation = system.addCustomEntity("ryloth_starforge", "Golan III Space Defense SpaceGun", "sw_station_golan_iii", "sw_zann");
		//rylothStation.setCircularOrbitPointingDown(system.getEntityById("ryloth"), 0, 250, 30);		
		//rylothStation.setInteractionImage("illustrations", "orbital");
		//rylothStation.setCustomDescriptionId("station_coruscant");
		
		MarketAPI saleucami_market = SW_AddMarket.SW_AddMarket("sw_zann", (SectorEntityToken)saleucami1, null, "Saleucami", 9, new ArrayList<String>(Arrays.asList("population_9", "terran", "habitable", "mild_climate", "urbanized_polity", "trade_center", "free_market")), new ArrayList<String>(Arrays.asList("population", "megaport", "orbitalworks", "lightindustry", "militarybase", "commerce", "heavybatteries", "sw_golan_iii_zann", "waystation")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item1");
		saleucami_market.getIndustry("militarybase").setAICoreId("alpha_core");
		saleucami_market.getIndustry("commerce").setAICoreId("alpha_core");
		saleucami_market.getIndustry("lightindustry").setAICoreId("alpha_core");
		saleucami_market.getIndustry("waystation").setAICoreId("alpha_core");
		saleucami_market.getIndustry("sw_golan_iii_zann").setAICoreId("alpha_core");
		saleucami_market.getIndustry("population").setAICoreId("beta_core");
		saleucami_market.getIndustry("megaport").setAICoreId("beta_core");
		saleucami_market.getIndustry("heavybatteries").setAICoreId("beta_core");
		saleucami_market.getIndustry("orbitalworks").setAICoreId("alpha_core");
		saleucami_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
		
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
			saleucami_star, 
			StarAge.AVERAGE, //This setting determines what kind of potential entities are added.
			8, 8, //Min-Max entities to add, here we'll just add 1 entity!
			2000, //Radius to start adding at. Make sure it's greater than your star's actual radius! You can have planets inside a star otherwise (maybe cool???) 
			1, //Name offset - next planet will be <system name> <roman numeral of this parameter + 1> if using system-based names.
			false);
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("saleucami_inner_jump", "Saleucami Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(saleucami1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(saleucami1);
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
