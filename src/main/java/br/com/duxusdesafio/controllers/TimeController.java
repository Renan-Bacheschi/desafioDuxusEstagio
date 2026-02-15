package br.com.duxusdesafio.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/da-data")
    public ResponseEntity<Map<String, Object>> getTimeDaData(@RequestParam String data) {
        List<Time> todosOsTimes = timeRepository.findAll();
        LocalDate dataBusca = LocalDate.parse(data);

        Time timeEncontrado = apiService.timeDaData(dataBusca, todosOsTimes);

        if (timeEncontrado == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> integrantesFormatados = timeEncontrado.getComposicaoTime().stream()
                .map(c -> c.getIntegrante().getNome() + " (" + c.getIntegrante().getFranquia() + ")")
                .toList();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("data", dataBusca);
        resposta.put("integrantes", integrantesFormatados);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/contagem-por-funcao")
    public ResponseEntity<Map<String, Long>> getContagemPorFuncao(
            // Caso tenha datas nulas, Aplicação continua
            @RequestParam(required = false) String dataInicial,
            @RequestParam(required = false) String dataFinal) {

        List<Time> todosOsTimes = timeRepository.findAll();


        LocalDate inicio = (dataInicial != null && !dataInicial.isEmpty()) ? LocalDate.parse(dataInicial) : null;
        LocalDate fim = (dataFinal != null && !dataFinal.isEmpty()) ? LocalDate.parse(dataFinal) : null;

        Map<String, Long> resultado = apiService.contagemPorFuncao(inicio, fim, todosOsTimes);

        return ResponseEntity.ok(resultado);
    }
}