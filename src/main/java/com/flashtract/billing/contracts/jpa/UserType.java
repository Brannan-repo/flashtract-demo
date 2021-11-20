package com.flashtract.billing.contracts.jpa;

public enum UserType {

	CLIENT_USER, VENDOR_USER;

	public String value() {
		return name();
	}

	public static UserType fromValue(String value) {
		return valueOf(value);
	}
}
