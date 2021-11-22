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
import com.flashtract.billing.contracts.jpa.InvoiceStatus;
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

	@Autowired
	private BillingValidator validator;

	@GetMapping(value = "/list/{userId}/{contractId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> returnInvoicesForContract(@PathVariable Integer userId, @PathVariable Integer contractId) {

		if (!validator.validateUserById(userId)) {
			return BillingValidator.userNotFoundResponse(userId);
		}

		return ResponseEntity.ok(invoiceRepository.findInvoicesByContractAndAssignedUser(contractId, userId));
	}

	/**
	 * Creates an invoice if the user is a Vendor User type under a specified Contract. An error will be thrown if the contract is not assigned to the Vendor that is trying to create the Invoice.
	 * 
	 * @param contractId
	 * @param userId
	 * @param invoice
	 * @return
	 */
	@PostMapping(value = "/create/{userId}/{contractId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createInvoice(@PathVariable Integer userId, @PathVariable Integer contractId, @RequestBody InvoiceEntity invoice) {

		Optional<UserEntity> user = userRepository.findById(userId);
		if (user.isPresent()) {
			if (user.get().getType() != UserType.VENDOR_USER) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Vendor Users are able to create Invoices.");
			}
			invoice.setCreatedBy(user.get());
		} else {
			return BillingValidator.userNotFoundResponse(userId);
		}

		ContractEntity contract = contractRepository.findByIdAndAssignedTo(contractId, userId);
		if (contract == null) {
			// Could not find a contract for the information provided so stop the creation flow
			return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
					.body(String.format("Could not find a Contract with id {%s} and assigned to %s", contractId, user.get().getName()));
		}

		invoice.setContractId(contract.getId());
		invoice.setStatus(InvoiceStatus.APPROVED); // Auto-Approve the Invoice, pre-populated invoices are set to Submitted.

		InvoiceEntity saved = invoiceRepository.save(invoice);
		if (saved != null) {
			return ResponseEntity.ok("Successfully created Invoice.");
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("There was an error while creating the Invoice.");
		}
	}

	/**
	 * "Submit" the invoice by changing it's status to Submitted.
	 * 
	 * @param userId
	 * @param invoiceId
	 * @return
	 */
	@GetMapping(value = "/submit/{userId}/{invoiceId}")
	public ResponseEntity<?> submitInvoice(@PathVariable Integer userId, @PathVariable Integer invoiceId) {
		Optional<UserEntity> user = userRepository.findById(userId);
		if (user.isPresent()) {
			if (user.get().getType() != UserType.VENDOR_USER) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Vendor Users can submit their invoices.");
			}
		} else {
			return BillingValidator.userNotFoundResponse(userId);
		}

		InvoiceEntity invoice = invoiceRepository.findInvoiceByIdAndAssignedId(invoiceId, userId);
		if (invoice == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(String.format("No Invoice found with ID {%s} and assigned to user wth ID {%s}", invoiceId, userId));
		}

		invoice.setStatus(InvoiceStatus.SUBMITTED);
		invoiceRepository.save(invoice);

		return ResponseEntity.ok("Successully submitted Invoice.");
	}

}
