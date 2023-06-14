package br.com.meta.apivotoscooperativa.domain.controller;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosPautaDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoPautaDTO;
import br.com.meta.apivotoscooperativa.domain.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.domain.service.CadastraPautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("pautas")
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private CadastraPautaService cadastraPauta;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid DadosPautaDTO dados, UriComponentsBuilder uriBuilder) {
        var dto = cadastraPauta.cadastrar(dados);
        var uri = uriBuilder.path("/pautas/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<Page<DadosDetalhamentoPautaDTO>> listar(@PageableDefault(size = 5, sort = {"id"})Pageable pageable) {
        var page = pautaRepository.findAllByActiveTrue(pageable).map(DadosDetalhamentoPautaDTO::new);
        return ResponseEntity.ok(page);
    }
}
