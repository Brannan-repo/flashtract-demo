package com.flashtract.billing.contracts.jpa.persistence;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRACT")
public class ContractEntity {

	@Id
	@Column(name = "ID", nullable = false, unique = true)
	private Integer id;

	@OneToMany
	@JoinColumn(name = "CONTRACT_ID")
	private List<InvoiceEntity> invoices;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Column(name = "TERMS", nullable = false)
	private String terms;

	@Column(name = "TOTAL_AMOUNT", nullable = false)
	private Long totalAmount;

	@Column(name = "CREATED_DT", nullable = false)
	private ZonedDateTime createdDt;

	@OneToOne
	@JoinColumn(name = "ID")
	private UserEntity createdBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<InvoiceEntity> getInvoices() {
		// Eliminate the chance of a NullPointerException
		if (invoices == null) {
			invoices = new ArrayList<>();
		}
		return invoices;
	}

	public void setInvoices(List<InvoiceEntity> invoices) {
		this.invoices = invoices;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ZonedDateTime getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(ZonedDateTime createdDt) {
		this.createdDt = createdDt;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

}
