package br.com.meta.apivotoscooperativa.domain.repository;

import br.com.meta.apivotoscooperativa.domain.entity.Pauta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    Page<Pauta> findAllByActiveTrue(Pageable pageable);

    @Query("""
            SELECT p FROM Pauta p
            WHERE p.titulo = :titulo
            """)
    Pauta findByTitulo(String titulo);

}
