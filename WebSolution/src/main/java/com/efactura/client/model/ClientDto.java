package com.efactura.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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
