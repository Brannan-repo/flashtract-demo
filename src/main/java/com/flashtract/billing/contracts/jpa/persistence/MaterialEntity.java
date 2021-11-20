package com.flashtract.billing.contracts.jpa.persistence;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table(name = "MATERIAL")
public class MaterialEntity {

	@Id
	@Column(name = "ID", nullable = false, unique = true)
	private Integer id;

	@Basic
	@NonNull
	@Column(name = "NAME", nullable = false)
	private String name;

	@Basic
	@NonNull
	@Column(name = "QUANTITY", nullable = false)
	private Integer quantity;

	@Basic
	@NonNull
	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
