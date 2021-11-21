package com.flashtract.billing.contracts.jpa.persistence;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.flashtract.billing.contracts.jpa.UserType;

@Entity
@Table(name = "USER")
public class UserEntity {

	@Id
	@Column(name = "ID", nullable = false, unique = true)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", nullable = false)
	private UserType type;

	@Basic
	@Column(name = "USERNAME", nullable = false)
	private String username;

	@Basic
	@Column(name = "NAME", nullable = false)
	private String name;

	public UserEntity() {
	}

	public UserEntity(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
