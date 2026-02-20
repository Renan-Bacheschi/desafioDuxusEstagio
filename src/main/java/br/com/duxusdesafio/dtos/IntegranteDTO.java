package br.com.duxusdesafio.dtos;

import javax.validation.constraints.NotBlank;

public record IntegranteDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A franquia é obrigatória")
        String franquia,

        @NotBlank(message = "A função é obrigatória")
        String funcao
) {}