package com.pagani.minhasFinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.pagani.minhasFinancas.model.entity.Usuario;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@teste.com").build();	
		
		repository.save(usuario);
		
		boolean result = repository.existsByEmail("usuario@teste.com");
		
		Assertions.assertThat(result).isTrue();
	}
	
}
