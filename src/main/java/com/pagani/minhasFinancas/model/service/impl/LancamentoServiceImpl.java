package com.pagani.minhasFinancas.model.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.pagani.minhasFinancas.exception.RegraNegocioException;
import com.pagani.minhasFinancas.model.entity.Lancamento;
import com.pagani.minhasFinancas.model.enums.StatusLancamento;
import com.pagani.minhasFinancas.model.enums.TipoLancamento;
import com.pagani.minhasFinancas.model.repository.LancamentoRepository;
import com.pagani.minhasFinancas.model.service.LancamentoService;

import jakarta.transaction.Transactional;

@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	@Autowired
	private LancamentoRepository repository;
	

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		validar(lancamento);
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}

	@Override
	@Transactional
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

	@Override
	public void validar(Lancamento lancamento) {
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals(" ")) {
			throw new RegraNegocioException("Informe uma descricao valida");
		}
		
		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um mês valido.");
		}
		
		if (lancamento.getAno() == null || lancamento.getAno().toString().length() !=4) {
			throw new RegraNegocioException("Informe um ano valido.");
		}
		
		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usuario.");
		}
		
		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			 throw new RegraNegocioException("Informe um valor valido");
		}
		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de lancamento.");
		}
	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {	
		return repository.findById(id);
	}

	@Override
	@Transactional
	public BigDecimal obterSaldoPorUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoDeLancamentoEUsuario(id, TipoLancamento.RECEITA.name());
		BigDecimal dispesa = repository.obterSaldoPorTipoDeLancamentoEUsuario(id, TipoLancamento.DESPESA.name());
		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}
		if (dispesa == null) {
			dispesa = BigDecimal.ZERO;
		}
		
		return receitas.subtract(dispesa);
	}

}
