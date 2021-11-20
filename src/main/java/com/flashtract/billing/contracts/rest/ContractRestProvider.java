package com.flashtract.billing.contracts.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flashtract.billing.contracts.jpa.ContractRepository;
import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
import com.flashtract.billing.contracts.jpa.persistence.InvoiceEntity;

/**
 * All endpoints for creating/managing a Contract
 *
 */
@RestController
@RequestMapping("/contract")
public class ContractRestProvider {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public String returnTest() {
		return "Working";
	}

	@SuppressWarnings("unused")
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ContractEntity> returnAllContract() {
		List<ContractEntity> allContracts = contractRepository.findAll();
		ContractEntity ci = allContracts.get(0);
		if (ci != null && ci.getInvoices() != null) {
			InvoiceEntity i = ci.getInvoices().get(0);
			System.out.println();
		}
		return contractRepository.findAll();
	}

	/**
	 * Assumes the user has an authenticated session. Just passing the user ID will
	 * check for the user type as a small validation.
	 * 
	 * @param contract
	 */
	@PostMapping(value = "/create/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean createContract(@PathVariable Integer id, @RequestBody ContractEntity contract) {
		System.out.println();
		return true;
	}

}
