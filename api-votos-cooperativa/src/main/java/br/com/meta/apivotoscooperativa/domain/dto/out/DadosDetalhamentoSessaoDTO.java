package br.com.meta.apivotoscooperativa.domain.dto.out;

import br.com.meta.apivotoscooperativa.domain.entity.Pauta;
import br.com.meta.apivotoscooperativa.domain.entity.Sessao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class DadosDetalhamentoSessaoDTO {
    private Long id;
    private PautaDTO pauta;
    private LocalDateTime abertaAte;
    private String tempoRestante;

    public DadosDetalhamentoSessaoDTO(Sessao sessao) {
        this.id = sessao.getNumeroDaSessao();
        this.pauta = new PautaDTO(sessao.getPauta());
        this.abertaAte = sessao.getAbertaAte();
        this.tempoRestante = getTempoRestante();
    }

    @Data
    public static class PautaDTO{
        private Long id;
        private String titulo;
        private String descricao;
        private int votosSim;
        private int votosNao;

        public PautaDTO(Pauta pauta){
            this.id = pauta.getId();
            this.titulo = pauta.getTitulo();
            this.descricao = pauta.getDescricao();
            this.votosSim = pauta.getVotosSim();
            this.votosNao = pauta.getVotosNao();
        }
    }
}

