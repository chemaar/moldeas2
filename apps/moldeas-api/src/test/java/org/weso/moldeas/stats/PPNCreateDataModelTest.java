package org.weso.moldeas.stats;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.StatsPairTO;


public class PPNCreateDataModelTest {

	@Test
	public void testCreateTable() throws Exception{
		PPNCreateDataModel ppnCreateDataModel = new PPNCreateDataModel("2012");
		ppnCreateDataModel.execute();
		Map<StatsPairTO, Integer> stats = ppnCreateDataModel.getPair();
		Set<Entry<StatsPairTO, Integer>> entries = stats.entrySet();
		Assert.assertEquals(24, entries.size());
	}
}
