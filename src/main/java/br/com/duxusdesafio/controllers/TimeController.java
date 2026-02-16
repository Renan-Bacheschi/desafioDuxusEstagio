package br.com.duxusdesafio.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.duxusdesafio.exceptions.PeriodoSemDadosException;
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

@RestController
@RequestMapping("/api/times")
//@CrossOrigin(origins = "*") // Config abrindo acesso ao Front Futuramente
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
    public ResponseEntity<TimeResponseDTO> cadastrar(@RequestBody TimeRequestDTO dto) {
        List<ComposicaoTime> listaComposicao = new ArrayList<>();
        Time novoTime = new Time(dto.data(), listaComposicao);

        for (Long id : dto.integrantesIds()) {
            Integrante integrante = integranteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ID não encontrado: " + id));

            ComposicaoTime comp = new ComposicaoTime(novoTime, integrante);
            listaComposicao.add(comp);
        }

        Time salvo = timeRepository.save(novoTime);

        List<String> nomes = salvo.getComposicaoTime().stream()
                .map(c -> c.getIntegrante().getNome())
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TimeResponseDTO(salvo.getId(), salvo.getData(), nomes));
    }

    //  -------------------- Metodos de consulta
    @GetMapping("/da-data")
    public ResponseEntity<Map<String, Object>> getTimeDaData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<Time> todosOsTimes = timeRepository.findAll();

        Time timeEncontrado = apiService.timeDaData(data, todosOsTimes);

        if (timeEncontrado == null) {
            throw new TimeNaoEncontradoException(data);
        }

        List<String> integrantesFormatados = timeEncontrado.getComposicaoTime().stream()
                .map(c -> c.getIntegrante().getNome() + " (" + c.getIntegrante().getFranquia() + ")")
                .toList();


        return ResponseEntity.ok(Map.of("data", data,
                "integrantes", integrantesFormatados));
    }

    @GetMapping("/contagem-por-funcao")
    public ResponseEntity<Map<String, Long>> getContagemPorFuncao(
            // Caso tenha datas nulas, Aplicação continua
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        Map<String, Long> resultado = apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);

        if (resultado == null || resultado.isEmpty()) {
            throw new PeriodoSemDadosException();
        }
        return  ResponseEntity.ok(resultado);
    }

    @GetMapping("/franquia-mais-famosa")
    public ResponseEntity<Map<String, String>> getFranquiaMaisFamosa(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();
        String franquiaMaisFamosa = apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes);

        if (franquiaMaisFamosa == null) {
            throw new PeriodoSemDadosException();
        }

        return ResponseEntity.ok(Map.of("Franquia", franquiaMaisFamosa));
    }

}