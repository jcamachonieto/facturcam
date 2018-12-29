package com.efactura.bill.model;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConceptEntity {

	private int id;
	
	@NotNull
	private Integer idBill;
	
	@NotNull
	private String description;

	@NotNull
	private Integer quantity;
	
	@NotNull
	private Integer taxBase;
	
	public ConceptEntity() {
		super();
	}
	
	public ConceptEntity(int id, @NotNull Integer idBill, @NotNull String description, @NotNull Integer quantity, @NotNull Integer taxBase) {
		super();
		this.id = id;
		this.idBill = idBill;
		this.description = description;
		this.quantity = quantity;
		this.taxBase = taxBase;
	}
	
	public ConceptEntity(@NotNull String description, @NotNull Integer quantity, @NotNull Integer taxBase) {
		super();
		this.description = description;
		this.quantity = quantity;
		this.taxBase = taxBase;
	}

}
