package com.relesi.modernizationbiometric.api.services;

import java.util.Optional;

import com.relesi.modernizationbiometric.api.entities.Funcionario;

public interface FuncionarioService {

	/**
	 * Persiste um funcionario na base de dados
	 * 
	 * @param funcionario
	 * @return funcionario
	 */

	Funcionario persistir(Funcionario funcionario);

	/**
	 * Busca e retorna um funcionario dado um cpf
	 * 
	 * @param cpf
	 * @return Optional <Funcionario>
	 */

	Optional<Funcionario> buscaPorCpf(String cpf);

	/**
	 * Busca e retorna um funcionario dado um email
	 * 
	 * @param email
	 * @return Optional <Funcionario>
	 */

	Optional<Funcionario> buscarPorEmail(String email);

	/**
	 * Busca e retorna um funcioario por Id
	 * 
	 * @param id
	 * @return Optional <Funcionario>
	 */

	Optional<Funcionario> buscarPorId(Long id);
}
