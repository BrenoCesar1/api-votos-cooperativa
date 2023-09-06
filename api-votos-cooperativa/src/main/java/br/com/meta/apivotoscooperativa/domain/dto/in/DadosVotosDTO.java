package br.com.meta.apivotoscooperativa.domain.dto.in;

import br.com.meta.apivotoscooperativa.infra.Messages;
import br.com.meta.apivotoscooperativa.infra.exceptions.SintaxeVotoInvalidaException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DadosVotosDTO {

        @NotNull
        Long numeroDaSessao;

        @NotNull
        String cpf;

        @NotBlank
        String voto;

    public String votoRefatorado() {

        String votoRefatorado = voto.toLowerCase().replaceAll("não", "nao");
        if(!votoRefatorado.equalsIgnoreCase("sim") && !votoRefatorado.equalsIgnoreCase("nao")) {
            throw new SintaxeVotoInvalidaException(Messages.SYNTAX_VOTO_INVALIDO);
        }

        return votoRefatorado ;
    }





}

