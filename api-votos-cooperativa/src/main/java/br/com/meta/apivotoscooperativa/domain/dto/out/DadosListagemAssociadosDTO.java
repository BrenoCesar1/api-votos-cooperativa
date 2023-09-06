package br.com.meta.apivotoscooperativa.domain.dto.out;

import br.com.meta.apivotoscooperativa.domain.entity.Associado;

public record DadosListagemAssociadosDTO(Long id, String nome, String cpf) {
    public DadosListagemAssociadosDTO(Associado associado){
        this(associado.getId(), associado.getNome(), associado.getCpf());
    }
}
