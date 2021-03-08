package com.suchorski.scati.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("nomeCompleto")
public class NomeCompletoConverter implements Converter<String> {
	
	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, String value) {
		Matcher matcher = Pattern.compile("\\b(.)(.*?)\\b").matcher(value.toString());
		StringBuffer stringBuffer = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase() + matcher.group(2).toLowerCase());
		}
		return matcher.appendTail(stringBuffer).toString();
	}

}
