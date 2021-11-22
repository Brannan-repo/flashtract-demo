package com.flashtract.billing.contracts.jpa;

public enum InvoiceStatus {

	IN_PROGRESS("In Progress"), SUBMITTED("Submitted"), APPROVED("Approved"), DECLINED("Declined");

	private String status;

	InvoiceStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return this.status;
	}

	public static InvoiceStatus fromValue(String value) {
		return valueOf(value);
	}
}
