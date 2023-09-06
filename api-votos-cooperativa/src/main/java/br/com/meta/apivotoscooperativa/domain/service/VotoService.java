package br.com.meta.apivotoscooperativa.domain.service;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosVotosDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosListagemVotosDTO;
import br.com.meta.apivotoscooperativa.domain.entity.Votos;
import br.com.meta.apivotoscooperativa.domain.repository.SessaoRepository;
import br.com.meta.apivotoscooperativa.domain.repository.VotosRepository;
import br.com.meta.apivotoscooperativa.infra.Messages;
import br.com.meta.apivotoscooperativa.infra.exceptions.SessaoFechadaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class VotoService {
    @Autowired
    public VotoService(SessaoRepository sessaoRepository,
                       VotosRepository votosRepository,
                       ValidacoesService validacoesService){
        this.sessaoRepository = sessaoRepository;
        this.votosRepository = votosRepository;
        this.validacoesService = validacoesService;
    }
    private final SessaoRepository sessaoRepository;
    private final VotosRepository votosRepository;

    private final ValidacoesService validacoesService;
    public DadosListagemVotosDTO votar(DadosVotosDTO dados) {

        Votos voto = new Votos();
        voto.setVoto(dados.votoRefatorado());
        voto.setSessao(validacoesService.verificaSessao(dados.getNumeroDaSessao()));
        voto.setAssociado(validacoesService.verificaAssociado(dados.getCpf(), dados.getNumeroDaSessao()));


        if (LocalDateTime.now().isAfter(voto.getSessao().getAbertaAte())){
            throw new SessaoFechadaException(Messages.SESSAO_FECHADA);
        }
        if(dados.votoRefatorado().equalsIgnoreCase("sim")){
            votosRepository.atualizarVotosSim(dados.getNumeroDaSessao());
        } else if(dados.votoRefatorado().equalsIgnoreCase("nao")){
            votosRepository.atualizarVotosNao(dados.getNumeroDaSessao());
        }

        votosRepository.save(voto);
        return new DadosListagemVotosDTO(voto);
    }


}
