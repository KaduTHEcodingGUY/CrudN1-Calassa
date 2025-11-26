package com.example.CrudN1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Value("${auth.server.url:http://localhost:8082/auth-server}")
    private String authServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> authenticate(String email, String password) {
        try {
            String tokenUrl = authServerUrl + "/auth/login";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("login", email);
            body.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro no login: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        return token != null && !token.isEmpty();
    }

    public Map<String, Object> register(String email, String password) {
        try {
            String registerUrl = authServerUrl + "/auth/register";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("login", email);
            body.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(registerUrl, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            }
            return null;
        } catch (HttpClientErrorException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", e.getResponseBodyAsString());
            errorResponse.put("status", e.getStatusCode().value());
            return errorResponse;
        } catch (Exception e) {
            throw new RuntimeException("Erro no cadastro: " + e.getMessage());
        }
    }
}