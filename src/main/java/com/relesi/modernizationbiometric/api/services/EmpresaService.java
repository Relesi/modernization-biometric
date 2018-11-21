package com.relesi.modernizationbiometric.api.services;

import java.util.Optional;

import com.relesi.modernizationbiometric.api.entities.Empresa;

public interface EmpresaService {

	/**
	 * Retorna uma empresa dado um CNPJ.
	 * 
	 * @Param cnpj 
	 * @return Optional<empresa>
	 * 
	 */

	Optional<Empresa> buscarPorCnpj(String cnpj);

	/**
	 * 
	 * Cadastra uma nova empresa na base de dados
	 * 
	 * @Param empresa
	 * @return Empresa
	 * 
	 */

	Empresa persistir(Empresa empresa);
}
