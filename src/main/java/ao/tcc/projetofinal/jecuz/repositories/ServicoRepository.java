package ao.tcc.projetofinal.jecuz.repositories;

import ao.tcc.projetofinal.jecuz.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {}
