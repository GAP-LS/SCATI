package com.suchorski.scati.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("cpf")
public class CPFConverter implements Converter<String> {

	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		return value.replaceAll("[.-]", "");
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, String value) {
		String cpf = value.toString().replaceAll("[.-]", "");
		if (cpf.length() == 11) {
			return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		}
		return cpf;
	}

}
