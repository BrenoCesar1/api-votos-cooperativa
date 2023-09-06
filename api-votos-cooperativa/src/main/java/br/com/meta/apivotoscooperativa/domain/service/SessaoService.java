package br.com.meta.apivotoscooperativa.domain.service;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosSessaoDTO;
import br.com.meta.apivotoscooperativa.domain.dto.out.DadosDetalhamentoSessaoDTO;
import br.com.meta.apivotoscooperativa.domain.entity.Sessao;
import br.com.meta.apivotoscooperativa.domain.repository.PautaRepository;
import br.com.meta.apivotoscooperativa.domain.repository.SessaoRepository;
import br.com.meta.apivotoscooperativa.infra.Messages;
import br.com.meta.apivotoscooperativa.infra.exceptions.RequisicaoFalhaException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class SessaoService {
    @Autowired
    public SessaoService(SessaoRepository sessaoRepository,
                         PautaRepository pautaRepository){
        this.sessaoRepository = sessaoRepository;
        this.pautaRepository = pautaRepository;}

    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;


    public DadosDetalhamentoSessaoDTO abrirSessao(DadosSessaoDTO dados) {
        var fimDaVotacao = LocalDateTime.now().plusMinutes(1);
        var possivelPauta = pautaRepository.findById(dados.getIdPauta());
        if (possivelPauta.isEmpty()) {
            throw new EntityNotFoundException("Pauta não encontrada.");
        }
        if (dados.getDuracaoEmMinutos() != null) {
            fimDaVotacao = LocalDateTime.now().plusMinutes(dados.getDuracaoEmMinutos());
        }
        var pauta = possivelPauta.get();
        var sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setAtiva(true);
        if (sessaoRepository.existsByPautaId(dados.getIdPauta())) {
            throw new RequisicaoFalhaException(Messages.EXISTE_SESSAO_PARA_PAUTA);
        }
        sessao.setAbertaAte(fimDaVotacao);
        sessaoRepository.save(sessao);

        // Cálculo da contagem regressiva
        LocalDateTime agora = LocalDateTime.now();
        Duration duracaoRestante = Duration.between(agora, fimDaVotacao);
        String tempoRestante = formatarTempoRestante(duracaoRestante);

        DadosDetalhamentoSessaoDTO detalhesSessaoDTO = new DadosDetalhamentoSessaoDTO(sessao);
        detalhesSessaoDTO.setTempoRestante(tempoRestante); // Adicione isso ao DTO

        return detalhesSessaoDTO;
    }

    // Método para formatar o tempo restante em HH:MM:SS

    public Page<DadosDetalhamentoSessaoDTO> listar(Pageable pageable) {
        var dadosDetalhamentoSessaoList = sessaoRepository.findAll(pageable).map(DadosDetalhamentoSessaoDTO::new);
        return dadosDetalhamentoSessaoList;
    }
    private String formatarTempoRestante(Duration duracao) {
        if (duracao.isNegative()) {
            long horasRestantes = 0;
            long minutosRestantes = 0;
            long segundosRestantes = 0;
            return String.format("%02d:%02d:%02d", horasRestantes, minutosRestantes, segundosRestantes);
        }
        long horasRestantes = duracao.toHours();
        duracao = duracao.minusHours(horasRestantes);
        long minutosRestantes = duracao.toMinutes();
        duracao = duracao.minusMinutes(minutosRestantes);
        long segundosRestantes = duracao.getSeconds();
        return String.format("%02d:%02d:%02d", horasRestantes, minutosRestantes, segundosRestantes);
    }

    public String calcularTempoRestante(Long sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada."));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String abertaAte = sessao.getAbertaAte().format(formatter);
        LocalTime horaAbertaAteTime = LocalTime.parse(abertaAte, formatter);
        LocalDateTime abertaAteComHora = sessao.getAbertaAte().toLocalDate().atTime(horaAbertaAteTime);
        LocalDateTime agora = LocalDateTime.now();
        Duration duracaoRestante = Duration.between(agora, abertaAteComHora);
        return formatarTempoRestante(duracaoRestante);
    }


}





