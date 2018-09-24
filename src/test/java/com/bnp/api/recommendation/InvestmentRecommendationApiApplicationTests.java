package com.bnp.api.recommendation;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.bnp.api.recommendation.model.ClientPortfolio;
import com.bnp.api.recommendation.service.DataAccessService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvestmentRecommendationApiApplicationTests {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	DataAccessService dataServiceTest;

	@Test
	public void testApis() {

		// REST call for /api/clients
		ResponseEntity<List> responseAllClients = testRestTemplate.getForEntity("/api/clients", List.class, new HashMap<>());
		List allClientsFromRestCall = responseAllClients.getBody();

		// DB call for the corresponding REST call /api/clients
		List<ClientPortfolio> clientsFromDb = dataServiceTest.findAllClients();

		// assert that REST call is successful
		assertThat(responseAllClients.getStatusCode(), Matchers.equalTo(HttpStatus.OK));

		// assert REST v DB
		assertThat(allClientsFromRestCall.size(), Matchers.equalTo(clientsFromDb.size()));

		if (allClientsFromRestCall.size() > 0) {

			Map item = (Map) allClientsFromRestCall.get(0);
			Object id = item.get("id");

			// REST call for /api/client/{id}
			ResponseEntity<ClientPortfolio> responseClientPortfolio = testRestTemplate.getForEntity("/api/client/" + id, ClientPortfolio.class,
					new HashMap<>());
			ClientPortfolio clientFromRestCall = responseClientPortfolio.getBody();

			// DB call for the corresponding REST call /api/client/{id}
			ClientPortfolio clientFromDb = dataServiceTest.findClientById((int) id);

			// assert that REST call is successful
			assertThat(responseClientPortfolio.getStatusCode(), Matchers.equalTo(HttpStatus.OK));

			// assert REST v DB
			assertThat(clientFromRestCall.getClientName(), Matchers.equalTo(clientFromDb.getClientName()));
			assertThat(clientFromRestCall.getInvestmentRecommendations().size(), Matchers.equalTo(clientFromDb.getInvestmentRecommendations().size()));
		}
	}

}
