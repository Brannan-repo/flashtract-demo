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

import com.flashtract.billing.contracts.jpa.ContractRepository;
import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.UserType;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
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

	/**
	 * Return all Contracts assigned to a specific User.
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ContractEntity>> returnAllContract(@PathVariable Integer userId) {

		Optional<UserEntity> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		// Return all contracts by userId since there are only 2 user types right now.
		// If the user type is Client User then they should be able to find all contracts.
		// If the user type is Vendor User the it is assumed they are already authenticated and the
		// system will submit this request with userId equal only to the authenticated Vendors ID.

		return ResponseEntity.ok(contractRepository.findAllAssignedTo(userId));
	}

	@GetMapping(value = "/list/{userId}/{contractId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContractEntity> returnContractById(@PathVariable Integer userId, @PathVariable Integer contractId) {

		Optional<UserEntity> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		// Return all contracts by userId since there are only 2 user types right now.
		// If the user type is Client User then they should be able to find all contracts.
		// If the user type is Vendor User the it is assumed they are already authenticated and the
		// system will submit this request with userId equal only to the authenticated Vendors ID.

		return ResponseEntity.ok(contractRepository.findByIdAndAssignedTo(contractId, userId));
	}

	/**
	 * Assumes the user has an authenticated session. Just passing the user ID will check for the user type as a small validation.
	 * 
	 * @param userId   The ID of the user requesting to create a Contract.
	 * @param contract The ContractEntity containing Description, Terms, and the Assigned To user's ID.
	 * @return ResposnEntity: 200 for a successful creation, otherwise the response will contain an error.
	 */
	@PostMapping(value = "/create/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createContract(@PathVariable Integer userId, @RequestBody ContractEntity contract) {

		// Simple validation to start
		if (contract.getDescription() == null || contract.getDescription().isBlank()) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Contract Description is required.");
		}
		if (contract.getTerms() == null || contract.getTerms().isBlank()) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Contract Terms are required.");
		}

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
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User with ID {%s} not found.", userId));
		}

		// Validate the AssignedTo field.
		if (contract.getAssignedTo() == null || contract.getAssignedTo().getId() == null) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body("Assigned To must be set when creating a Contract.");
		}
		Optional<UserEntity> assignedtoUser = userRepository.findById(contract.getAssignedTo().getId());
		if (assignedtoUser.isPresent()) {
			// Make sure they are trying to assign the Contract to a Vendor User
			if (assignedtoUser.get().getType() != UserType.VENDOR_USER) {
				return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(String.format(
						"Assigned To user with ID {%s} is not a Vendor User type. Only Vendor User's can be assigned to a contract.", contract.getAssignedTo().getId()));
			}
			contract.setAssignedTo(assignedtoUser.get());
		} else {
			return ResponseEntity.ok(String.format("Assigned To user with ID {%s} not found.", contract.getAssignedTo().getId()));
		}

		ContractEntity saved = contractRepository.save(contract);
		if (saved != null) {
			return ResponseEntity.ok("Successfully created Contract.");
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("There was an error while creating the Contract.");
		}
	}

}
