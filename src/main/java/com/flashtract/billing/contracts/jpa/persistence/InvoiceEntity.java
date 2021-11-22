package com.flashtract.billing.contracts.jpa.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.lang.NonNull;

import com.flashtract.billing.contracts.jpa.InvoiceStatus;

@Entity
@Table(name = "INVOICE")
public class InvoiceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	private Integer id;

	@Transient
	private ContractEntity contract;

	@Column(name = "CONTRACT_ID")
	private int contractId;

	@NonNull
	@Column(name = "SUMMARY", nullable = false)
	private String summary;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TOTAL_AMOUNT", nullable = false)
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@Column(name = "LABOR_AMOUNT")
	private BigDecimal laborAmount = BigDecimal.ZERO;

	@Column(name = "IS_PAID", nullable = false)
	private boolean isPaid = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private InvoiceStatus status = InvoiceStatus.SUBMITTED;

	@OneToOne
	@JoinColumn(name = "CREATED_BY")
	private UserEntity createdBy;

	@Column(name = "CREATED_DT", nullable = false)
	private LocalDateTime createdDt = LocalDateTime.now();

	@Column(name = "UPDATED_DT")
	private LocalDateTime updatedDt;

	@OneToOne
	@JoinColumn(name = "UPDATED_BY")
	private UserEntity updatedBy;

	@OneToMany
	@JoinColumn(name = "INVOICE_ID")
	private List<MaterialEntity> materials;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ContractEntity getContract() {
		return contract;
	}

	public void setContract(ContractEntity contract) {
		this.contract = contract;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTotalAmount() {
		// Calculate totalAmount, all materials plus labor
		totalAmount = BigDecimal.ZERO;
		for (MaterialEntity material : getMaterials()) {
			totalAmount = totalAmount.add(material.getPrice().multiply(BigDecimal.valueOf(material.getQuantity())));
		}
		return totalAmount.add(getLaborAmount());
	}

	public BigDecimal getLaborAmount() {
		return laborAmount;
	}

	public void setLaborAmount(BigDecimal laborAmount) {
		this.laborAmount = laborAmount;
	}

	public boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public InvoiceStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(LocalDateTime createdDt) {
		this.createdDt = createdDt;
	}

	public LocalDateTime getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(LocalDateTime updatedDt) {
		this.updatedDt = updatedDt;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public UserEntity getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserEntity updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<MaterialEntity> getMaterials() {
		if (materials == null) {
			materials = new ArrayList<>();
		}
		return materials;
	}

	public void setMaterials(List<MaterialEntity> materials) {
		this.materials = materials;
	}

}
