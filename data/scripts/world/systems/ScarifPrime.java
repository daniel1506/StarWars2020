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

public class ScarifPrime {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Scarif Prime");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(43731.2f, -23915.5f);
		
		SectorEntityToken scarif_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI scarif_star = system.initStar("scarif_prime", // unique id for this star 
										    "star_yellow",  // id in planets.json
										    700f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(255, 255, 240)); // light color in entire system, affects all entities				
		
		PlanetAPI scarif1 = system.addPlanet("scarif", scarif_star, "Scarif", "terran", 24, 455.6f, 3500, 366);
		scarif1.getSpec().setPlanetColor(new Color(205,205,205,205));
		scarif1.setCustomDescriptionId("planet_scarif");
		scarif1.applySpecChanges();
		
		//SectorEntityToken mandaloreStation = system.addCustomEntity("mandalore_starforge", "Mandal Hypernautics", "sw_station_golan_iii", "sw_mandalorian");
		//mandaloreStation.setCircularOrbitPointingDown(system.getEntityById("mandalore"), 0, 250, 30);		
		//mandaloreStation.setInteractionImage("illustrations", "orbital");
		//mandaloreStation.setCustomDescriptionId("station_mandalore");
		
		MarketAPI scarif_market = SW_AddMarket.SW_AddMarket("sw_empire", (SectorEntityToken)scarif1, null, "Scarif", 6, new ArrayList<String>(Arrays.asList("terran", "habitable", "population_6", "mild_climate", "outpost", "farmland_rich", "organics_abundant", "volatiles_plentiful")), new ArrayList<String>(Arrays.asList("population", "spaceport", "patrolhq", "militarybase", "farming", "mining", "fuelprod", "heavybatteries", "sw_shieldgate_imperial", "planetaryshield")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.2f);
		
		scarif_market.getIndustry("militarybase").setAICoreId("alpha_core");
		scarif_market.getIndustry("farming").setAICoreId("alpha_core");
		scarif_market.getIndustry("mining").setAICoreId("alpha_core");
		scarif_market.getIndustry("fuelprod").setAICoreId("alpha_core");
		scarif_market.getIndustry("patrolhq").setAICoreId("alpha_core");		
		scarif_market.getIndustry("sw_shieldgate_imperial").setAICoreId("alpha_core");
		scarif_market.getIndustry("population").setAICoreId("beta_core");
		scarif_market.getIndustry("spaceport").setAICoreId("beta_core");
		scarif_market.getIndustry("heavybatteries").setAICoreId("beta_core");
		scarif_market.getIndustry("planetaryshield").setAICoreId("alpha_core");
		
		//Industrie: "farming", "aquaculture", "mining", "techmining", "refining", "spaceport", "megaport",
		//Industrie: "lightindustry", "heavyindustry", "orbitalworks", "fuelprod", "commerce", "heavybatteries"
		//Industrie: "patrolhq", "militarybase", "highcommand", "planetaryshield", "waystation", "cryosanctum"
		
		//conditions: "farmland_rich", "organics_abundant", "volatiles_plentiful", "rare_ore_rich", "ore_ultrarich"
		//conditions: "pollution", "water_surface", "mild_climate", "hot", "cold", "ruins_extensive", "habitable"
		//conditions: "hydroponics_complex", "headquarters", "water", "jungle", "terran", "desert", "ice", "frontier"
		//conditions: "urbanized_polity", "industrial_polity", "rural_polity", "trade_center", "shipbreaking_center"
		//conditions: "outpost", "regional_capital", "free_market"
		
		//SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item1");
		//mandalore_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);					
		
		//SectorEntityToken ryloth_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_zann"); 
		//ryloth_loc.setCircularOrbitPointingDown(ryloth_star, 180-60, 7300, 340);		
		
		float outermostOrbitDistance = StarSystemGenerator.addOrbitingEntities(
			system, 
			scarif_star, 
			StarAge.AVERAGE, //This setting determines what kind of potential entities are added.
			3, 4, //Min-Max entities to add, here we'll just add 1 entity!
			2000, //Radius to start adding at. Make sure it's greater than your star's actual radius! You can have planets inside a star otherwise (maybe cool???) 
			1, //Name offset - next planet will be <system name> <roman numeral of this parameter + 1> if using system-based names.
			false);
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("scarif_inner_jump", "Scarif Inner System Jump-point");
		OrbitAPI orbit = Global.getFactory().createCircularOrbit(scarif1, 0, 1500, 65);
		jumpPoint.setOrbit(orbit);
		jumpPoint.setRelatedPlanet(scarif1);
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
