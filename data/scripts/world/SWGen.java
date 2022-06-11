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
import data.scripts.world.systems.KuatPrime;
import data.scripts.world.systems.BeshqekPrime;
import data.scripts.world.systems.ChandrilaPrime;
import data.scripts.world.systems.MonCalamariPrime;
import data.scripts.world.systems.HothPrime;
import data.scripts.world.systems.KaminoPrime;
import data.scripts.world.systems.FondorPrime;
import data.scripts.world.systems.KashyyykPrime;
import data.scripts.world.systems.RaxusPrime;
import data.scripts.world.systems.NeimoidiaPrime;
import data.scripts.world.systems.GeonosisPrime;
import data.scripts.world.systems.RylothPrime;
import data.scripts.world.systems.HyporiPrime;
import data.scripts.world.systems.SaleucamiPrime;
import data.scripts.world.systems.MandalorePrime;
import data.scripts.world.systems.BasiliskPrime;
import data.scripts.world.systems.ConcordDawnPrime;
import data.scripts.world.systems.BrightJewelPrime;
import data.scripts.world.systems.MustafarPrime;
import data.scripts.world.systems.ScarifPrime;
import data.scripts.world.systems.LiannaPrime;
import data.scripts.world.systems.IlumPrime;
import data.scripts.world.systems.ExegolPrime;
import data.scripts.world.systems.LothalPrime;
import data.scripts.world.systems.CsillaPrime;
import data.scripts.world.systems.CoperoPrime;
import data.scripts.world.systems.NaporarPrime;

public class SWGen implements SectorGeneratorPlugin {

