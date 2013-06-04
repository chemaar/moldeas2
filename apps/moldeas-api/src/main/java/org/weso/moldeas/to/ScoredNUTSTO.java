package org.weso.moldeas.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "scoredNUTSTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scoredNUTSTO", propOrder = {
    "nutsTO",
    "score"
})
public class ScoredNUTSTO {
	NUTSTO nutsTO;
	double score;
	public NUTSTO getNutsTO() {
		return nutsTO;
	}
	public void setNutsTO(NUTSTO nutsTO) {
		this.nutsTO = nutsTO;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ScoredNUTSTO(NUTSTO nutsTO, double score) {
		super();
		this.nutsTO = nutsTO;
		this.score = score;
	}
	public ScoredNUTSTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ScoredNUTSTO [nutsTO=" + nutsTO + ", score=" + score + "]";
	}
	
}
