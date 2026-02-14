package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.dtos.IntegranteDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integrantes")
//@CrossOrigin(origins = "*") // Config abrindo acesso ao Front Futuramente
public class IntegranteController {

    private final IntegranteRepository repository;

    // Injeção via construtor, SOLID
    public IntegranteController(IntegranteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Integrante> cadastrar(@RequestBody IntegranteDTO dto) {
        Integrante novoIntegrante = new Integrante();
        novoIntegrante.setNome(dto.nome());
        novoIntegrante.setFranquia(dto.franquia());
        novoIntegrante.setFuncao(dto.funcao());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repository.save(novoIntegrante));
    }
}