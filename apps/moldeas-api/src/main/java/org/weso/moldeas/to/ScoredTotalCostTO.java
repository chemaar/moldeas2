package org.weso.moldeas.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "scoredTotalCostTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scoredTotalCostTO", propOrder = {
    "totalCostTO",
    "score"
})
public class ScoredTotalCostTO {
	TotalCostTO totalCostTO;
	double score;
	public TotalCostTO getTotalCostTO() {
		return totalCostTO;
	}
	public void setTotalCostTO(TotalCostTO totalCostTO) {
		this.totalCostTO = totalCostTO;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ScoredTotalCostTO(TotalCostTO totalCostTO, double score) {
		super();
		this.totalCostTO = totalCostTO;
		this.score = score;
	}
	public ScoredTotalCostTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ScoredTotalCostTO [totalCostTO=" + totalCostTO + ", score="
				+ score + "]";
	}
	
	
}
