package com.bnp.api.recommendation.service;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bnp.api.recommendation.model.ClientPortfolio;
import com.bnp.api.recommendation.model.InvestmentRecommendation;

@RunWith(MockitoJUnitRunner.class)
public class DataAccessServiceTest {

	DataAccessService testSubject = new DataAccessService();

	@Mock
	JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {
		testSubject.jdbcTemplate = jdbcTemplate;
	}

	@Test
	public void testFindAllClients_whenNoClient() {

		List<ClientPortfolio> mockedClients = new ArrayList<ClientPortfolio>();
		when(jdbcTemplate.query(Mockito.eq(DataAccessService.SQL_FIND_ALL_CLIENTS), Mockito.any(RowMapper.class))).thenReturn(mockedClients);

		List<ClientPortfolio> foundClients = testSubject.findAllClients();

		assertThat(foundClients, Matchers.empty());
	}

	@Test
	public void testFindAllClients_whenNoRecommendationData() {

		List<ClientPortfolio> mockedClients = new ArrayList<ClientPortfolio>();
		ClientPortfolio cp1 = new ClientPortfolio(1, "arbitraryClient1", 1);
		mockedClients.add(cp1);

		when(jdbcTemplate.query(Mockito.eq(DataAccessService.SQL_FIND_ALL_CLIENTS), Mockito.any(RowMapper.class))).thenReturn(mockedClients);

		List<InvestmentRecommendation> mockedRecommendations = new ArrayList<InvestmentRecommendation>();

		when(jdbcTemplate.query(Mockito.eq(DataAccessService.SQL_FIND_ALL_RECOMMENDATIONS), Mockito.any(RowMapper.class))).thenReturn(mockedRecommendations);

		List<ClientPortfolio> foundClients = testSubject.findAllClients();

		assertThat(foundClients, contains(cp1));
		assertThat(foundClients.get(0).getInvestmentRecommendations(), Matchers.nullValue());
	}

	@Test
	public void testFindAllClients_whenTypical() {

		List<ClientPortfolio> mockedClients = new ArrayList<ClientPortfolio>();
		ClientPortfolio cp1 = new ClientPortfolio(1, "arbitraryClient1", 1);
		ClientPortfolio cp2 = new ClientPortfolio(2, "arbitraryClient2", 2);
		ClientPortfolio cp3 = new ClientPortfolio(3, "arbitraryClient3", 3);
		mockedClients.add(cp1);
		mockedClients.add(cp2);
		mockedClients.add(cp3);

		when(jdbcTemplate.query(Mockito.eq(DataAccessService.SQL_FIND_ALL_CLIENTS), Mockito.any(RowMapper.class))).thenReturn(mockedClients);

		List<InvestmentRecommendation> mockedRecommendations = new ArrayList<InvestmentRecommendation>();
		InvestmentRecommendation ir1 = new InvestmentRecommendation(1, "arbitraryInvestment1", 1, 2);
		InvestmentRecommendation ir2 = new InvestmentRecommendation(2, "arbitraryInvestment1", 1, 3);
		InvestmentRecommendation ir3 = new InvestmentRecommendation(3, "arbitraryInvestment1", 2, 2);
		InvestmentRecommendation ir4 = new InvestmentRecommendation(4, "arbitraryInvestment1", 2, 4);
		mockedRecommendations.add(ir1);
		mockedRecommendations.add(ir2);
		mockedRecommendations.add(ir3);
		mockedRecommendations.add(ir4);

		when(jdbcTemplate.query(Mockito.eq(DataAccessService.SQL_FIND_ALL_RECOMMENDATIONS), Mockito.any(RowMapper.class))).thenReturn(mockedRecommendations);

		List<ClientPortfolio> foundClients = testSubject.findAllClients();

		assertThat(foundClients, Matchers.hasSize(3));
		assertThat(foundClients, contains(cp1, cp2, cp3));
		assertThat(foundClients.get(0).getInvestmentRecommendations(), contains(ir1, ir2));
		assertThat(foundClients.get(1).getInvestmentRecommendations(), contains(ir3, ir4));
		assertThat(foundClients.get(2).getInvestmentRecommendations(), Matchers.emptyIterable());
	}

	@Test
	public void testFindClientById_whenClientNotFound() {

		int arbitraryId = 1;

		when(jdbcTemplate.queryForObject(Mockito.eq(DataAccessService.SQL_FIND_CLIENT_BY_ID), Mockito.any(Object[].class), Mockito.any(int[].class),
				Mockito.any(RowMapper.class))).thenThrow(EmptyResultDataAccessException.class);

		ClientPortfolio found = testSubject.findClientById(arbitraryId);

		assertThat(found.getId(), Matchers.equalTo(-1));
		assertThat(found.getClientName(), Matchers.equalTo(DataAccessService.CLIENT_NAME_CLIENT_NOT_FOUND));
	}

	@Test
	public void testFindClientById_whenTypical() {

		int arbitraryId = 1;
		String clientName = "arbitraryClientName";
		int riskScore = 1;
		ClientPortfolio clientPortfolio = new ClientPortfolio(arbitraryId, clientName, riskScore);

		when(jdbcTemplate.queryForObject(Mockito.eq(DataAccessService.SQL_FIND_CLIENT_BY_ID), Mockito.any(Object[].class), Mockito.any(int[].class),
				Mockito.any(RowMapper.class))).thenReturn(clientPortfolio);

		List<InvestmentRecommendation> recommendations = new ArrayList<InvestmentRecommendation>();
		InvestmentRecommendation ir1 = new InvestmentRecommendation(1, "arbitraryInvestment1", 1, 2);
		InvestmentRecommendation ir2 = new InvestmentRecommendation(2, "arbitraryInvestment1", 1, 3);
		recommendations.add(ir1);
		recommendations.add(ir2);

		when(jdbcTemplate.query(Mockito.eq(DataAccessService.SQL_FIND_RECOMMENDATIONS_BY_RISK_SCORE), Mockito.any(Object[].class), Mockito.any(int[].class),
				Mockito.any(RowMapper.class))).thenReturn(recommendations);

		ClientPortfolio found = testSubject.findClientById(arbitraryId);

		assertThat(found, Matchers.equalTo(clientPortfolio));
		assertThat(found.getInvestmentRecommendations(), contains(ir1, ir2));
	}

}
