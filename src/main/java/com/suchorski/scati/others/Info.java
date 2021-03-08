package com.suchorski.scati.others;

import lombok.Getter;

@Getter
public class Info {
	
	private String parameter;
	private String local;
	private String description;
	
	public Info(String parameter, String description, String local) {
		this.parameter = parameter;
		this.local = local;
		this.description = description;
	}

	public Info(String parameter, String description) {
		this(parameter, description, "");
	}
	
}