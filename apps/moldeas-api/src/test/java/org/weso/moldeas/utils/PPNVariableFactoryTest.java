package org.weso.moldeas.utils;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.ScoredNUTSTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.ScoredYearsTO;


public class PPNVariableFactoryTest {

	@Test
	public void testCreateObjects(){
		Assert.assertTrue(PPNVariableFactory.createScoreTO(0, 1.0) instanceof ScoredNUTSTO);
		Assert.assertTrue(PPNVariableFactory.createScoreTO(2000, 1.0)instanceof ScoredNUTSTO);
		Assert.assertTrue(PPNVariableFactory.createScoreTO(2001, 1.0) == null);
		Assert.assertTrue(PPNVariableFactory.createScoreTO(2011, 1.0)instanceof ScoredYearsTO);
		Assert.assertTrue(PPNVariableFactory.createScoreTO(2012, 1.0)instanceof ScoredYearsTO);
		long cpvId = 1000000;
		long cpvIdMax = 99999999;
		Assert.assertTrue(PPNVariableFactory.createScoreTO(cpvId, 1.0)instanceof ScoredPSCTO);
		Assert.assertTrue(PPNVariableFactory.createScoreTO(cpvIdMax, 1.0)instanceof ScoredPSCTO);
	}
}
