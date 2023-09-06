package br.com.meta.apivotoscooperativa.domain.repository;

import br.com.meta.apivotoscooperativa.domain.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    boolean existsByPautaId(Long idPauta);
    @Query(value = "SELECT s.numero_da_sessao FROM Sessao s WHERE s.numero_da_sessao = (SELECT MAX(ss.numero_da_sessao) FROM Sessao ss)", nativeQuery = true)
    Long findLastSessionNumber();

    @Query(value = "SELECT * FROM sessao WHERE numero_da_sessao = :numeroDaSessao", nativeQuery = true)
    Sessao findByNumeroDaSessao(Long numeroDaSessao);

}
