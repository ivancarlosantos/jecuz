package ao.tcc.projetofinal.jecuz.repositories;

import ao.tcc.projetofinal.jecuz.entities.Diarista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaristaRepository extends JpaRepository<Diarista, Long> {}
