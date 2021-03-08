package com.suchorski.scati.api.resource.service;

import javax.persistence.NoResultException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.suchorski.scati.api.resource.model.ResourceTokenData;
import com.suchorski.scati.api.resource.model.ResourceUserData;
import com.suchorski.scati.daos.TokenDAO;
import com.suchorski.scati.models.Token;

@Path("/v2")
public class ResourceService {
	
	@POST
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceTokenData token(@FormParam("grant_type") String grant_type, @FormParam("code") String token, @FormParam("redirect_uri") String redirectUri, @FormParam("client_id") String clientId, @FormParam("client_secret") String clientSecret) {
		return new ResourceTokenData(token);
	}
	
	@GET
	@Path("/userinfo/{secret_key}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceUserData oauthUserInfo(@HeaderParam("Authorization") String bearer, @PathParam("secret_key") String secretKey) {
		try (TokenDAO tokenDAO = new TokenDAO()) {
			String token = bearer.split("\\s")[1];
			Token t = tokenDAO.findByToken(token);
			if (secretKey.equals(t.getAplicacao().getChaveSecreta())) {
				return new ResourceUserData(t.getUsuario(), t.getAplicacao());
			}
			return new ResourceUserData("Chave secreta inválida");
		} catch (NoResultException e) {
			return new ResourceUserData("Token inválido");
		}
	}
	
	@GET
	@Path("/userinfo/{secret_key}/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceUserData restUserInfo(@PathParam("secret_key") String secretKey, @PathParam("token") String token) {
		try (TokenDAO tokenDAO = new TokenDAO()) {
			Token t = tokenDAO.findByToken(token);
			if (secretKey.equals(t.getAplicacao().getChaveSecreta())) {
				return new ResourceUserData(t.getUsuario(), t.getAplicacao());
			}
			return new ResourceUserData("Chave secreta inválida");
		} catch (NoResultException e) {
			return new ResourceUserData("Token inválido");
		}
	}

}
