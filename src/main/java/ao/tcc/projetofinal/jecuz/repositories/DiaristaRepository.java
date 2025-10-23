package ao.tcc.projetofinal.jecuz.repositories;

import ao.tcc.projetofinal.jecuz.entities.Diarista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaristaRepository extends JpaRepository<Diarista, Long> {

    @Query(value = "SELECT u FROM Diarista u WHERE u.email=:email")
    Diarista findByDiarista(@Param("email") String email);

    @Query(value = "SELECT u FROM Diarista u WHERE u.numeroBi=:numeroBi")
    Diarista findByNumeroBi(@Param("numeroBi") String numeroBi);

}
