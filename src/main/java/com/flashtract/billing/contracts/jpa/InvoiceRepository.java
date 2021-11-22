package com.flashtract.billing.contracts.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.flashtract.billing.contracts.jpa.persistence.InvoiceEntity;

public interface InvoiceRepository extends CrudRepository<InvoiceEntity, Integer> {

	public List<InvoiceEntity> findAll();

	@Query(value = "select i.* from invoice i, contract c where i.contract_id = ?1 and c.assigned_to = ?2", nativeQuery = true)
	public List<InvoiceEntity> findInvoicesByContractAndAssignedUser(int contractId, int assignedUserId);

	@Query(value = "select i.* from invoice i, contract c where i.id = ?1 and i.contract_id = c.id and c.assigned_to = ?2", nativeQuery = true)
	public InvoiceEntity findInvoiceByIdAndAssignedId(int invoiceId, int assignedUserId);

}
