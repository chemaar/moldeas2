package org.weso.moldeas.stats;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;
import org.weso.moldeas.to.StatsPairTO;


public class PPNCreateDataModelNUTSTest {

	@Test
	public void testCreateTable() throws Exception{
		PPNCreateDataModelNUTS ppnCreateDataModel = new 
		PPNCreateDataModelNUTS("/home/chema/weso/google-repo/moldeas/trunk/apps/moldeas-api/src/test/resources/ppn/tender-nut-2013.csv");
		ppnCreateDataModel.execute();
		Map<StatsPairTO, Integer> stats = ppnCreateDataModel.getPair();
		Set<Entry<StatsPairTO, Integer>> entries = stats.entrySet();
//		for(Entry entry:entries){			
//			StatsPairTO pair = (StatsPairTO) entry.getKey();
//			System.out.println(pair);	
//		}
		Assert.assertEquals(16, entries.size());

	}
}
