package es.efactura.bill.model;

import java.util.Date;

import es.efactura.client.model.ClientDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BillDto {

	private int id;
	private ClientDto client;
	private int number;
	private Date broadcastDate;
	private Date expirationDate;

}
