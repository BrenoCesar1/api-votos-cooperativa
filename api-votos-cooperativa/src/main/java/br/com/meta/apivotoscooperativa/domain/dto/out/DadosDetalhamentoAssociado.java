package br.com.meta.apivotoscooperativa.domain.dto.out;

import br.com.meta.apivotoscooperativa.domain.entity.Associado;

public record DadosDetalhamentoAssociado(String nome, String cpf) {

    public DadosDetalhamentoAssociado (Associado associado){
    this(associado.getNome(), associado.getCpf());
    }
}
