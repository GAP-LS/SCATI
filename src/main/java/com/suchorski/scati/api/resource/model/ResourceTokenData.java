package com.suchorski.scati.api.resource.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceTokenData {
	
	private int expires_in;
	private String access_token;
	
	public ResourceTokenData(String token) {
		this.expires_in = 0;
		this.access_token = token;
	}
	
}