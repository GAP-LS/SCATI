package com.suchorski.scati.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.suchorski.scati.daos.AplicacaoDAO;
import com.suchorski.scati.models.Aplicacao;

@FacesConverter("aplicacao")
public class AplicacaoConverter implements Converter<Aplicacao> {

	@Override
	public Aplicacao getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try (AplicacaoDAO aplicacaoDAO = new AplicacaoDAO()) {
			return aplicacaoDAO.getById(Long.parseLong(value));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Aplicacao value) {
		if (value != null) {
			return String.valueOf(value.getId());
		}
		return null;
	}
	
}
