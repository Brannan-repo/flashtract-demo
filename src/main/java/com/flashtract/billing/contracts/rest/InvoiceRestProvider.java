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
import com.flashtract.billing.contracts.jpa.InvoiceRepository;
import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.UserType;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
import com.flashtract.billing.contracts.jpa.persistence.InvoiceEntity;
import com.flashtract.billing.contracts.jpa.persistence.UserEntity;

/**
 * All endpoints for creating/managing an Invoice
 *
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceRestProvider {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping(value = "/list/{contractId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<InvoiceEntity>> returnAllContractInvoices(@PathVariable Integer contractId, @PathVariable Integer userId) {

		Optional<UserEntity> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.ok(invoiceRepository.findInvoicesByContractAndAssignedUser(contractId, userId));
	}

	@PostMapping(value = "/create/{contractId}/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createInvoice(@PathVariable Integer contractId, @PathVariable Integer userId, @RequestBody InvoiceEntity invoice) {

		Optional<UserEntity> user = userRepository.findById(userId);
		if (user.isPresent()) {
			if (user.get().getType() != UserType.VENDOR_USER) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Vendor Users are able to create Contracts.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User with ID {%s} not found.", userId));
		}

		ContractEntity contract = contractRepository.findByIdAndAssignedTo(contractId, userId);
		if (contract == null) {
			// Could not find a contract for the information provided so stop the creation flow
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Could not find a Contract with id {%s} and assigned to %s", contractId, user.get().getName()));
		}

		return null;
	}

}
