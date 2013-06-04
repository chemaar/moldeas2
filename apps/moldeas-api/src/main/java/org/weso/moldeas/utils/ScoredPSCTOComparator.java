package org.weso.moldeas.utils;

import java.util.Comparator;

import org.weso.moldeas.to.ScoredPSCTO;

public class ScoredPSCTOComparator implements Comparator<ScoredPSCTO>{

	@Override
	public int compare(ScoredPSCTO arg0, ScoredPSCTO arg1) {
		return -Double.compare(arg0.getScore(), arg1.getScore());
	}

}
