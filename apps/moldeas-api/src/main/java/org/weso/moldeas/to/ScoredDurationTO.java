package org.weso.moldeas.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "scoredDurationTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scoredDurationTO", propOrder = {
    "durationTO",
    "score"
})
public class ScoredDurationTO {
	DurationTO durationTO;
	double score;
	public DurationTO getDurationTO() {
		return durationTO;
	}
	public void setDurationTO(DurationTO durationTO) {
		this.durationTO = durationTO;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ScoredDurationTO(DurationTO durationTO, double score) {
		super();
		this.durationTO = durationTO;
		this.score = score;
	}
	public ScoredDurationTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ScoredDurationTO [durationTO=" + durationTO + ", score="
				+ score + "]";
	}
	
	
}
