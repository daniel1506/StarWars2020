package data.missions.sw_battle_of_yavin;

import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin
{
    public void defineMission(final MissionDefinitionAPI api) {
        api.initFleet(FleetSide.PLAYER, "RBL", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "IMP", FleetGoal.ATTACK, true);
        api.setFleetTagline(FleetSide.PLAYER, "Rebel Alliance Attack Fleet");
        api.setFleetTagline(FleetSide.ENEMY, "Death Star I");
        api.addBriefingItem("Destroy the Death Star!");
        api.addToFleet(FleetSide.PLAYER, "sw_mc75_mission", FleetMemberType.SHIP, "Profundity", true);
        api.addToFleet(FleetSide.PLAYER, "sw_hammerhead_rebel", FleetMemberType.SHIP, "Consonance", false);
        api.addToFleet(FleetSide.PLAYER, "sw_hammerhead_rebel", FleetMemberType.SHIP, "Lightmaker", false);
        api.addToFleet(FleetSide.PLAYER, "sw_hammerhead_rebel", FleetMemberType.SHIP, "Amity's Arrow", false);
		api.addToFleet(FleetSide.PLAYER, "sw_nebulon_b_rebel", FleetMemberType.SHIP, "Remembrance", false);
        api.addToFleet(FleetSide.PLAYER, "sw_nebulon_b_rebel", FleetMemberType.SHIP, "Redemption", false);
        api.addToFleet(FleetSide.PLAYER, "sw_nebulon_b_rebel", FleetMemberType.SHIP, "Tyvarex", false);
        api.addToFleet(FleetSide.PLAYER, "sw_cr90_rebel", FleetMemberType.SHIP, "Tantive IV", false);
        api.addToFleet(FleetSide.PLAYER, "sw_cr90_rebel", FleetMemberType.SHIP, "Liberator", false);
        api.addToFleet(FleetSide.PLAYER, "sw_cr90_rebel", FleetMemberType.SHIP, "Tantive I", false);
        api.addToFleet(FleetSide.PLAYER, "sw_cr90_rebel", FleetMemberType.SHIP, "Transport One", false);
        api.addToFleet(FleetSide.PLAYER, "sw_gr75_medium", FleetMemberType.SHIP, "Liberty", false);
        api.addToFleet(FleetSide.PLAYER, "sw_gr75_medium", FleetMemberType.SHIP, "Nautilian", false);
		api.addToFleet(FleetSide.PLAYER, "sw_vcx100_light", FleetMemberType.SHIP, "Ghost", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Red Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Red Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Red Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Red Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Green Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Green Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Green Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Green Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_t65b_xwing_wing", FleetMemberType.FIGHTER_WING, "Blue Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_ywing_wing", FleetMemberType.FIGHTER_WING, "Gold Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_ywing_wing", FleetMemberType.FIGHTER_WING, "Gold Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_ywing_wing", FleetMemberType.FIGHTER_WING, "Gold Squadron", false);
		api.addToFleet(FleetSide.PLAYER, "sw_ywing_wing", FleetMemberType.FIGHTER_WING, "Gold Squadron", false);
        api.addToFleet(FleetSide.ENEMY, "sw_station_death_star_i_imperial", FleetMemberType.SHIP, "Death Star I", false);
		api.defeatOnShipLoss("Death Star I");
        final float width = 10000.0f;
        final float height = 10000.0f;
        api.initMap(-width / 2.0f, width / 2.0f, -height / 2.0f, height / 2.0f);
		api.addPlanet(0f, 0f, 2000f, "gas_giant", 1f, false);
		api.setPlanetBgSize(2000f, 2000f);
        /*final float minX = -width / 2.0f;
        final float minY = -height / 2.0f;
        api.addNebula(minX + width * 0.2f, minY + height * 0.6f, 1200.0f);
        api.addNebula(minX + width * 0.3f, minY + height * 0.4f, 2000.0f);
        api.addNebula(minX + width * 0.6f, minY + height * 0.7f, 1000.0f);
        api.addNebula(minX + width * 0.5f, minY + height * 0.2f, 500.0f);
        for (int i = 0; i < 8; ++i) {
            final float x = (float)Math.random() * width - width / 2.0f;
            final float y = (float)Math.random() * height - height / 2.0f;
            final float radius = 100.0f + (float)Math.random() * 400.0f;
            api.addNebula(x, y, radius);
        }*/
    }
}