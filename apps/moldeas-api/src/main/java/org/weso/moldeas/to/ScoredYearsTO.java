package org.weso.moldeas.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "scoredYearsTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scoredYearsTO", propOrder = {
    "yearsTO",
    "score"
})
public class ScoredYearsTO {
	YearsTO yearsTO;
	double score;
	public YearsTO getYearsTO() {
		return yearsTO;
	}
	public void setYearsTO(YearsTO yearsTO) {
		this.yearsTO = yearsTO;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ScoredYearsTO(YearsTO yearsTO, double score) {
		super();
		this.yearsTO = yearsTO;
		this.score = score;
	}
	public ScoredYearsTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ScoredYearsTO [yearsTO=" + yearsTO + ", score=" + score + "]";
	}
	
	
	
}
