package com.suchorski.scati.ad.utils;

import java.security.MessageDigest;
import java.util.Base64;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenhaAD {
	
	public static final String SENHA = "1234567890";
	public static final String SHA1 = "{SHA}AbMHrLpPVPVar8M7sGu79sqAPpo=";
	public static final byte[] UNICODE = { 34, 0, 49, 0, 50, 0, 51, 0, 52, 0, 53, 0, 54, 0, 55, 0, 56, 0, 57, 0, 48, 0, 34, 0 };
	
	
			
	
	private String senha;
	private String sha1;
	private byte[] unicode;
	
	public SenhaAD(String senha) {
		try {
			this.senha = senha;
			this.sha1 = String.format("{SHA}%s", Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA1").digest(senha.getBytes("UTF-8"))));
			this.unicode = String.format("\"%s\"", senha).getBytes("UTF-16LE");
		} catch (Exception e) {
			this.senha = SENHA;
			this.sha1 = SHA1;
			this.unicode = UNICODE;
		}		
	}
	
	public static SenhaAD randomize() {
		int size = 10;
		String format = String.format("%%0%dd", size);
		String senha = String.format(format, (long) (Math.random() * Math.pow(10, size)));
		return new SenhaAD(senha);
	}
	
}
