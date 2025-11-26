package com.example.CrudN1.controller;

import com.example.CrudN1.model.User;
import com.example.CrudN1.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User body) {
        if (body.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID must be null when creating");
        }
        if (body.getEmail() == null || body.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("name and email are required");
        }
        if (userRepository.existsByEmail(body.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email already exists");
        }
        User saved = userRepository.save(body);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User body) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = existing.get();
        if (body.getName() != null) user.setName(body.getName());
        if (body.getEmail() != null) user.setEmail(body.getEmail());
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}



