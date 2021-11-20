package com.flashtract.billing.contracts.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flashtract.billing.contracts.jpa.ContractRepository;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
import com.flashtract.billing.contracts.jpa.persistence.InvoiceEntity;

@RestController
public class BillingRestProvider {

	@Autowired
	private ContractRepository contractRepository;

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

}
