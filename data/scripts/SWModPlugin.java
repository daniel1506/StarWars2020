package data.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.BaseModPlugin;
import exerelin.campaign.SectorManager;
import data.scripts.world.SWGen;

public class SWModPlugin extends BaseModPlugin {
	
	public void onNewGame() {
        final boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (haveNexerelin) {
            SectorManager.getManager();
            if (!SectorManager.getCorvusMode()) {
                return;
            }
        }
        new SWGen().generate(Global.getSector());
    }
	
}
