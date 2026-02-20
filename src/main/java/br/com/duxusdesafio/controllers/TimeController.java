package br.com.duxusdesafio.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.duxusdesafio.exceptions.PeriodoSemDadosException;
import br.com.duxusdesafio.exceptions.SemDataException;
import br.com.duxusdesafio.exceptions.TimeNaoEncontradoException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.repositories.TimeRepository;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import br.com.duxusdesafio.service.ApiService;
import br.com.duxusdesafio.dtos.TimeRequestDTO;
import br.com.duxusdesafio.dtos.TimeResponseDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/times")
@CrossOrigin(origins = "*") // Config abrindo acesso ao Front Futuramente
public class TimeController {

    private final TimeRepository timeRepository;
    private final IntegranteRepository integranteRepository;
    private final ApiService apiService;

    public TimeController(TimeRepository timeRepository,
                          IntegranteRepository integranteRepository,
                          ApiService apiService) {
        this.timeRepository = timeRepository;
        this.integranteRepository = integranteRepository;
        this.apiService = apiService;
    }

    @PostMapping
    public ResponseEntity<TimeResponseDTO> cadastrar(@Valid @RequestBody TimeRequestDTO dto) {
        List<ComposicaoTime> listaComposicao = new ArrayList<>();

        Time novoTime = new Time(dto.nome(), dto.data(), listaComposicao);
        for (Long id : dto.integrantesIds()) {
            Integrante integrante = integranteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ID não encontrado: " + id));

            ComposicaoTime comp = new ComposicaoTime(novoTime, integrante);
            listaComposicao.add(comp);
        }

        Time salvo = timeRepository.save(novoTime);
        List<String> nomesDosIntegrantes = new ArrayList<>();
        for (ComposicaoTime c : salvo.getComposicaoTime()) {
            nomesDosIntegrantes.add(c.getIntegrante().getNome());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TimeResponseDTO(salvo.getId(), salvo.getNome(), salvo.getData(), nomesDosIntegrantes));
    }
    // -------------- Metodo de consulta adicional
    @GetMapping("/buscar-data-com-nome")
    public ResponseEntity<Map<String, Object>> getTimeDaDataComNome(
            @RequestParam String nome,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<Time> todosOsTimes = timeRepository.findAll();
        Time timeEncontrado = apiService.buscarTimePorNomeEData(nome, data, todosOsTimes);

        if (timeEncontrado == null) {
            throw new TimeNaoEncontradoException(data);
        }

        List<String> integrantesFormatados = new ArrayList<>();
        for (ComposicaoTime c : timeEncontrado.getComposicaoTime()) {
            integrantesFormatados.add(c.getIntegrante().getNome() + " (" + c.getIntegrante().getFranquia() + ")");
        }

        return ResponseEntity.ok(Map.of(
                "nome", timeEncontrado.getNome(),
                "data", timeEncontrado.getData(),
                "integrantes", integrantesFormatados
        ));
    }

    //  -------------------- Metodos de consulta
    @GetMapping("/da-data")
    public ResponseEntity<Map<String, Object>> getTimeDaData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        if (data == null) {
            throw new SemDataException();
        }
        List<Time> todosOsTimes = timeRepository.findAll();
        Time timeEncontrado = apiService.timeDaData(data, todosOsTimes);
        if (timeEncontrado == null) {
            throw new TimeNaoEncontradoException(data);
        }
        List<String> integrantesFormatados = new ArrayList<>();
        if (timeEncontrado.getComposicaoTime() != null) {
            for (ComposicaoTime c : timeEncontrado.getComposicaoTime()) {
                String nome = c.getIntegrante().getNome();
                String franquia = c.getIntegrante().getFranquia();
                integrantesFormatados.add(nome + " (" + franquia + ")");
            }
        }
        return ResponseEntity.ok(Map.of(
                "data", data,
                "integrantes", integrantesFormatados
        ));
    }
    @GetMapping("/integrante-mais-usado")
    public ResponseEntity<Map<String, Object>> getIntegranteMaisUsado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        Integrante integrante = apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);

        if (integrante == null) {
            throw new PeriodoSemDadosException("Nenhum integrante encontrado no período.");
        }

        return ResponseEntity.ok(Map.of(
                "nome", integrante.getNome(),
                "funcao", integrante.getFuncao(),
                "franquia", integrante.getFranquia()
        ));
    }

    @GetMapping("/time-mais-comum")
    public ResponseEntity<List<String>> getIntegrantesDoTimeMaisComum(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        List<String> integrantes = apiService.integrantesDoTimeMaisComum(dataInicial, dataFinal, todosOsTimes);

        if (integrantes.isEmpty()) {
            throw new PeriodoSemDadosException("Nenhum time comum encontrado no período.");
        }

        return ResponseEntity.ok(integrantes);
    }

    @GetMapping("/funcao-mais-comum")
    public ResponseEntity<Map<String, String>> getMaisComum(
            @RequestParam(required = false) @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE) LocalDate dataFinal){

        List<Time> todosOsTimes = timeRepository.findAll();
        String maisC = apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes);

        if (maisC ==  null) {
            throw new PeriodoSemDadosException("Nenhuma função encontrada no período informado.");
        }

        return ResponseEntity.ok(Map.of("Função mais comum: ", maisC));
    }

    @GetMapping("/franquia-mais-famosa")
    public ResponseEntity<Map<String, String>> getFranquiaMaisFamosa(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        String franquiaMaisFamosa = apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes);

        if (franquiaMaisFamosa == null) {
            throw new PeriodoSemDadosException("Nenhuma franquia encontrada no período informado.");
        }

        return ResponseEntity.ok(Map.of("Franquia", franquiaMaisFamosa));
    }

    @GetMapping("/contagem-por-franquia")
    public ResponseEntity<Map<String, Long>> getContagemPorFranquia(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        Map<String, Long> resultado = apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes);

        if (resultado.isEmpty()) {
            throw new PeriodoSemDadosException("Nenhuma franquia encontrada no período informado.");
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/contagem-por-funcao")
    public ResponseEntity<Map<String, Long>> getContagemPorFuncao(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        Map<String, Long> resultado = apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);

        if (resultado == null || resultado.isEmpty()) {
            throw new PeriodoSemDadosException("Nenhuma função encontrada no periodo informado.");
        }

        return ResponseEntity.ok(resultado);
    }

}