package com.flashtract.billing.contracts.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flashtract.billing.contracts.jpa.persistence.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	public List<UserEntity> findAll();

}
