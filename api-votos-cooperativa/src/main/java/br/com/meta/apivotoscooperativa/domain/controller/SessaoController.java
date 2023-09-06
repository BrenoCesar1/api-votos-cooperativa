package br.com.meta.apivotoscooperativa.domain.controller;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosSessaoDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoPautaDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoSessaoDTO;
import br.com.meta.apivotoscooperativa.domain.entity.Sessao;
import br.com.meta.apivotoscooperativa.domain.repository.SessaoRepository;
import br.com.meta.apivotoscooperativa.domain.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("sessao")
public class SessaoController {

    @Autowired
    public SessaoController(SessaoService sessaoService, SessaoRepository repository) {
        this.sessaoService = sessaoService;
        this.repository = repository;
    }

    private final SessaoService sessaoService;
    private final SessaoRepository repository;

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping
    public ResponseEntity abrirSessao(@RequestBody @Valid DadosSessaoDTO dados, UriComponentsBuilder uriBuilder) {
        var sessaoAberta = sessaoService.abrirSessao(dados);
        var uri = uriBuilder.path("/sessao/{id}").buildAndExpand(sessaoAberta.getId()).toUri();
        return ResponseEntity.created(uri).body(sessaoAberta);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoSessaoDTO>> listar(@PageableDefault Pageable pageable) {
        var page = sessaoService.listar(pageable);
        if (page.isEmpty()) {
            throw new RuntimeException();
        }
        return ResponseEntity.ok(page);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        String tempoRestante = sessaoService.calcularTempoRestante(id);
        var idSessao = repository.getReferenceById(id);
        var dadosSessao = new DadosDetalhamentoSessaoDTO(idSessao);
        dadosSessao.setTempoRestante(tempoRestante);
        return ResponseEntity.ok(dadosSessao);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/ultimoCadastro")
    public ResponseEntity obterUltimaSessao() {
        long idSessao = repository.findLastSessionNumber();
        var sessao = detalhar(idSessao);
        if (sessao == null) {
            throw new RuntimeException();
        }
        return ResponseEntity.ok(sessao.getBody());
    }
}

