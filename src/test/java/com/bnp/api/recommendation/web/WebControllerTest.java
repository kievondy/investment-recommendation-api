package com.bnp.api.recommendation.web;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.bnp.api.recommendation.model.ClientPortfolio;
import com.bnp.api.recommendation.model.InvestmentRecommendation;
import com.bnp.api.recommendation.service.DataAccessService;

@RunWith(MockitoJUnitRunner.class)
public class WebControllerTest {

	WebController testSubject = new WebController();

	@Mock
	DataAccessService dataAccessService;

	@Before
	public void setUp() throws Exception {
		testSubject.dataAccessService = dataAccessService;
	}

	@Test
	public void testFindAllClients_whenEmpty() {

		List<ClientPortfolio> mockedClients = new ArrayList<ClientPortfolio>();

		when(dataAccessService.findAllClients()).thenReturn(mockedClients);

		List<ClientPortfolio> foundClients = testSubject.findAllClients();

		assertThat(foundClients, Matchers.empty());
	}

	@Test
	public void testFindAllClients_whenTypical() {

		InvestmentRecommendation ir1 = new InvestmentRecommendation(1, "arbitraryInvestment1", 1, 2);
		InvestmentRecommendation ir2 = new InvestmentRecommendation(2, "arbitraryInvestment1", 1, 3);
		InvestmentRecommendation ir3 = new InvestmentRecommendation(3, "arbitraryInvestment1", 2, 2);
		InvestmentRecommendation ir4 = new InvestmentRecommendation(4, "arbitraryInvestment1", 2, 4);

		List<ClientPortfolio> mockedClients = new ArrayList<ClientPortfolio>();
		ClientPortfolio cp1 = new ClientPortfolio(1, "arbitraryClient1", 1);
		cp1.setInvestmentRecommendations(Arrays.asList(ir1, ir2));
		ClientPortfolio cp2 = new ClientPortfolio(2, "arbitraryClient2", 2);
		cp2.setInvestmentRecommendations(Arrays.asList(ir3, ir4));
		ClientPortfolio cp3 = new ClientPortfolio(3, "arbitraryClient3", 3);
		mockedClients.add(cp1);
		mockedClients.add(cp2);
		mockedClients.add(cp3);

		when(dataAccessService.findAllClients()).thenReturn(mockedClients);

		List<ClientPortfolio> foundClients = testSubject.findAllClients();

		assertThat(foundClients, Matchers.hasSize(3));
		assertThat(foundClients, contains(cp1, cp2, cp3));
		assertThat(foundClients.get(0).getInvestmentRecommendations(), contains(ir1, ir2));
		assertThat(foundClients.get(1).getInvestmentRecommendations(), contains(ir3, ir4));
		assertThat(foundClients.get(2).getInvestmentRecommendations(), Matchers.nullValue());
	}

	@Test
	public void testFindClient_whenTypical() {

		int arbitraryId = 1;
		String arbitraryClientName = "arbitraryClientName";
		int arbitraryRiskScore = 1;
		ClientPortfolio mockedClient = new ClientPortfolio(arbitraryId, arbitraryClientName, arbitraryRiskScore);

		when(dataAccessService.findClientById(arbitraryId)).thenReturn(mockedClient);

		ClientPortfolio found = testSubject.findClient(arbitraryId);

		assertThat(found, Matchers.equalTo(mockedClient));
	}

}
