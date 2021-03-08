package com.suchorski.scati.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("pin")
public class PINConverter implements Converter<String> {

	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, String value) {
		String pin = value.toString();
		if (pin.length() > 2) {
			return pin.substring(0, 1) + new String(new char[pin.length() - 2]).replace("\0", "*") + pin.substring(pin.length() - 1);
		}
		return "**";
	}

}
