package com.flashtract.billing.contracts.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.flashtract.billing.contracts.jpa.persistence.MaterialEntity;

public interface MaterialRepository extends CrudRepository<MaterialEntity, Integer> {

	public List<MaterialEntity> findAll();

	@Query(value = "select * from material where invoice_id = ?1", nativeQuery = true)
	public List<MaterialEntity> findAllInvoiceMaterials(int invoiceId);

}
