package org.weso.moldeas.utils;

import java.util.ResourceBundle;

import org.weso.moldeas.to.NUTSTO;
import org.weso.moldeas.to.PSCTO;
import org.weso.moldeas.to.ScoredNUTSTO;
import org.weso.moldeas.to.ScoredPSCTO;
import org.weso.moldeas.to.ScoredYearsTO;
import org.weso.moldeas.to.YearsTO;
import org.weso.pscs.utils.PSCConstants;

public class PPNVariableFactory {
	protected static long maxYear;
	protected static long minYear;
	protected static long maxNUTSCode;
	protected static long minNUTSCode;
	protected static long minCPVCode;
	protected static long maxCPVCode;
	
	static{
		ResourceBundle range = ResourceBundle.getBundle(
				PPNVariableFactory.class.getName().toString());		
		maxYear = Long.valueOf(range.getString("maxYear"));
		minYear = Long.valueOf(range.getString("minYear"));
		maxNUTSCode = Long.valueOf(range.getString("maxNUTSCode"));
		minNUTSCode = Long.valueOf(range.getString("minNUTSCode"));
		minCPVCode = Long.valueOf(range.getString("minCPVCode"));
		maxCPVCode = Long.valueOf(range.getString("maxCPVCode"));
	}
	
	public static Object createScoreTO(long value, double score){
		if (value >=minYear && value <=maxYear){
			return new ScoredYearsTO(new YearsTO(value),score);
		}else if (value >=minNUTSCode && value<=maxNUTSCode){
			String id = NUTSCodesEncoder.getStringCode((int)value); 
			String uri = PSCConstants.formatNUTSTO(id);			
			return new ScoredNUTSTO(new NUTSTO(uri,id),score);
		}else if (value >= minCPVCode && value <= maxCPVCode){
			String id = CPVEncoderCodes.encode(String.valueOf(value));
			String uri = PSCConstants.formatId(id); 
			return new ScoredPSCTO(new PSCTO(id,uri),score);
		}
		return null;
	}
	
}
