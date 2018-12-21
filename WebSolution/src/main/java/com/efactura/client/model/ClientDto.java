package com.efactura.client.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientDto {

	private int id;
	private String name;
	private String cif;
	private String address;
	private String location;
	private String province;
	private String postalCode;
	private String country;
	private String telephone;
	private String email;

}
