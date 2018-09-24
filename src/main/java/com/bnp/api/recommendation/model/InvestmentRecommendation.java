package com.bnp.api.recommendation.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class InvestmentRecommendation implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String investmentName;

	private int riskScore;

	private int investmentReturnScore;

	public InvestmentRecommendation() {
	}

	public InvestmentRecommendation(int id, String investmentName, int riskScore, int investmentReturnScore) {
		this();
		this.id = id;
		this.investmentName = investmentName;
		this.riskScore = riskScore;
		this.investmentReturnScore = investmentReturnScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInvestmentName() {
		return investmentName;
	}

	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}

	public int getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(int riskScore) {
		this.riskScore = riskScore;
	}

	public int getInvestmentReturnScore() {
		return investmentReturnScore;
	}

	public void setInvestmentReturnScore(int investmentReturnScore) {
		this.investmentReturnScore = investmentReturnScore;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("investmentName", investmentName)
				.append("riskScore", riskScore)
				.append("investmentReturnScore", investmentReturnScore)
				.toString();
	}

}
