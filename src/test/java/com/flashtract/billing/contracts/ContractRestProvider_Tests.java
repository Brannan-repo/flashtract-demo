package com.flashtract.billing.contracts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flashtract.billing.contracts.jpa.ContractRepository;
import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
import com.flashtract.billing.contracts.jpa.persistence.UserEntity;
import com.flashtract.billing.contracts.rest.BillingValidator;
import com.flashtract.billing.contracts.rest.ContractRestProvider;

@SuppressWarnings("unchecked")
@SpringBootTest
public class ContractRestProvider_Tests {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContractRestProvider contractRestProvider;

	@BeforeEach
	public void setup() {
	}

	@Test
	public void returnAllContracts_UserNotFound_Test() {
		int userId = 10;
		ResponseEntity<?> allContracts = contractRestProvider.returnAllContracts(userId);
		assertThat(allContracts, is(BillingValidator.userNotFoundResponse(userId)));
	}

	@Test
	public void returnAllContracts_Empty_Test() {
		ResponseEntity<?> allContracts = contractRestProvider.returnAllContracts(1);
		assertThat(allContracts.getStatusCode().value(), is(200));
		List<ContractEntity> contracts = (List<ContractEntity>) allContracts.getBody();
		assertTrue(contracts.isEmpty());
	}

	@Test
	public void returnAllContracts_NotEmpty_Test() {
		ResponseEntity<?> allContracts = contractRestProvider.returnAllContracts(2);
		assertThat(allContracts.getStatusCode().value(), is(200));
		List<ContractEntity> contracts = (List<ContractEntity>) allContracts.getBody();
		assertTrue(contracts.size() == 1);
	}

	@Test
	public void returnContractByIdAndUser_NotEmpty_Test() {
		ResponseEntity<?> contract = contractRestProvider.returnContractById(2, 1);
		assertThat(contract.getStatusCode().value(), is(200));
		assertNotNull(contract);
	}

	@Test
	public void returnContractByIdAndUser_NotEmptyButNoContractFound_Test() {
		int contractId = 10;
		int userId = 2;
		ResponseEntity<?> contract = contractRestProvider.returnContractById(userId, contractId);
		assertThat(contract.getStatusCode(), is(HttpStatus.NOT_FOUND));
		assertNotNull(contract);
		assertThat(contract.getBody(), is(BillingValidator.contractByUserNotFound(userId, contractId)));
	}

	@Test
	public void createContractMissingFields_Test() {
		ContractEntity contract = new ContractEntity();

		ResponseEntity<?> response = contractRestProvider.createContract(2, contract);
		assertThat(response.getStatusCode(), is(HttpStatus.PRECONDITION_REQUIRED));
		assertThat(response.getBody(), is("Contract Description is required."));

		contract.setDescription("Test contract description");

		response = contractRestProvider.createContract(2, contract);
		assertThat(response.getStatusCode(), is(HttpStatus.PRECONDITION_REQUIRED));
		assertThat(response.getBody(), is("Contract Terms are required."));

		contract.setTerms("Test contract terms");

		response = contractRestProvider.createContract(2, contract);
		assertThat(response.getStatusCode(), is(HttpStatus.PRECONDITION_REQUIRED));
		assertThat(response.getBody(), is("Assigned To must be set when creating a Contract."));
	}

	@Test
	public void createContractWrongUserType_Test() {
		ContractEntity contract = new ContractEntity();
		contract.setDescription("Test contract description");
		contract.setTerms("Test contract terms");
		contract.setAssignedTo(new UserEntity(1));

		ResponseEntity<?> response = contractRestProvider.createContract(1, contract);
		assertThat(response.getStatusCode(), is(HttpStatus.PRECONDITION_FAILED));
		assertThat(response.getBody(), is("Assigned To user with ID {1} is not a Vendor User type. Only Vendor User's can be assigned to a contract."));

		contract.setAssignedTo(new UserEntity(2));
		response = contractRestProvider.createContract(2, contract);
		assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
		assertThat(response.getBody(), is("Only Client Users are able to create Contracts."));

	}

}