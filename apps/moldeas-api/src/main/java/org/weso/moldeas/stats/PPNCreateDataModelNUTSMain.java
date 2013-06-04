package org.weso.moldeas.stats;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.weso.moldeas.to.StatsPairTO;

public class PPNCreateDataModelNUTSMain {

	public static final String TESIS_TEST_GENERATED = "/home/chema/tesis/test/generated";
	public static final String TESIS_TEST_SOURCES = "/home/chema/tesis/test/sources";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String year = "2008";
		PPNCreateDataModelNUTS ppnCreateDataModel = new PPNCreateDataModelNUTS(
				TESIS_TEST_SOURCES+"/tender-nut-"+year+".csv");
		ppnCreateDataModel.execute();
		Map<StatsPairTO, Integer> stats = ppnCreateDataModel.getPair();
		Set<Entry<StatsPairTO, Integer>> entries = stats.entrySet();
		PrintWriter pw = new PrintWriter(new File("" +
				TESIS_TEST_GENERATED+"/stats_2_2_2_2_"+year+".mht"));
		for(Entry entry:entries){			
			StatsPairTO pair = (StatsPairTO) entry.getKey();
			Integer hits = (Integer) entry.getValue();
			pw.println(pair.getUserId()+","+pair.getItemId()+","+hits);
		}
		pw.close();
	}

}
