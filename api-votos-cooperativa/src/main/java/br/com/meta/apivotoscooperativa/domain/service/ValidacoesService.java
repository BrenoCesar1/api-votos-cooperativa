package br.com.meta.apivotoscooperativa.domain.service;

import br.com.meta.apivotoscooperativa.domain.dto.in.DadosVotosDTO;
import br.com.meta.apivotoscooperativa.domain.entity.Associado;
import br.com.meta.apivotoscooperativa.domain.entity.Sessao;
import br.com.meta.apivotoscooperativa.domain.repository.AssociadoRepository;
import br.com.meta.apivotoscooperativa.domain.repository.SessaoRepository;
import br.com.meta.apivotoscooperativa.domain.repository.VotosRepository;
import br.com.meta.apivotoscooperativa.infra.Messages;
import br.com.meta.apivotoscooperativa.infra.exceptions.CpfInvalidoException;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class ValidacoesService {
    @Autowired
    public ValidacoesService(SessaoRepository sessaoRepository,
                             AssociadoRepository associadoRepository,
                             VotosRepository votosRepository){
        this.sessaoRepository = sessaoRepository;
        this.associadoRepository = associadoRepository;
        this.votosRepository = votosRepository;
    }
    private final SessaoRepository sessaoRepository;
    private final AssociadoRepository associadoRepository;
    private final VotosRepository votosRepository;

    private class DadosCpf {
        private Long cpf;
        private String valid;

    }

    public boolean validarCpf(String cpf) {
        DadosCpf dadosCpf;
        if(cpf.length() < 11 || cpf.length() > 11){
            throw new CpfInvalidoException(Messages.CPF_INVALIDO);
        }
        var request =  new HttpGet("https://api.nfse.io/validate/NaturalPeople/taxNumber/"+cpf);
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
            CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                var result = EntityUtils.toString(entity);
                Gson gson = new Gson();
                dadosCpf = gson.fromJson(result, DadosCpf.class);
                return Objects.equals(dadosCpf.valid, "true");
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Associado verificaAssociado(String numeroCpf, Long numeroDaSessao) {
        Associado associado = associadoRepository.findByCpf(numeroCpf);
        if (associado == null) {
            throw new EntityNotFoundException(Messages.ASSOCIADO_INEXISTENTE);
        }
        Long qtdVotosPorAssociado = votosRepository.existsByAssociadoIdAndSessaoId(associado.getId(), numeroDaSessao);
        if (qtdVotosPorAssociado >= 1) {
            throw new EntityNotFoundException(Messages.VOTO_UNICO);
        }
        return associado;
    }
    public Sessao verificaSessao(Long numeroDaSessao){
        Sessao sessao = sessaoRepository.findByNumeroDaSessao(numeroDaSessao);
        if (sessao == null) {
            throw new EntityNotFoundException(Messages.SESSAO_INEXISTENTE);
        }
        return sessao;
    }
}
