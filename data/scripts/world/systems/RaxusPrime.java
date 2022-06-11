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

public class RaxusPrime {

	public void generate(SectorAPI sector) {
		
		StarSystemAPI system = sector.createStarSystem("Raxus");
		LocationAPI hyper = Global.getSector().getHyperspace();
		
		system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");
		system.getLocation().set(45781.1f, 41681.3f);
		//system.getLocation().set(-6833f, 9454f);
		//Star Wars map to Starsector map ratio: 1x1 (grid) : 6833x4727
		
		SectorEntityToken raxus_nebula = Misc.addNebulaFromPNG("data/campaign/terrain/valhalla_nebula.png",
				  0, 0, // center of nebula
				  system, // location to add to
				  "terrain", "nebula_blue", // "nebula_blue", // texture to use, uses xxx_map for map
				  4, 4, StarAge.YOUNG); // number of cells in texture
		
		// create the star and generate the hyperspace anchor for this system
		PlanetAPI raxus_star = system.initStar("raxus_prime", // unique id for this star 
										    "star_yellow",  // id in planets.json
										    700f, 		  // radius (in pixels at default zoom)
										    500); // corona radius, from star edge
		system.setLightColor(new Color(255, 255, 240)); // light color in entire system, affects all entities				
	
		//light: "Blue(240, 255, 255)", "Yellow(255, 255, 240)", "Orange(255, 230, 205)", "White(245, 250, 255)"
		//star_type: "star_neutron", "black_hole", "star_yellow", "star_white", "star_blue_giant", "star_blue_supergiant"
		//star_type: "star_orange", "star_orange_giant", "star_red_supergiant", "star_red_giant", "star_red_dwarf", "star_browndwarf", ""
	
		PlanetAPI raxus1 = system.addPlanet("raxus_secundus", raxus_star, "Raxus Secundus", "terran", 24, 122.4f, 5000, 110);
		raxus1.getSpec().setPlanetColor(new Color(205,205,205,205));
		raxus1.setCustomDescriptionId("planet_raxus_secundus");
		raxus1.applySpecChanges();
		
		
		//planet_type: "gas_giant", "ice_giant", "lava", "frozen", "frozen1-3", "barren", "barren_castiron"
		//planet_type: "barren_venuslike", "toxic", "toxic_cold", "jungle", "terran", "desert", "arid", "rocky_metallic"
		//planet_type: "cryovolcanic", "rocky_unstable", "water", "rocky_ice", "irradiated", "tundra", "barren-bombarded"
		//planet_type: "barren-desert", "terran-eccentric", "ecumenopolis"
		
		//SectorEntityToken mandaloreStation = system.addCustomEntity("mandalore_starforge", "Mandal Hypernautics", "sw_station_golan_iii", "sw_mandalorian");
		//mandaloreStation.setCircularOrbitPointingDown(system.getEntityById("mandalore"), 0, 250, 30);		
		//mandaloreStation.setInteractionImage("illustrations", "orbital");
		//mandaloreStation.setCustomDescriptionId("station_mandalore");
		
		MarketAPI raxus_market = SW_AddMarket.SW_AddMarket("sw_cis", (SectorEntityToken)raxus1, null, "Raxus Secundus", 9, new ArrayList<String>(Arrays.asList("population_9", "habitable", "mild_climate", "terran", "regional_capital", "urbanized_polity", "free_market")), new ArrayList<String>(Arrays.asList("population", "megaport", "highcommand", "commerce", "lightindustry", "heavyindustry", "patrolhq", "heavybatteries", "sw_golan_iii_cis", "planetaryshield")), new ArrayList<String>(Arrays.asList("storage", "black_market", "open_market", "generic_military")), 0.3f);
		
		//SpecialItemData nanoforge = new SpecialItemData("pristine_nanoforge", "special_item1");
		//copero_market.getIndustry("orbitalworks").setSpecialItem(nanoforge);
		raxus_market.getIndustry("highcommand").setAICoreId("alpha_core");
		raxus_market.getIndustry("commerce").setAICoreId("alpha_core");		
		raxus_market.getIndustry("lightindustry").setAICoreId("alpha_core");
		raxus_market.getIndustry("heavyindustry").setAICoreId("alpha_core");
		raxus_market.getIndustry("patrolhq").setAICoreId("alpha_core");
		raxus_market.getIndustry("sw_golan_iii_cis").setAICoreId("alpha_core");
		raxus_market.getIndustry("population").setAICoreId("beta_core");
		raxus_market.getIndustry("megaport").setAICoreId("beta_core");
		raxus_market.getIndustry("heavybatteries").setAICoreId("beta_core");
		raxus_market.getIndustry("planetaryshield").setAICoreId("beta_core");
		
		//Industrie: "farming", "aquaculture", "mining", "techmining", "refining", "spaceport", "megaport",
		//Industrie: "lightindustry", "heavyindustry", "orbitalworks", "fuelprod", "commerce", "heavybatteries"
		//Industrie: "patrolhq", "militarybase", "highcommand", "planetaryshield", "waystation", "cryosanctum"
		
		//conditions: "farmland_rich", "organics_abundant", "volatiles_plentiful", "rare_ore_rich", "ore_ultrarich"
		//conditions: "pollution", "water_surface", "mild_climate", "hot", "cold", "ruins_extensive", "habitable"
		//conditions: "hydroponics_complex", "headquarters", "water", "jungle", "terran", "desert", "ice", "frontier"
		//conditions: "urbanized_polity", "industrial_polity", "rural_polity", "trade_center", "shipbreaking_center"
		//conditions: "outpost", "regional_capital", "free_market", "sw_ecumenopolis"						
		
		//SectorEntityToken ryloth_loc = system.addCustomEntity(null,null, "sensor_array_makeshift","sw_zann"); 
		//ryloth_loc.setCircularOrbitPointingDown(ryloth_star, 180-60, 7300, 340);		
		
		float outermostOrbitDistance = StarSystemGenerator.addOrbitingEntities(
			system, 
			raxus_star, 
			StarAge.AVERAGE, //This setting determines what kind of potential entities are added.
			2, 3, //Min-Max entities to add, here we'll just add 1 entity!
			2000, //Radius to start adding at. Make sure it's greater than your star's actual radius! You can have planets inside a star otherwise (maybe cool???) 
			1, //Name offset - next planet will be <system name> <roman numeral of this parameter + 1> if using system-based names.
			false);
		
		// jump point 
		JumpPointAPI jumpPoint = Global.getFactory().createJumpPoint("raxus_inner_jump", "Raxus Secundus Inner System Jump-point");
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
