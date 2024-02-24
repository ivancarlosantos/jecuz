package ao.tcc.projetofinal.jecuz.repositories;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "SELECT u FROM Cliente u WHERE u.email=:email")
    Cliente findByCliente(@Param("email") String email);
}
