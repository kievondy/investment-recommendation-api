package com.bnp.api.recommendation.service;

import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bnp.api.recommendation.model.ClientPortfolio;
import com.bnp.api.recommendation.model.InvestmentRecommendation;

@Service
public class DataAccessService {

	private static final Logger logger = Logger.getLogger(DataAccessService.class);

	static final String SQL_FIND_ALL_CLIENTS = "SELECT ID, CLIENT_NAME, RISK_SCORE FROM CLIENT_PORTFOLIO";

	static final String SQL_FIND_ALL_RECOMMENDATIONS = "SELECT ID, INVESTMENT_NAME, RISK_SCORE, INVESTMENT_RETURN_SCORE FROM INVESTMENT_RECOMMENDATION";

	static final String SQL_FIND_CLIENT_BY_ID = "SELECT ID, CLIENT_NAME, RISK_SCORE FROM CLIENT_PORTFOLIO WHERE ID=?";

	static final String SQL_FIND_RECOMMENDATIONS_BY_RISK_SCORE = "SELECT ID, INVESTMENT_NAME, RISK_SCORE, INVESTMENT_RETURN_SCORE FROM INVESTMENT_RECOMMENDATION WHERE RISK_SCORE = ?";

	static final String CLIENT_NAME_CLIENT_NOT_FOUND = "[CLIENT NOT FOUND]";

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<ClientPortfolio> findAllClients() {

		List<ClientPortfolio> allClients = jdbcTemplate.query(SQL_FIND_ALL_CLIENTS, (resultSet, rowNum) -> {
			return new ClientPortfolio(resultSet.getInt("ID"), resultSet.getString("CLIENT_NAME"), resultSet.getInt("RISK_SCORE"));
		});

		if (!CollectionUtils.isEmpty(allClients)) {
			List<InvestmentRecommendation> allRecommendations = findAllRecommendations();

			if (!allRecommendations.isEmpty()) {
				allClients.stream().forEach(c -> {
					c.setInvestmentRecommendations(allRecommendations.stream().filter(r -> r.getRiskScore() == c.getRiskScore()).collect(Collectors.toList()));
				});
			}
		}

		return allClients;

	}

	private List<InvestmentRecommendation> findAllRecommendations() {

		return jdbcTemplate.query(SQL_FIND_ALL_RECOMMENDATIONS, (resultSet, rowNum) -> {
			return new InvestmentRecommendation(resultSet.getInt("ID"),
					resultSet.getString("INVESTMENT_NAME"),
					resultSet.getInt("RISK_SCORE"),
					resultSet.getInt("INVESTMENT_RETURN_SCORE"));
		});
	}

	public ClientPortfolio findClientById(int id) {

		try {
			logger.debug(String.format("Running SQL query '%s' with argument '%s'", SQL_FIND_CLIENT_BY_ID, id));

			ClientPortfolio clientPortfolio = jdbcTemplate.queryForObject(SQL_FIND_CLIENT_BY_ID, new Object[] { id }, new int[] { Types.INTEGER },
					(resultSet, rowNum) -> {
						return new ClientPortfolio(resultSet.getInt("ID"), resultSet.getString("CLIENT_NAME"), resultSet.getInt("RISK_SCORE"));
					});

			clientPortfolio.setInvestmentRecommendations(findRecommendationsByRiskScore(clientPortfolio.getRiskScore()));

			logger.debug(String.format("Query returned: %s", clientPortfolio));

			return clientPortfolio;

		} catch (DataAccessException e) {
			logger.info(String.format("Client not found: %s", e.getMessage()), e);
			return clientPortfolioNotFound();
		}
	}

	private ClientPortfolio clientPortfolioNotFound() {
		return new ClientPortfolio(-1, CLIENT_NAME_CLIENT_NOT_FOUND, -1);
	}

	private List<InvestmentRecommendation> findRecommendationsByRiskScore(int riskScore) {

		return jdbcTemplate.query(SQL_FIND_RECOMMENDATIONS_BY_RISK_SCORE, new Object[] { riskScore }, new int[] { Types.INTEGER }, (resultSet, rowNum) -> {
			return new InvestmentRecommendation(resultSet.getInt("ID"),
					resultSet.getString("INVESTMENT_NAME"),
					resultSet.getInt("RISK_SCORE"),
					resultSet.getInt("INVESTMENT_RETURN_SCORE"));
		});
	}

}
