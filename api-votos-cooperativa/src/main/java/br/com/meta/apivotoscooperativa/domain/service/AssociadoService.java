package br.com.meta.apivotoscooperativa.domain.service;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosCadastroAssociadoDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoAssociadoDTO;
import br.com.meta.apivotoscooperativa.domain.entity.Associado;
import br.com.meta.apivotoscooperativa.domain.repository.AssociadoRepository;
import br.com.meta.apivotoscooperativa.domain.repository.VotosRepository;
import br.com.meta.apivotoscooperativa.infra.Messages;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssociadoService {
    @Autowired
    public AssociadoService(AssociadoRepository repository,
                            VotosRepository votosRepository,
                            ValidacoesService validacoesService) {
        this.repository = repository;
        this.votosRepository = votosRepository;
        this.validacoesService = validacoesService;
    }


    private final AssociadoRepository repository;
    private final VotosRepository votosRepository;
    private final ValidacoesService validacoesService;

    public DadosDetalhamentoAssociadoDTO cadastrar(DadosCadastroAssociadoDTO dados) {
        if (!validacoesService.validarCpf(dados.cpf().trim())) {
            throw new EntityNotFoundException(Messages.CPF_NAO_ELEGIVEL);
        }
        var associado = new Associado(dados);
        if (repository.existsByCpf(dados.cpf())) {
            throw new EntityNotFoundException(Messages.CPF_JA_CADASTRADO);
        }
        repository.save(associado);
        return new DadosDetalhamentoAssociadoDTO(associado);
    }

    public Page<DadosDetalhamentoAssociadoDTO> listar(Pageable pageable) {
        var dadosDetalhamentoAssociadoList = repository.findAll(pageable).map(DadosDetalhamentoAssociadoDTO::new);

        if (dadosDetalhamentoAssociadoList.isEmpty()) {
            throw new EntityNotFoundException(Messages.ASSOCIADOS_INEXISTENTES);
        }
        return dadosDetalhamentoAssociadoList;
    }

    public void deletar (Long id){
        var associadoOptional = repository.findById(id);
        if(!associadoOptional.isPresent()){
            throw new EntityNotFoundException(Messages.ASSOCIADO_INEXISTENTE);
        }
        var associado = associadoOptional.get();
        new DadosDetalhamentoAssociadoDTO(associado);
        repository.deleteById(associado.getId());

    }
}

