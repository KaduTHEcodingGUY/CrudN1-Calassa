package info.datahorizons.AuthServer.web.rest;

import info.datahorizons.AuthServer.model.EnumStatus;
import info.datahorizons.AuthServer.model.UserAuth;
import info.datahorizons.AuthServer.repository.UserAuthRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class PublicAuthRest {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public PublicAuthRest(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, 
                          JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        // Aceita tanto "login" quanto "email" como campo de identificação
        String identifier = loginRequest.get("email");
        if (identifier == null || identifier.isBlank()) {
            identifier = loginRequest.get("login");
        }
        String password = loginRequest.get("password");

        if (identifier == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email/login e senha são obrigatórios"));
        }

        // Busca pelo email primeiro, depois por login
        Optional<UserAuth> userOpt = userAuthRepository.findByEmailAndActiveTrue(identifier);
        if (userOpt.isEmpty()) {
            userOpt = userAuthRepository.findByLoginAndActiveTrue(identifier);
        }

        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuário ou senha inválidos"));
        }

        UserAuth user = userOpt.get();
        Instant now = Instant.now();
        long expiry = 36000L; // 10 horas

        // Gera o Token com os dados do usuário
        String[] roles = user.getRoles() != null ? user.getRoles().split(",") : new String[]{};
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:8082/auth-server")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getLogin())
                .claim("roles", roles)
                .claim("tenant", user.getTenant() != null ? user.getTenant() : "master")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("token_type", "Bearer");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");
        String roles = registerRequest.getOrDefault("roles", "USER");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email e senha são obrigatórios"));
        }

        // Verificar se já existe
        if (userAuthRepository.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email já cadastrado"));
        }

        // Criar login a partir do email (parte antes do @)
        String login = email.split("@")[0].toLowerCase();

        UserAuth newUser = new UserAuth();
        newUser.setLogin(login);
        newUser.setEmail(email.toLowerCase());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(roles);
        newUser.setStatus(EnumStatus.ACTIVE);
        newUser.setTenant("master");
        newUser.setActive(true);

        userAuthRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
            "message", "Usuário criado com sucesso",
            "login", login,
            "email", email
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("password");

        if (email == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email e nova senha são obrigatórios"));
        }

        Optional<UserAuth> userOpt = userAuthRepository.findByEmailAndActiveTrue(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuário não encontrado"));
        }

        UserAuth user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userAuthRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Senha atualizada com sucesso"));
    }

    /**
     * Valida um token JWT e retorna as informações do usuário
     * Endpoint para ser usado por outras aplicações para validar tokens
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "valid", false,
                "message", "Token é obrigatório"
            ));
        }

        // Remove "Bearer " se presente
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Jwt jwt = jwtDecoder.decode(token);
            
            // Verifica se o token expirou
            Instant expiresAt = jwt.getExpiresAt();
            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                return ResponseEntity.ok(Map.of(
                    "valid", false,
                    "message", "Token expirado"
                ));
            }

            // Token válido - retorna as claims
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("subject", jwt.getSubject());
            response.put("roles", jwt.getClaim("roles"));
            response.put("tenant", jwt.getClaim("tenant"));
            response.put("issuedAt", jwt.getIssuedAt());
            response.put("expiresAt", jwt.getExpiresAt());
            
            return ResponseEntity.ok(response);

        } catch (JwtException e) {
            return ResponseEntity.ok(Map.of(
                "valid", false,
                "message", "Token inválido: " + e.getMessage()
            ));
        }
    }

    /**
     * Retorna informações do usuário autenticado a partir do token
     * Use o header Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "Token não fornecido. Use o header Authorization: Bearer <token>"
            ));
        }

        String token = authHeader.substring(7);

        try {
            Jwt jwt = jwtDecoder.decode(token);
            
            // Verifica expiração
            Instant expiresAt = jwt.getExpiresAt();
            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Token expirado"
                ));
            }

            String login = jwt.getSubject();
            
            // Busca dados do usuário no banco
            Optional<UserAuth> userOpt = userAuthRepository.findByLoginAndActiveTrue(login);
            
            Map<String, Object> response = new HashMap<>();
            response.put("login", login);
            response.put("roles", jwt.getClaim("roles"));
            response.put("tenant", jwt.getClaim("tenant"));
            
            if (userOpt.isPresent()) {
                UserAuth user = userOpt.get();
                response.put("email", user.getEmail());
                response.put("status", user.getStatus());
                response.put("extra", user.getExtra());
            }
            
            return ResponseEntity.ok(response);

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "Token inválido: " + e.getMessage()
            ));
        }
    }

    /**
     * Verifica apenas se o token é válido (resposta simples)
     * Útil para health checks de autenticação
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "authenticated", false
            ));
        }

        String token = authHeader.substring(7);

        try {
            Jwt jwt = jwtDecoder.decode(token);
            
            Instant expiresAt = jwt.getExpiresAt();
            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "authenticated", false,
                    "reason", "expired"
                ));
            }

            return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "user", jwt.getSubject()
            ));

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "authenticated", false,
                "reason", "invalid"
            ));
        }
    }
}