package es.efactura.license.model;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LicenseDto {

	private String email;
	private UUID key;
	private Date expirationDate;
	private String maxVersion;

}
