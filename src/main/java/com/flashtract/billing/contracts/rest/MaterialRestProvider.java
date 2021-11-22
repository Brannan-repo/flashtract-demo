package com.flashtract.billing.contracts.rest;

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
import com.flashtract.billing.contracts.jpa.MaterialRepository;
import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.UserType;
import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;
import com.flashtract.billing.contracts.jpa.persistence.InvoiceEntity;
import com.flashtract.billing.contracts.jpa.persistence.MaterialEntity;
import com.flashtract.billing.contracts.jpa.persistence.UserEntity;

/**
 * All endpoints for creating/managing Invoice Materials.
 *
 */
@RestController
@RequestMapping("/material")
public class MaterialRestProvider {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BillingValidator validator;

	@GetMapping(value = "/list/{userId}/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> returnInvoicesForContract(@PathVariable Integer userId, @PathVariable Integer invoiceId) {

		if (!validator.validateUserById(userId)) {
			return BillingValidator.userNotFoundResponse(userId);
		}

		return ResponseEntity.ok(materialRepository.findAllInvoiceMaterials(invoiceId));
	}

	@PostMapping(value = "/create/{userId}/{invoiceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createInvoice(@PathVariable Integer userId, @PathVariable Integer invoiceId, @RequestBody MaterialEntity material) {

		Optional<UserEntity> user = userRepository.findById(userId);
		if (user.isPresent()) {
			if (user.get().getType() != UserType.VENDOR_USER) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Vendor Users are able to add materials to invoices.");
			}
		} else {
			return BillingValidator.userNotFoundResponse(userId);
		}

		Optional<InvoiceEntity> invoice = invoiceRepository.findById(invoiceId);
		if (!invoice.isPresent()) {
			// Could not find a contract for the information provided so stop the creation flow
			return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(String.format("Could not find a Invoice with id {%s}.", invoiceId, user.get().getName()));
		}

		// Check if the user trying to add the material is assigned to the Contract.
		Optional<ContractEntity> contract = contractRepository.findById(invoice.get().getContractId());
		if (contract.isPresent() && contract.get().getAssignedTo().getId() != userId) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user attempting to add a Material to the Invoice is not assigned to the Contract.");
		}

		material.setInvoiceId(invoice.get().getId());
		MaterialEntity saved = materialRepository.save(material);
		if (saved != null) {
			return ResponseEntity.ok("Successfully added Invoice Material.");
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("There was an error while creating the Invoice Material.");
		}
	}

}
