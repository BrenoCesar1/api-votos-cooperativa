package br.com.meta.apivotoscooperativa.domain.repository;

import br.com.meta.apivotoscooperativa.domain.entity.Associado;
import br.com.meta.apivotoscooperativa.domain.entity.Pauta;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    boolean existsByCpf(String cpf);

    @Query(value = "SELECT COUNT(id) FROM associados a", nativeQuery = true)
    Long contarAssociados();

}
