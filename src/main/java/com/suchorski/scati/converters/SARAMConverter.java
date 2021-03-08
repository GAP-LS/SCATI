package com.suchorski.scati.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("saram")
public class SARAMConverter implements Converter<String> {

	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		return value.replaceAll("[.-]", "");
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, String value) {
		String saram = value.toString().replaceAll("[.-]", "");
		if (saram.length() == 7) {
			return saram.replaceFirst("(\\d+)(\\d)$", "$1-$2");
		}
		return saram;
	}

}
