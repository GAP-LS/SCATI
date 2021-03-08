package com.suchorski.scati.exceptions;

public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 5292474850669119601L;

	public BadRequestException(String message) {
		super(message);
	}

}
