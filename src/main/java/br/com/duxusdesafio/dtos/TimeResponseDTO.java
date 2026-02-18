package br.com.duxusdesafio.dtos;

import java.time.LocalDate;
import java.util.List;

public record TimeResponseDTO(
        long id,
        String nome,
        LocalDate data,
        List<String> integrantes // Apenas os nomes
) {
}