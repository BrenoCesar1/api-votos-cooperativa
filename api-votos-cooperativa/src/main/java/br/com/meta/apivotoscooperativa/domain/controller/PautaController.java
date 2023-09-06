package br.com.meta.apivotoscooperativa.domain.controller;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosPautaDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoPautaDTO;
import br.com.meta.apivotoscooperativa.domain.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.domain.service.PautaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pautas")
public class PautaController {
    @Autowired
    public PautaController(PautaRepository repository,
                           PautaService pautaService){
        this.repository = repository;
        this.pautaService = pautaService;

    }
    private final PautaRepository repository;
    private final PautaService pautaService;

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid DadosPautaDTO dados, UriComponentsBuilder uriBuilder) {
        var pautaCadastrada = pautaService.cadastrar(dados);
        var uri = uriBuilder.path("/pautas/{id}").buildAndExpand(pautaCadastrada.getId()).toUri();
        return ResponseEntity.created(uri).body(pautaCadastrada);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoPautaDTO>> listar(@PageableDefault(sort = {"id"})Pageable pageable) {
        var page = pautaService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable  Long id) {
        var idpauta = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPautaDTO(idpauta));
    }
}
