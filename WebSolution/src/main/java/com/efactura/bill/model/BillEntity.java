package com.efactura.bill.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.efactura.client.model.ClientEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillEntity {

	private int id;
	
	@NotNull
	private ClientEntity client;
	
	@NotNull
	private Integer number;

	@NotNull
	private Date broadCast;
	
	@NotNull
	private Date expiration;
	
	@NotNull
	private Integer year;
	
	@NotNull
	private Integer tax;
	
	public BillEntity() {
		super();
	}
	
	public BillEntity(int id, @NotNull ClientEntity client, @NotNull Integer number, @NotNull Date broadCast, @NotNull Date expiration,
			@NotNull Integer year, @NotNull Integer tax) {
		super();
		this.id = id;
		this.client = client;
		this.number = number;
		this.broadCast = broadCast;
		this.expiration = expiration;
		this.year = year;
		this.tax = tax;
	}

}
