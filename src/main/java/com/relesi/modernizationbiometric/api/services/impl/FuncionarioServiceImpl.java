package com.relesi.modernizationbiometric.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.relesi.modernizationbiometric.api.entities.Funcionario;
import com.relesi.modernizationbiometric.api.repositories.FuncionarioRepository;
import com.relesi.modernizationbiometric.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

	private static Logger log = LoggerFactory.getLogger(FuncionarioService.class);

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo funcionário: {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	public Optional<Funcionario> buscaPorCpf(String cpf) {
		log.info("Buscando funcionário pelo CPF {}", cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscando funcionario por EMAIL: {}", email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}

	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Busncando funcionário por ID {}", id);
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
	}

}
