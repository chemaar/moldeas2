package org.weso.moldeas.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.ScoredPSCTO;


public class ScoredPSCTOComparatorTest {

	@Test
	public void testCompare(){
		ScoredPSCTO pscTO1 = new ScoredPSCTO(null,1.0);
		ScoredPSCTO pscTO2 = new ScoredPSCTO(null,2.0);
		ScoredPSCTO pscTO3 = new ScoredPSCTO(null,3.0);
		List<ScoredPSCTO> set = new LinkedList<ScoredPSCTO>();		
		set.add(pscTO2);
		set.add(pscTO3);
		set.add(pscTO1);
		ScoredPSCTOComparator comparator = new ScoredPSCTOComparator();
		Collections.sort(set, comparator);
		Assert.assertEquals(pscTO3.getScore(),set.get(0).getScore());		
		
	}
}
