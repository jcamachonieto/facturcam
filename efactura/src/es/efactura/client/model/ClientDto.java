package es.efactura.client.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientDto {

	private int id;
	private String name;
	private String cif;
	
}
