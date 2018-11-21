package com.relesi.modernizationbiometric.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.relesi.modernizationbiometric.api.entities.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	Funcionario findByCpf(String cpf);

	Funcionario findByEmail(String email);

	Funcionario findByCpfOrEmail(String cpf, String email);

}
