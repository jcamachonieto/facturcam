package com.efactura.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSessionDto {
	private String name;
	private String email;
	private String databaseFile;
}
