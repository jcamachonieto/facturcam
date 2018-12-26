package com.efactura.client.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientEntity {

	private int id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String cif;
	
	@NotNull
	private String address;
	
	@NotNull
	private String location;
	
	@NotNull
	private String province;
	
	@NotNull
	private String postalCode;
	
	@NotNull
	private String country;
	
	@NotNull
	private String telephone;
	
	@NotNull
	@Email
	private String email;
	

	public ClientEntity() {
		super();
	}
	
	public ClientEntity(int id, @NotNull String name, @NotNull String cif, @NotNull String address,
			@NotNull String location, @NotNull String province, @NotNull String postalCode, @NotNull String country,
			@NotNull String telephone, @NotNull @Email String email) {
		super();
		this.id = id;
		this.name = name;
		this.cif = cif;
		this.address = address;
		this.location = location;
		this.province = province;
		this.postalCode = postalCode;
		this.country = country;
		this.telephone = telephone;
		this.email = email;
	}

}
