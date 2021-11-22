package com.flashtract.billing.contracts.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.flashtract.billing.contracts.jpa.UserRepository;
import com.flashtract.billing.contracts.jpa.persistence.UserEntity;

@Component
public class BillingValidator {

	@Autowired
	private UserRepository userRepository;

	public boolean validateUserById(int userId) {
		Optional<UserEntity> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			return false;
		}
		return true;
	}

	public static ResponseEntity<?> userNotFoundResponse(int userId) {
		return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(String.format("User with ID {%s} was not found.", userId));
	}

	public static String contractByUserNotFound(int userId, int contractId) {
		return String.format("No contracts found with ID {%s} that are assigned to user with ID {%s}", contractId, userId);
	}
}