package com.bnp.api.recommendation.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnp.api.recommendation.model.ClientPortfolio;
import com.bnp.api.recommendation.service.DataAccessService;

@RestController
@RequestMapping("/api")
public class WebController {

	@Autowired
	DataAccessService dataAccessService;

	@GetMapping("/clients")
	public List<ClientPortfolio> findAllClients() {
		return dataAccessService.findAllClients();
	}

	@GetMapping(path = "/client/{id}")
	public ClientPortfolio findClient(@PathVariable(name = "id") int id) {
		return dataAccessService.findClientById(id);
	}
}
