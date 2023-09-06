package br.com.meta.apivotoscooperativa.domain.controller;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosCadastroAssociadoDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoAssociadoDTO;
import br.com.meta.apivotoscooperativa.domain.repository.AssociadoRepository;
import br.com.meta.apivotoscooperativa.domain.service.AssociadoService;
import br.com.meta.apivotoscooperativa.infra.Messages;
import jakarta.persistence.EntityNotFoundException;
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
    public AssociadoController(AssociadoRepository repository,
                               AssociadoService associadoService){
        this.repository = repository;
        this.associadoService = associadoService;}


    private final AssociadoRepository repository;
    private final AssociadoService associadoService;



    @PostMapping
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroAssociadoDTO dados, UriComponentsBuilder uriBuilder) {
       var associado = associadoService.cadastrar(dados);
        var uri = uriBuilder.path("/associados/{id}").buildAndExpand(associado.id()).toUri();
        return ResponseEntity.created(uri).body(associado);
    }


    @GetMapping
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<Page<DadosDetalhamentoAssociadoDTO>> listar(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {
        var page = associadoService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var associado = repository.findById(id);
        if(associado.isEmpty()){
            throw new EntityNotFoundException(Messages.ASSOCIADO_INEXISTENTE);
        }
        return ResponseEntity.ok(new DadosDetalhamentoAssociadoDTO(associado.get()));
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity excluir(@PathVariable Long id) {
        associadoService.deletar(id);
        return ResponseEntity.ok(Messages.ASSOCIADO_DELETADO);


    }
}
