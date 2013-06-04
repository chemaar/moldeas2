package org.weso.moldeas.stats;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.weso.moldeas.to.StatsPairTO;

public class PPNCreateDataModelYearsMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String year = "2008";
		PPNCreateDataModelYears ppnCreateDataModel = new PPNCreateDataModelYears(year);
		ppnCreateDataModel.execute();
		Map<StatsPairTO, Integer> stats = ppnCreateDataModel.getPair();
		Set<Entry<StatsPairTO, Integer>> entries = stats.entrySet();
		PrintWriter pw = new PrintWriter(new File("/home/chema/tesis/test/generated/stats_2_3_1_"+year+".mht"));
		for(Entry entry:entries){
			StatsPairTO pair = (StatsPairTO) entry.getKey();
			Integer hits = (Integer) entry.getValue();
			pw.println(pair.getUserId()+","+pair.getItemId()+","+hits);
		}
		pw.close();
	}

}
