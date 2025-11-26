package info.datahorizons.AuthServer.config;

import info.datahorizons.AuthServer.model.UserAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;


@Configuration
public class CustomTokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            Authentication principal = context.getPrincipal();
            
            // Fluxo 1: Login de Usuário (Futuro)
            if (principal.getPrincipal() instanceof UserAuth) {
                UserAuth user = (UserAuth) principal.getPrincipal();
                // O Spring Security já converte Authorities para claims, mas podemos reforçar
                 context.getClaims().claim("tenant", user.getTenant());
                 if (user.getExtra() != null) context.getClaims().claim("extra", user.getExtra());
            }
            
            // Fluxo 2: Login de Cliente (Client Credentials - O que estamos usando agora)
            else if (principal instanceof OAuth2ClientAuthenticationToken) {
                // Injetamos a ROLE_ADMIN na força bruta para o cliente 'teste'
                context.getClaims().claim("roles", List.of("ROLE_ADMIN")); 
                context.getClaims().claim("tenant", "master");
            }
        };
    }
}