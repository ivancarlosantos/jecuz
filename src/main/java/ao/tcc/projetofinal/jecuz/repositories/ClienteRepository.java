package ao.tcc.projetofinal.jecuz.repositories;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
