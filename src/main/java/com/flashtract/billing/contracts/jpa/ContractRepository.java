package com.flashtract.billing.contracts.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flashtract.billing.contracts.jpa.persistence.ContractEntity;

public interface ContractRepository extends CrudRepository<ContractEntity, Long> {

	public List<ContractEntity> findAll();

}
