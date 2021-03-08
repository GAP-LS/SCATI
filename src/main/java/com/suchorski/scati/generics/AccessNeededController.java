package com.suchorski.scati.generics;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.models.Usuario;

public interface AccessNeededController {
	
	public default void checkAccess(Usuario usuario, String roles) throws AccessDeniedException {
		if (!usuario.hasPerfil(roles)) {
			throw new AccessDeniedException();
		}
	}
	
	public void grantAccess() throws AccessDeniedException;

}
