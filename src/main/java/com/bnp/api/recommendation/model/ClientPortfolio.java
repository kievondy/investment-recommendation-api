package com.bnp.api.recommendation.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClientPortfolio implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String clientName;

	private int riskScore;

	private List<InvestmentRecommendation> investmentRecommendations;

	public ClientPortfolio() {
	}

	public ClientPortfolio(int id, String clientName, int riskScore) {
		this();
		this.id = id;
		this.clientName = clientName;
		this.riskScore = riskScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(int riskScore) {
		this.riskScore = riskScore;
	}

	public List<InvestmentRecommendation> getInvestmentRecommendations() {
		return investmentRecommendations;
	}

	public void setInvestmentRecommendations(List<InvestmentRecommendation> investmentRecommendations) {
		this.investmentRecommendations = investmentRecommendations;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("clientName", clientName)
				.append("riskScore", riskScore)
				.append("investmentRecommendations", investmentRecommendations)
				.toString();
	}

}
