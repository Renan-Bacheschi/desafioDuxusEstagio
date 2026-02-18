package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.dtos.IntegranteDTO;
import br.com.duxusdesafio.exceptions.PeriodoSemDadosException;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import br.com.duxusdesafio.repositories.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/integrantes")
@CrossOrigin(origins = "*") // Config abrindo acesso ao Front Futuramente
public class IntegranteController {

    private final IntegranteRepository integranteRepository;
    private final TimeRepository timeRepository;
    private final ApiService apiService;

    // Injeção via construtor, SOLID
    public IntegranteController(IntegranteRepository integranteRepository, TimeRepository timeRepository, ApiService apiService) {
        this.integranteRepository = integranteRepository;
        this.timeRepository = timeRepository;
        this.apiService = apiService;
    }

    @PostMapping
    public ResponseEntity<Integrante> cadastrar(@RequestBody IntegranteDTO dto) {
        Integrante novoIntegrante = new Integrante();
        novoIntegrante.setNome(dto.nome());
        novoIntegrante.setFranquia(dto.franquia());
        novoIntegrante.setFuncao(dto.funcao());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(integranteRepository.save(novoIntegrante));
    }

    @GetMapping
    public ResponseEntity<List<Integrante>> listarTodos() {
        return ResponseEntity.ok(integranteRepository.findAll());
    }

    @GetMapping("/mais-usado")
    public ResponseEntity<Map<String, String>> getIntegranteMaisUsado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        Integrante maisUsado = apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);

        if (maisUsado == null) {
            throw new PeriodoSemDadosException("Nenhum integrante encontrado no período informado.");
        }

        return ResponseEntity.ok(Map.of("integrante", maisUsado.getNome()));
    }
}