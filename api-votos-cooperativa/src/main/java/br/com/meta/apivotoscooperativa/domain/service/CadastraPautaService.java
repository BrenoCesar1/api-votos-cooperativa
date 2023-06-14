package br.com.meta.apivotoscooperativa.domain.service;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosPautaDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoPautaDTO;
import br.com.meta.apivotoscooperativa.domain.entity.Pauta;
import br.com.meta.apivotoscooperativa.domain.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastraPautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public DadosDetalhamentoPautaDTO cadastrar(DadosPautaDTO dados) {
        if (dados.getTitulo() == null) {
            throw new ValidacaoException("O campo 'Titulo' não pode ser vazio!");
        }
        if (dados.getDescricao() == null) {
            throw new ValidacaoException("O campo 'Descrição' não pode ser vazio!");
        }

        var pauta = new Pauta();
        pauta.setActive(true);
        pauta.setTitulo(dados.getTitulo());
        pauta.setDescricao(dados.getDescricao());
        pautaRepository.saveAndFlush(pauta);

        return new DadosDetalhamentoPautaDTO(pauta);
    }

}
