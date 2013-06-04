package org.weso.moldeas.to;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "scoredLanguageTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scoredLanguageTO", propOrder = {
    "languageTO",
    "score"
})
public class ScoredLanguageTO {
	LanguageTO languageTO;
	double score;
	public LanguageTO getLanguageTO() {
		return languageTO;
	}
	public void setLanguageTO(LanguageTO languageTO) {
		this.languageTO = languageTO;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ScoredLanguageTO(LanguageTO languageTO, double score) {
		super();
		this.languageTO = languageTO;
		this.score = score;
	}
	public ScoredLanguageTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ScoredLanguageTO [languageTO=" + languageTO + ", score="
				+ score + "]";
	}
	
	
}
