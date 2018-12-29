package com.efactura.bill.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillEntity {

	private int id;
	
	@NotNull
	private Integer idClient;
	
	private Long number;

	@NotNull
	private Date broadCast;
	
	@NotNull
	private Date expiration;
	
	private Integer year;
	
	@NotNull
	private Integer tax;
	
	private String clientName;
	
	public BillEntity() {
		super();
	}
	
	public BillEntity(int id, @NotNull Integer idClient, Long number, @NotNull Date broadCast, @NotNull Date expiration,
			Integer year, @NotNull Integer tax, String clientName) {
		super();
		this.id = id;
		this.idClient = idClient;
		this.number = number;
		this.broadCast = broadCast;
		this.expiration = expiration;
		this.year = year;
		this.tax = tax;
		this.clientName = clientName;
	}

}