	public void generate(SectorAPI sector) {
			
		new CoruscantPrime().generate(sector);
		new KuatPrime().generate(sector);
		new BeshqekPrime().generate(sector);
		new ChandrilaPrime().generate(sector);
		new MonCalamariPrime().generate(sector);
		new HothPrime().generate(sector);
		new KaminoPrime().generate(sector);
		new FondorPrime().generate(sector);
		new KashyyykPrime().generate(sector);
		new RaxusPrime().generate(sector);
		new NeimoidiaPrime().generate(sector);
		new GeonosisPrime().generate(sector);
		new RylothPrime().generate(sector);
		new HyporiPrime().generate(sector);
		new SaleucamiPrime().generate(sector);
		new MandalorePrime().generate(sector);
		new BasiliskPrime().generate(sector);
		new ConcordDawnPrime().generate(sector);
		new BrightJewelPrime().generate(sector);
		new MustafarPrime().generate(sector);
		new LiannaPrime().generate(sector);
		new ScarifPrime().generate(sector);
		new IlumPrime().generate(sector);
		new ExegolPrime().generate(sector);
		new LothalPrime().generate(sector);
		new CsillaPrime().generate(sector);
		new CoperoPrime().generate(sector);
		new NaporarPrime().generate(sector);
		
		
		LocationAPI hyper = Global.getSector().getHyperspace();
		//SectorEntityToken empireLabel = hyper.addCustomEntity("sw_empire_label", null, "sw_empire_label", null);
		//SectorEntityToken rebelLabel = hyper.addCustomEntity("sw_rebel_label", null, "sw_rebel_label", null);		
		
		//empireLabel.setFixedLocation(1400, 5000);
		//rebelLabel.setFixedLocation(10000, 3500);
		
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_empire");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_rebel");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_republic");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_cis");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_mandalorian");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_zann");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_black_sun");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_first_order");
		SharedData.getData().getPersonBountyEventData().addParticipatingFaction("sw_chiss");
		
		
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
		FactionAPI republic = sector.getFaction("sw_republic");
		FactionAPI cis = sector.getFaction("sw_cis");
		FactionAPI mandalorian = sector.getFaction("sw_mandalorian");
		FactionAPI zann = sector.getFaction("sw_zann");
		FactionAPI blacksun = sector.getFaction("sw_black_sun");
		FactionAPI firstorder = sector.getFaction("sw_first_order");
		FactionAPI chiss = sector.getFaction("sw_chiss");
				
		player.setRelationship(empire.getId(), RepLevel.NEUTRAL);
		player.setRelationship(rebel.getId(), RepLevel.NEUTRAL);
		player.setRelationship(republic.getId(), RepLevel.NEUTRAL);
		player.setRelationship(cis.getId(), RepLevel.NEUTRAL);
		player.setRelationship(mandalorian.getId(), RepLevel.NEUTRAL);
		player.setRelationship(firstorder.getId(), RepLevel.NEUTRAL);
		player.setRelationship(chiss.getId(), RepLevel.NEUTRAL);
		player.setRelationship(zann.getId(), RepLevel.INHOSPITABLE);
		player.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
				
		empire.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		empire.setRelationship(tritachyon.getId(), RepLevel.WELCOMING);
		empire.setRelationship(path.getId(), RepLevel.HOSTILE);
		empire.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
		empire.setRelationship(persean.getId(), RepLevel.HOSTILE);
		empire.setRelationship(diktat.getId(), RepLevel.WELCOMING);
		empire.setRelationship(rebel.getId(), RepLevel.VENGEFUL);
		empire.setRelationship(republic.getId(), RepLevel.HOSTILE);
		empire.setRelationship(cis.getId(), RepLevel.HOSTILE);
		empire.setRelationship(mandalorian.getId(), RepLevel.INHOSPITABLE);
		empire.setRelationship(zann.getId(), RepLevel.HOSTILE);
		empire.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
		empire.setRelationship(firstorder.getId(), RepLevel.COOPERATIVE);
		empire.setRelationship(chiss.getId(), RepLevel.COOPERATIVE);
		
		rebel.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(path.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(persean.getId(), RepLevel.FRIENDLY);
		rebel.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
		rebel.setRelationship(independent.getId(), RepLevel.FRIENDLY);
		rebel.setRelationship(empire.getId(), RepLevel.VENGEFUL);
		rebel.setRelationship(republic.getId(), RepLevel.COOPERATIVE);
		rebel.setRelationship(cis.getId(), RepLevel.NEUTRAL);
		rebel.setRelationship(mandalorian.getId(), RepLevel.FAVORABLE);
		rebel.setRelationship(zann.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(firstorder.getId(), RepLevel.HOSTILE);
		rebel.setRelationship(chiss.getId(), RepLevel.HOSTILE);
		
		republic.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		republic.setRelationship(tritachyon.getId(), RepLevel.FRIENDLY);
		republic.setRelationship(path.getId(), RepLevel.HOSTILE);
		republic.setRelationship(hegemony.getId(), RepLevel.NEUTRAL);
		republic.setRelationship(persean.getId(), RepLevel.WELCOMING);
		republic.setRelationship(independent.getId(), RepLevel.FRIENDLY);
		republic.setRelationship(rebel.getId(), RepLevel.COOPERATIVE);
		republic.setRelationship(empire.getId(), RepLevel.HOSTILE);
		republic.setRelationship(cis.getId(), RepLevel.HOSTILE);
		republic.setRelationship(mandalorian.getId(), RepLevel.NEUTRAL);
		republic.setRelationship(zann.getId(), RepLevel.HOSTILE);
		republic.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
		republic.setRelationship(firstorder.getId(), RepLevel.HOSTILE);
		republic.setRelationship(chiss.getId(), RepLevel.NEUTRAL);
		
		cis.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		cis.setRelationship(tritachyon.getId(), RepLevel.WELCOMING);
		cis.setRelationship(path.getId(), RepLevel.HOSTILE);
		cis.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
		cis.setRelationship(persean.getId(), RepLevel.SUSPICIOUS);
		cis.setRelationship(independent.getId(), RepLevel.FRIENDLY);
		cis.setRelationship(rebel.getId(), RepLevel.NEUTRAL);
		cis.setRelationship(republic.getId(), RepLevel.HOSTILE);
		cis.setRelationship(empire.getId(), RepLevel.HOSTILE);
		cis.setRelationship(mandalorian.getId(), RepLevel.NEUTRAL);
		cis.setRelationship(zann.getId(), RepLevel.FAVORABLE);
		cis.setRelationship(blacksun.getId(), RepLevel.FRIENDLY);
		cis.setRelationship(firstorder.getId(), RepLevel.NEUTRAL);
		cis.setRelationship(chiss.getId(), RepLevel.NEUTRAL);
		
		mandalorian.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		mandalorian.setRelationship(tritachyon.getId(), RepLevel.WELCOMING);
		mandalorian.setRelationship(path.getId(), RepLevel.HOSTILE);
		mandalorian.setRelationship(independent.getId(), RepLevel.FRIENDLY);
		mandalorian.setRelationship(hegemony.getId(), RepLevel.HOSTILE);
		mandalorian.setRelationship(persean.getId(), RepLevel.SUSPICIOUS);
		mandalorian.setRelationship(rebel.getId(), RepLevel.FAVORABLE);
		mandalorian.setRelationship(republic.getId(), RepLevel.NEUTRAL);
		mandalorian.setRelationship(cis.getId(), RepLevel.NEUTRAL);
		mandalorian.setRelationship(empire.getId(), RepLevel.HOSTILE);
		mandalorian.setRelationship(zann.getId(), RepLevel.COOPERATIVE);
		mandalorian.setRelationship(blacksun.getId(), RepLevel.FAVORABLE);
		mandalorian.setRelationship(firstorder.getId(), RepLevel.INHOSPITABLE);
		mandalorian.setRelationship(chiss.getId(), RepLevel.NEUTRAL);
		
		zann.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		zann.setRelationship(tritachyon.getId(), RepLevel.WELCOMING);
		zann.setRelationship(path.getId(), RepLevel.HOSTILE);
		zann.setRelationship(independent.getId(), RepLevel.NEUTRAL);
		zann.setRelationship(hegemony.getId(), RepLevel.HOSTILE);
		zann.setRelationship(persean.getId(), RepLevel.HOSTILE);
		zann.setRelationship(rebel.getId(), RepLevel.HOSTILE);
		zann.setRelationship(republic.getId(), RepLevel.HOSTILE);
		zann.setRelationship(cis.getId(), RepLevel.FAVORABLE);
		zann.setRelationship(empire.getId(), RepLevel.HOSTILE);
		zann.setRelationship(mandalorian.getId(), RepLevel.COOPERATIVE);
		zann.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
		zann.setRelationship(firstorder.getId(), RepLevel.HOSTILE);
		zann.setRelationship(chiss.getId(), RepLevel.INHOSPITABLE);
		
		blacksun.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(tritachyon.getId(), RepLevel.SUSPICIOUS);
		blacksun.setRelationship(path.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(independent.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(hegemony.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(persean.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(rebel.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(republic.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(cis.getId(), RepLevel.FAVORABLE);
		blacksun.setRelationship(empire.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(mandalorian.getId(), RepLevel.FAVORABLE);
		blacksun.setRelationship(zann.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(firstorder.getId(), RepLevel.HOSTILE);
		blacksun.setRelationship(chiss.getId(), RepLevel.HOSTILE);
		
		firstorder.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(tritachyon.getId(), RepLevel.WELCOMING);
		firstorder.setRelationship(path.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
		firstorder.setRelationship(persean.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(diktat.getId(), RepLevel.WELCOMING);
		firstorder.setRelationship(rebel.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(republic.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(cis.getId(), RepLevel.NEUTRAL);
		firstorder.setRelationship(mandalorian.getId(), RepLevel.INHOSPITABLE);
		firstorder.setRelationship(zann.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
		firstorder.setRelationship(empire.getId(), RepLevel.COOPERATIVE);
		firstorder.setRelationship(chiss.getId(), RepLevel.COOPERATIVE);
		
		chiss.setRelationship(pirates.getId(), RepLevel.HOSTILE);
		chiss.setRelationship(tritachyon.getId(), RepLevel.WELCOMING);
		chiss.setRelationship(path.getId(), RepLevel.HOSTILE);
		chiss.setRelationship(hegemony.getId(), RepLevel.INHOSPITABLE);
		chiss.setRelationship(persean.getId(), RepLevel.HOSTILE);
		chiss.setRelationship(diktat.getId(), RepLevel.WELCOMING);
		chiss.setRelationship(rebel.getId(), RepLevel.HOSTILE);
		chiss.setRelationship(republic.getId(), RepLevel.NEUTRAL);
		chiss.setRelationship(cis.getId(), RepLevel.NEUTRAL);
		chiss.setRelationship(mandalorian.getId(), RepLevel.NEUTRAL);
		chiss.setRelationship(zann.getId(), RepLevel.INHOSPITABLE);
		chiss.setRelationship(blacksun.getId(), RepLevel.HOSTILE);
		chiss.setRelationship(empire.getId(), RepLevel.COOPERATIVE);
		chiss.setRelationship(firstorder.getId(), RepLevel.COOPERATIVE);
				
		this.setModRelationshipsEmpire(empire);
		this.setModRelationshipsEmpire(cis);
		this.setModRelationshipsRebel(rebel);
		this.setModRelationshipsRebel(republic);
		this.setModRelationshipsRebel(mandalorian);
		this.setModRelationshipsRebel(zann);
		this.setModRelationshipsRebel(blacksun);
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




