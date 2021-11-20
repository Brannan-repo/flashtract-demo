package com.flashtract.billing.contracts.jpa;

public enum InvoiceStatus {

	IN_PROGRESS("In Progress"), SUBMITTED("Submitted"), APPROVED("Approved");

	private String status;

	InvoiceStatus(String status) {
		this.status = status;
	}

	public String value() {
		return this.status;
	}

	public static InvoiceStatus fromValue(String value) {
		return valueOf(value);
	}
}
