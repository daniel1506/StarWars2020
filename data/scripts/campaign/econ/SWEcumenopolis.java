package data.scripts.campaign.econ;

import com.fs.starfarer.api.impl.campaign.econ.WorldFarming;

public class SWEcumenopolis extends WorldFarming {

	public SWEcumenopolis() {
		super(0.2f, 0.00005f);
	}
	
	@Override
	public boolean showIcon() {
		return true;
	}

}
