package br.com.meta.apivotoscooperativa.domain.controller;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosCadastroAssociado;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoAssociado;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosListagemAssociados;
import br.com.meta.apivotoscooperativa.domain.entity.Associado;
import br.com.meta.apivotoscooperativa.domain.repository.AssociadoRepository;
import br.com.meta.apivotoscooperativa.infra.exceptions.ValidacaoException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("associados")
public class AssociadoController {

    @Autowired
    private AssociadoRepository repository;



    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroAssociado dados, UriComponentsBuilder uriBuilder) {
        var associado = new Associado(dados);
        if (repository.existsByCpf(dados.cpf())) {
            throw new ValidacaoException("CPF já consta cadastrado");
        }
        repository.save(associado);
        var uri = uriBuilder.path("/associados/{id}").buildAndExpand(associado.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoAssociado(associado));
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemAssociados>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemAssociados::new);
        var QTD_associados = repository.contarAssociados();
        if ( QTD_associados <= 0) {
            throw new ValidacaoException("Não há associados cadastrados");
        }
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var associado = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoAssociado(associado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var associado = repository.getReferenceById(id);
        if (!repository.existsById(associado.getId())) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(associado.getId());
        return ResponseEntity.ok("Associado excluído com sucesso");


    }
}
