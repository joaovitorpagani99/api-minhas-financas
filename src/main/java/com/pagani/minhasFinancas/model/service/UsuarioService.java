package com.pagani.minhasFinancas.model.service;

import java.util.Optional;

import com.pagani.minhasFinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);

	void validarEmail(String email);
	
	Optional<Usuario> obterPorId(Long id);
	
}
