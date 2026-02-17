package ao.tcc.projetofinal.jecuz.repositories;

import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdensDeServicoRepository extends JpaRepository<OrdensDeServico, Long> {}
