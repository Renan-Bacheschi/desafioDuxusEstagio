package br.com.duxusdesafio.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record TimeRequestDTO(
        @NotBlank(message = "O nome do time não pode ser vazio")
        String nome,

        @NotNull(message = "A data é obrigatória")
        LocalDate data,

        @NotEmpty(message = "O time precisa de pelo menos um integrante")
        List<Long> integrantesIds
) {}