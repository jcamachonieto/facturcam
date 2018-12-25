package com.efactura.message.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDto {
	private String text;
	private String title;
	private String type;
}
