package br.com.duxusdesafio.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.duxusdesafio.model.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;


@SpringBootTest
public class ApiServiceNovoTest {

    @Autowired
    private ApiService apiService;


    static Stream<Arguments> testTimePorNomeEDataParams() {
        DadosParaTeste3Parametros dados = new DadosParaTeste3Parametros();
        List<Time> todosOsTimes = dados.getTodosOsTimes();

        return Stream.of(
                // Argumentos (Nome, Data, ListaCompleta, TimeEsperado)
                Arguments.of("Chicago Bulls", LocalDate.of(1994, 1, 1), todosOsTimes, todosOsTimes.get(0)),
                Arguments.of("Chicago Bulls", LocalDate.of(1995, 1, 1), todosOsTimes, todosOsTimes.get(1)),
                Arguments.of("Detroit Pistons", LocalDate.of(1993, 1, 1), todosOsTimes, todosOsTimes.get(2))
        );
    }

    // teste
    @ParameterizedTest
    @MethodSource("testTimePorNomeEDataParams")
    public void testBuscarTimePorNomeEData(String nome, LocalDate data, List<Time> lista, Time esperado) {

        Time timeRetornado = apiService.buscarTimePorNomeEData(nome, data, lista);

        // Validações
        assertNotNull(timeRetornado, "ERRO: O time pesquisado não foi encontrado");
        assertEquals(esperado.getNome(), timeRetornado.getNome(), "ERRO: O nome do time retornado não condiz com o esperado.");
        assertEquals(esperado.getData(), timeRetornado.getData(), "ERRO: A data do time retornado está divergente.");

    }
}