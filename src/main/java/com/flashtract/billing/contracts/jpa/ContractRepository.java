package com.flashtract.billing.contracts.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;

public interface ContractRepository extends CrudRepository<ContractEntity, Integer> {

	public List<ContractEntity> findAll();

	@Query(value = "select * from contract where assigned_to = ?1", nativeQuery = true)
	public List<ContractEntity> findAllAssignedTo(int assignToId);

	@Query(value = "select * from contract where id = ?1 and assigned_to = ?2", nativeQuery = true)
	public ContractEntity findByIdAndAssignedTo(int contractId, int assignedTo);

}
