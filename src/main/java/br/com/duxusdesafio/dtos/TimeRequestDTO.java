package br.com.duxusdesafio.dtos;

import java.time.LocalDate;
import java.util.List;

public record TimeRequestDTO(
        LocalDate data,
        List<Long> integrantesIds
) {}