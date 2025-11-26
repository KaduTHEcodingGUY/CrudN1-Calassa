package com.example.CrudN1.controller;

import com.example.CrudN1.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || password == null || email.trim().isEmpty() || password.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email e senha são obrigatórios"));
            }

            Map<String, Object> tokenResponse = authService.authenticate(email, password);
            
            if (tokenResponse != null && tokenResponse.containsKey("access_token")) {
                return ResponseEntity.ok(tokenResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciais inválidas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro ao processar autenticação: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // O logout é feito no frontend removendo o token do localStorage
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> credentials) {
        try {
            System.out.println("=== REGISTER DEBUG ===");
            System.out.println("Credentials recebidas: " + credentials);
            
            String email = credentials.get("email");
            String password = credentials.get("password");
            
            System.out.println("Email: " + email);
            System.out.println("Password: " + (password != null ? "[PRESENTE]" : "null"));

            if (email == null || password == null || email.trim().isEmpty() || password.isEmpty()) {
                System.out.println("ERRO: Campos vazios ou nulos!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email e senha são obrigatórios"));
            }

            Map<String, Object> registerResponse = authService.register(email, password);
            
            if (registerResponse != null) {
                if (registerResponse.containsKey("error") && (Boolean) registerResponse.get("error")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", registerResponse.get("message")));
                }
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuário cadastrado com sucesso"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Erro ao cadastrar usuário"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro ao processar cadastro: " + e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "message", "Token não fornecido"));
        }

        String token = authHeader.substring(7);
        boolean isValid = authService.validateToken(token);
        
        if (isValid) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "message", "Token inválido ou expirado"));
        }
    }
}

