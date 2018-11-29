package com.relesi.modernizationbiometric.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.relesi.modernizationbiometric.api.dtos.LancamentoDto;
import com.relesi.modernizationbiometric.api.entities.Funcionario;
import com.relesi.modernizationbiometric.api.entities.Lancamento;
import com.relesi.modernizationbiometric.api.enums.TipoEnum;
import com.relesi.modernizationbiometric.api.response.Response;
import com.relesi.modernizationbiometric.api.services.FuncionarioService;
import com.relesi.modernizationbiometric.api.services.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Value("${pagincao.qtd_por_pagina}")
	private int qtdPorPagina;

	public LancamentoController() {

	}

	/**
	 * Retorna a listagem de lançamentos de um funcionário
	 * 
	 * @param funcioanarioId
	 * @return Responseentity<Response<LancamentoDto>>
	 */

	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
			@PathVariable("funcionarioId") Long funcionarioId, @RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {

		log.info("Busncando lançamentos por ID do funcionário: {}, página: {}", funcionarioId, pag);
		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();

		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));

		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);

	}

	/**
	 * Retorna um lançamento po ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response><LancamentoDto>>
	 */

	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando lancamento por ID: {}", id);
		Response<LancamentoDto> response = new Response<>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		if (!lancamento.isPresent()) {
			log.info("Lancamento na encontrado para ID: {}", id);
			response.getErros().add("Lancamento na encontrado para id" + id);
			return ResponseEntity.badRequest().body(response);

		}

		response.setData(this.converterLancamentoDto(lancamento.get()));
		return ResponseEntity.ok(response);

	}

	/**
	 * Adiciona um novo lançamento
	 * 
	 * @param lancamento
	 * @param result
	 * @return ResponseEntity<Response<LancamentoDto>>
	 * @throws ParseException
	 */

	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result) throws ParseException {

		log.info("Adicionando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

		if (result.hasErrors()) {
			log.info("Erro validando lançamento:  {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);

	}

	/**
	 * Atualiza os dados de um lançamento
	 * 
	 * @param id
	 * @param lancamento
	 * @return ResponseEntity<Response<LancamentoDto>>
	 * @throws ParseException
	 */

	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {

		log.info("Atualizando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		lancamentoDto.setId(Optional.of(id));
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

		if (result.hasErrors()) {
			log.info("Erro atualizando lançamento:  {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);

	}

	/**
	 * Remove um lançamento por ID
	 * 
	 * @param id
	 * @param    ResponseEntity<Response<Lancamento>>
	 */

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN)")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

		if (!lancamento.isPresent()) {
			log.info("Erro  ao remover devido ao lançamento ID:  {} ser inválido.", id);
			response.getErros().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());

	}

	/*
	 * Valida um funcionário, verificando se ele é existente ou válido no sistema.
	 * 
	 * @param lancamentoDto
	 * 
	 * @param result
	 */

	private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
		if (lancamentoDto.getFuncionarioId() == null) {
			result.addError(new ObjectError("funcionário", "Funcionário não informado."));
			return;
		}

		log.info("Validando funcionário id {}: ", lancamentoDto.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());

		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionário", "Funcionário não encontrado. ID Inexistente."));
		}

	}

	/*
	 * Converter uma entidade para seu respctivo DTO.
	 * 
	 * @param lancamento
	 * 
	 * @return LancamentoDto
	 */

	private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
		LancamentoDto lancamentoDto = new LancamentoDto();
		lancamentoDto.setId(Optional.of(lancamento.getId()));
		lancamentoDto.setData(this.dateformat.format(lancamento.getData()));
		lancamentoDto.setTipo(lancamento.getTipo().toString());
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
		lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());

		return lancamentoDto;
	}

	/**
	 * Converter um LancamentoDto para uma entidade Lancamento
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return Lancamento
	 * @throws ParseException
	 */

	private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result)
			throws ParseException {

		Lancamento lancamento = new Lancamento();

		if (lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());

			if (lanc.isPresent()) {
				lancamento = lanc.get();
			} else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado"));
			}
		} else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}

		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.dateformat.parse(lancamentoDto.getData()));

		if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido"));
		}

		return lancamento;

	}

}
