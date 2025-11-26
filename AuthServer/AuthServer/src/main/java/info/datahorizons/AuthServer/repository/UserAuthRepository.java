package info.datahorizons.AuthServer.repository;

import info.datahorizons.AuthServer.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
    
    Optional<UserAuth> findByLoginAndActiveTrue(String login);
    
    // Busca por email (para login com email)
    Optional<UserAuth> findByEmailAndActiveTrue(String email);
    
    // Método auxiliar para verificar existência ignorando case (útil para cadastro)
    boolean existsByLoginIgnoreCase(String login);
    
    boolean existsByEmailIgnoreCase(String email);
}