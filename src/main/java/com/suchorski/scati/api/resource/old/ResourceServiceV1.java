package com.suchorski.scati.api.resource.old;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.api.resource.model.ResourceUserData;
import com.suchorski.scati.exceptions.ApplicationException;

@Path("/v1")
public class ResourceServiceV1 {
	
	@Inject private LoginUnicoController loginUnico;
	
	@POST
	@Path("/ldap")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceUserData ldap(@FormParam("login") String username, @FormParam("senha") String password) {
		try {
			return new ResourceUserData(loginUnico.findUsuario(username, password));
		} catch (NamingException | ApplicationException e) {
			return new ResourceUserData("Usuário não encontrado");
		}
	}
	
}
