package com.flashtract.billing.contracts.jpa.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table(name = "CONTRACT")
public class ContractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	private Integer id;

	@OneToMany
	@JoinColumn(name = "CONTRACT_ID")
	private List<InvoiceEntity> invoices;

	@NonNull
	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@NonNull
	@Column(name = "TERMS", nullable = false)
	private String terms;

	@NonNull
	@Column(name = "TOTAL_AMOUNT")
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@NonNull
	@Column(name = "CREATED_DT")
	private LocalDateTime createdDt = LocalDateTime.now();

	@OneToOne
	@JoinColumn(name = "CREATED_BY")
	private UserEntity createdBy;

	@OneToOne
	@JoinColumn(name = "ASSIGNED_TO")
	private UserEntity assignedTo;

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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(LocalDateTime createdDt) {
		this.createdDt = createdDt;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public UserEntity getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(UserEntity assignedTo) {
		this.assignedTo = assignedTo;
	}

}
