package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service que possuirá as regras de negócio para o processamento dos dados
 * solicitados no desafio!
 * <p>
 * OBS ao candidato: PREFERENCIALMENTE, NÃO ALTERE AS ASSINATURAS DOS MÉTODOS!
 * Trabalhe com a proposta pura.
 *
 * @author carlosau
 */
@Service
public class ApiService {

    /**
     * Vai retornar um Time, com a composição do time daquela data
     */
    public Time timeDaData(LocalDate data, List<Time> todosOsTimes) {
        return todosOsTimes.stream()
                .filter(time -> time.getData()
                        .equals(data)).findFirst().orElse(null);
    }

    /**
     * Vai retornar o integrante que estiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
       List<Time> filtrados = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);
       if(filtrados.isEmpty())return null;

       Map<Integrante, Long> contagem = new HashMap<>();
       for (Time time : filtrados) {
           for (ComposicaoTime comp : time.getComposicaoTime()){
               Integrante integrante = comp.getIntegrante();
               contagem.put(integrante, contagem.getOrDefault(integrante, 0L) + 1);
           }
       }
       Integrante maisUsado = null;
       long maiorValor = -1;

       for (Map.Entry<Integrante, Long> entry : contagem.entrySet()) {
           if (entry.getValue() > maiorValor){
               maiorValor = entry.getValue();
               maisUsado = entry.getKey();
           }
       }
       return maisUsado;
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais comum
     * dentro do período
     */
    public List<String> integrantesDoTimeMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar a função mais comum nos times dentro do período
     */
    public String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> filtrados = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);

        Set<Integrante> integrantesUnicos = new HashSet<>();

        for (Time time : filtrados) {
            for (ComposicaoTime comp : time.getComposicaoTime()) {
                integrantesUnicos.add(comp.getIntegrante());
            }
        }
        // contando franquias usando apenas pessoas únicas
        Map<String, Long> contagem = new HashMap<>();
        for (Integrante integrante : integrantesUnicos) {
            String franquia = integrante.getFranquia();
            contagem.put(franquia, contagem.getOrDefault(franquia, 0L) + 1);
        }
        // Buscando a vencedora
        String famosa = null;
        long maiorContagem = -1;
        for (Map.Entry<String, Long> entry : contagem.entrySet()) {
            if (entry.getValue() >= maiorContagem) {
                maiorContagem = entry.getValue();
                famosa = entry.getKey();
            }
        }

        return famosa;
    }


    /**
     * Vai retornar o número (quantidade) de Franquias dentro do período
     */
    public Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> filtrados = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);

        return filtrados.stream()
                .flatMap(time -> time.getComposicaoTime().stream())
                .map(ComposicaoTime::getIntegrante) // Pega o objeto Integrante completo
                .distinct() // Remove Jordan duplicado antes de contar
                .map(Integrante::getFuncao) // Pega a função da pessoa única
                .collect(Collectors.groupingBy(f -> f, Collectors.counting()));
    }

    // DRY - Utilitário para filtrar com base em um intervalo.
    private List<Time> filtrarPorPeriodo(LocalDate inicio, LocalDate fim, List<Time> todosOsTimes) {
        return todosOsTimes.stream()
                .filter(time -> (inicio == null || !time.getData().isBefore(inicio)) &&
                        (fim == null || !time.getData().isAfter(fim)))
                .collect(Collectors.toList());

    }

}
