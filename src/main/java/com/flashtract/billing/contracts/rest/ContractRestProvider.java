package com.flashtract.billing.contracts.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.flashtract.billing.contracts.jpa.ContractRepository;
import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.UserType;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
import com.flashtract.billing.contracts.jpa.persistence.InvoiceEntity;
import com.flashtract.billing.contracts.jpa.persistence.UserEntity;

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
	 * Assumes the user has an authenticated session. Just passing the user ID will check for the user type as a small validation.
	 * 
	 * @param contract
	 */
	@PostMapping(value = "/create/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createContract(@PathVariable Integer userId, @RequestBody ContractEntity contract) {

		// Handle 'authorizing' the user submitting the request.
		// For this demo, the user has to pass in their own ID.
		Optional<UserEntity> user = userRepository.findById(userId);
		if (user.isPresent()) {
			// Check if the user has the right 'role' of Client User
			if (user.get().getType() != UserType.CLIENT_USER) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Client Users are able to create Contracts.");
			}
			contract.setCreatedBy(user.get());
		} else {
			return ResponseEntity.ok(String.format("User with ID {%s} not found.", userId));
		}

		// Validate the AssignedTo field.
		if (contract.getAssignedTo() == null || contract.getAssignedTo().getId() == null) {
			return ResponseEntity.ok("Assigned To must be set when creating a Contract.");
		}
		Optional<UserEntity> assignedtoUser = userRepository.findById(contract.getAssignedTo().getId());
		if (assignedtoUser.isPresent()) {
			// Make sure they are trying to assign the Contract to a Vendor User
			if (assignedtoUser.get().getType() != UserType.VENDOR_USER) {
				return ResponseEntity.ok(String.format("Assigned To user with ID {%s} is not a Vendor User type. Only Vendor User's can be assigned to a contract.", contract.getAssignedTo().getId()));
			}
			contract.setAssignedTo(assignedtoUser.get());
		} else {
			return ResponseEntity.ok(String.format("Assigned To user with ID {%s} not found.", contract.getAssignedTo().getId()));
		}

		ContractEntity saved = contractRepository.save(contract);
		return ResponseEntity.ok("Successfully Created Contract.");
	}

}
