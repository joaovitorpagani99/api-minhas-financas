package com.pagani.minhasFinancas.model.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pagani.minhasFinancas.exception.ErroAutenticacao;
import com.pagani.minhasFinancas.exception.RegraNegocioException;
import com.pagani.minhasFinancas.model.entity.Usuario;
import com.pagani.minhasFinancas.model.repository.UsuarioRepository;
import com.pagani.minhasFinancas.model.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario  = repository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("usuario não encontrado.");
		}
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha invalida.");
		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
		
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
