package br.com.meta.apivotoscooperativa.domain.dto.in;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class DadosPautaDTO {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

}
