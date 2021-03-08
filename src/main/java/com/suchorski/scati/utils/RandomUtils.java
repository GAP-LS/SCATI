package com.suchorski.scati.utils;

import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Classe de utilidades do SGTI
 * @author 2S Suchorski [GAP-LS]
 * @version 1.0.0
 */
public class RandomUtils {
	
	/**
	 * Gera números aleatórios (PIN)
	 * @param size tamanho do PIN
	 * @return PIN gerado
	 */
	public static String generatePin(int size) {
		String format = String.format("%%0%dd", size);
		return String.format(format, (long) (Math.random() * Math.pow(10, size)));
	}
	
	/**
	 * Gera cookies de tamanhos size * 32
	 * @param size
	 * @return Cookie gerado
	 */
	public static String generateCookie(int size) {
		StringBuffer sb = new StringBuffer();
		IntStream.range(0, size).forEach(i -> sb.append(UUID.randomUUID().toString()));
		return sb.toString().replaceAll("-", "");
	}
	
}
