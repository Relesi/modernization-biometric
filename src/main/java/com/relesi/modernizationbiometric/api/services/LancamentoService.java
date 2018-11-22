package com.relesi.modernizationbiometric.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.relesi.modernizationbiometric.api.entities.Lancamento;

public interface LancamentoService {

	/**
	 * Retorna uma lista paginada de lancamentos de um determinado funcionário.
	 * 
	 * @param funcionarioId
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */

	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);

	/**
	 * Retorna um lancamento por Id
	 * 
	 * @param Id
	 * @return Optional<Lancamento> *
	 */

	Optional<Lancamento> buscarPorId(Long id);

	/**
	 * Persiste um Lancamento na base de dados
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */

	Lancamento persistir(Lancamento lancamento);

	/**
	 * Remove um lancamento da base de dados
	 * 
	 * @param Id
	 * 
	 */

	void remover(Long id);
}
