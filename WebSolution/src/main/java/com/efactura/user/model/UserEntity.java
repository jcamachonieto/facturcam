package com.efactura.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="user")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEntity {

	@Id
	@Email
	private String email;
	
	@Column
	private Date createDate;
	
	@Column
	private Date expirationDate;
	
	@Column
	private boolean trial;
	
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
	
	private Date backup;
	private String backupPeriod;

}
