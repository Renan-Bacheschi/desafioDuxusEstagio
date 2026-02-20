package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
     * Busca um time pelo nome e pela data exata
     */
    public Time buscarTimePorNomeEData(String nome, LocalDate data, List<Time> todosOsTimes) {
        if (todosOsTimes == null || nome == null || data == null) {
            return null;
        }
        for (Time time : todosOsTimes) {
            if (time.getData().equals(data) && time.getNome().equalsIgnoreCase(nome)) {
                return time;
            }
        }

        return null;
    }

    /**
     * Vai retornar um Time, com a composição do time daquela data
     */
    public Time timeDaData(LocalDate data, List<Time> todosOsTimes) {
        for (Time time : todosOsTimes) {
            if (time.getData().equals(data)) {
                return time;
            }
        }

        return null;
    }

    /**
     * Vai retornar o integrante que estiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> filtrados = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        if (filtrados.isEmpty()) return null;

        Map<Integrante, Long> contagem = new HashMap<>();
        for (Time time : filtrados) {
            for (ComposicaoTime comp : time.getComposicaoTime()) {
                Integrante integrante = comp.getIntegrante();
                contagem.put(integrante, contagem.getOrDefault(integrante, 0L) + 1);
            }
        }

        Integrante maisUsado = null;
        long maiorValor = -1;

        for (Map.Entry<Integrante, Long> entry : contagem.entrySet()) {
            if (entry.getValue() > maiorValor) {
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
        List<Time> filtrados = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        if (filtrados.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Set<Integrante>, Integer> contagemComposicoes = new HashMap<>();
        for (Time time : filtrados) {
            Set<Integrante> integrantesDoTime = new HashSet<>();
            for (ComposicaoTime comp : time.getComposicaoTime()) {
                integrantesDoTime.add(comp.getIntegrante());
            }
            if (!integrantesDoTime.isEmpty()) {
                contagemComposicoes.put(integrantesDoTime, contagemComposicoes.getOrDefault(integrantesDoTime, 0) + 1);
            }
        }

        Set<Integrante> composicaoVencedora = null;
        int max = -1;
        for (Map.Entry<Set<Integrante>, Integer> entry : contagemComposicoes.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                composicaoVencedora = entry.getKey();
            }
        }

        List<String> nomesResultado = new ArrayList<>();
        if (composicaoVencedora != null) {
            for (Integrante i : composicaoVencedora) {
                nomesResultado.add(i.getNome());
            }
        }

        return nomesResultado;
    }
    /**
     * Vai retornar a função mais comum nos times dentro do período
     */
    public String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> filtraComum = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        Set<Integrante> integrantesUnicos = new HashSet<>();

        for (Time time : filtraComum) {
            for (ComposicaoTime comp : time.getComposicaoTime()) {
                integrantesUnicos.add(comp.getIntegrante());
            }
        }

        Map<String, Long> contando = new HashMap<>();
        for (Integrante integrante : integrantesUnicos) {
            String funcao = integrante.getFuncao();
            contando.put(funcao, contando.getOrDefault(funcao, 0L) + 1);
        }

        String resultadoComun = null;
        long contados = -1;
        for (Map.Entry<String, Long> entry : contando.entrySet()) {
            if (entry.getValue()>= contados){
                contados = entry.getValue();
                resultadoComun = entry.getKey();
            }
        }

        return resultadoComun;
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> filtraFamosa = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);

        Set<Integrante> integrantesUnicos = new HashSet<>();

        for (Time time : filtraFamosa) {
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
        List<Time> filtraQtdFranquia = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        Set<Integrante> integrantesUnicos = new HashSet<>();
        for (Time time: filtraQtdFranquia) {
            for (ComposicaoTime comp : time.getComposicaoTime()) {
                integrantesUnicos.add(comp.getIntegrante());
            }
        }

        Map<String, Long> contandoFranquia = new HashMap<>();
        for (Integrante integrante: integrantesUnicos) {
            String franquia = integrante.getFranquia();
            if (franquia != null) {
                contandoFranquia.put(franquia, contandoFranquia.getOrDefault(franquia, 0L) + 1);
            }
        }

        return contandoFranquia;

    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> filtrados = filtrarPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        Set<Integrante> integrantesUnicos = new HashSet<>();
        for (Time time : filtrados) {
            for (ComposicaoTime comp : time.getComposicaoTime()) {
                integrantesUnicos.add(comp.getIntegrante());
            }
        }

        Map<String, Long> contagemFuncao = new HashMap<>();
        for (Integrante integrante : integrantesUnicos) {
            String funcao = integrante.getFuncao();
            if (funcao != null) {
                contagemFuncao.put(funcao, contagemFuncao.getOrDefault(funcao, 0L) + 1);
            }
        }

        return contagemFuncao;
    }

    // DRY
    private List<Time> filtrarPorPeriodo(LocalDate inicio, LocalDate fim, List<Time> todosOsTimes) {
        List<Time> filtrados = new ArrayList<>();
        if (todosOsTimes == null) return filtrados;

        for (Time time : todosOsTimes) {
            LocalDate dataTime = time.getData();
            boolean atendeInicio = (inicio == null || !dataTime.isBefore(inicio));
            boolean atendeFim = (fim == null || !dataTime.isAfter(fim));

            if (atendeInicio && atendeFim) {
                filtrados.add(time);
            }
        }

        return filtrados;
    }

}
