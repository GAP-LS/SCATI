package com.suchorski.scati.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.suchorski.scati.daos.PerfilDAO;
import com.suchorski.scati.models.Perfil;

@FacesConverter("perfil")
public class PerfilConverter implements Converter<Perfil> {

	@Override
	public Perfil getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try (PerfilDAO perfilDAO = new PerfilDAO()) {
			return perfilDAO.getById(Long.parseLong(value));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Perfil value) {
		if (value != null) {
			return String.valueOf(value.getId());
		}
		return null;
	}
	
}
